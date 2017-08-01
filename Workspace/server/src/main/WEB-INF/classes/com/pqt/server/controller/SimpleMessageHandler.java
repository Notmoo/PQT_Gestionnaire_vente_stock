package com.pqt.server.controller;

import com.pqt.core.communication.GSonMessageToolFactory;
import com.pqt.core.communication.IMessageToolFactory;
import com.pqt.core.entities.messages.Message;
import com.pqt.core.entities.messages.MessageType;
import com.pqt.core.entities.product.LightweightProduct;
import com.pqt.core.entities.product.Product;
import com.pqt.core.entities.product.ProductUpdate;
import com.pqt.core.entities.sale.Sale;
import com.pqt.core.entities.user_account.AccountLevel;
import com.pqt.server.exception.ServerQueryException;
import com.pqt.server.module.account.AccountService;
import com.pqt.server.module.client.ClientService;
import com.pqt.server.module.sale.SaleService;
import com.pqt.server.module.state.ServerStateService;
import com.pqt.server.module.statistics.StatisticsService;
import com.pqt.server.module.stock.StockService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//TODO ajouter des messages d'erreur spécifiques pour les NullPointerException si le param du message vaut null
//TODO mettre à jour la liste des query supportées lorsque la version du serveur sera proche de la release
//TODO ne pas oublier de préciser le niveau de permission requis pour chaque requête
//TODO Paramétrer les supports de query et leurs permissions via un meilleur système (config file, etc ...)
/**
 * Implémentation de l'interface {@link IMessageHandler}. Cette classe définit le gestionnaire de message par défaut du
 * serveur de données du projet PQT.
 * <p/>
 * Liste des requêtes supportées :<br/>
 *  <ul>
 *
 *  </ul>
 * <p/>
 * Liste des requêtes non-supportées :<br/>
 *  <ul>
 *
 *  </ul>
 * @see IMessageHandler
 * @version 1.0
 * @author Guillaume "Cess" Prost
 */
public class SimpleMessageHandler implements IMessageHandler {

    private final String header_ref_query = "Detail_refus";
    private final String header_err_query = "Detail_erreur";

    /*
     * Liste des services du serveur
     */
    private AccountService accountService;
    private SaleService saleService;
    private StatisticsService statisticsService;
    private StockService stockService;
    //TODO faire qqch de clientService
    //private ClientService clientService;
    private ServerStateService serverStateService;
    private IMessageToolFactory messageToolFactory;

    private MessageManager manager;

    public SimpleMessageHandler() {
        serverStateService = new ServerStateService();
        accountService = new AccountService();
        //clientService = new ClientService();
        stockService = new StockService();
        saleService = new SaleService(stockService);
        statisticsService = new StatisticsService(stockService, saleService);
        messageToolFactory = new GSonMessageToolFactory();

        manager = new MessageManager();

        //TODO ajouter support des query de connexion de compte utilisateur
        manager.support(MessageType.QUERY_STOCK, (message)->{
            Map<String, String> fields = new HashMap<>();
            fields.put("stock", messageToolFactory.getListFormatter(Product.class).format(stockService.getProductList()));
            return new Message(MessageType.MSG_STOCK, serverStateService.getServer(), message.getEmitter(), message.getUser(), message, fields);
        }, AccountLevel.WAITER);
        manager.support(MessageType.QUERY_SALE, (message)->{
            Map<String, String> fields = new HashMap<>();
            try {
                long saleId = saleService.submitSale(messageToolFactory.getObjectParser(Sale.class).parse(message.getField("sale")));
                fields.put("saleId", Long.toString(saleId));
                return new Message(MessageType.ACK_SALE, serverStateService.getServer(), message.getEmitter(), message.getUser(), message, fields);
            }catch(ServerQueryException | NullPointerException e){
                fields.put(header_ref_query, e.toString());
                return new Message(MessageType.REFUSED_QUERY, serverStateService.getServer(), message.getEmitter(), message.getUser(), message, fields);
            }
        }, AccountLevel.WAITER);
        manager.support(MessageType.QUERY_STAT, (message)->{
            Map<String, String> fields = new HashMap<>();
            fields.put("total_sale_worth", Double.toString(statisticsService.getTotalSaleWorth()));
            fields.put("total_sale_amount", Integer.toString(statisticsService.getTotalAmountSale()));
            fields.put("total_money_made", Double.toString(statisticsService.getTotalMoneyMade()));
            fields.put("top_popular_products", messageToolFactory.getListFormatter(LightweightProduct.class).format(statisticsService.getTopPopularProducts(5)));
            fields.put("staff_sale_worth",Double.toString(statisticsService.getStaffSaleWorth()));
            fields.put("staff_sale_amount",Integer.toString(statisticsService.getStaffSaleAmount()));
            fields.put("guest_sale_worth",Double.toString(statisticsService.getGuestSaleWorth()));
            fields.put("guest_sale_amount",Integer.toString(statisticsService.getGuestSaleAmount()));

            return new Message(MessageType.MSG_STAT, serverStateService.getServer(), message.getEmitter(), message.getUser(), message, fields);
        }, AccountLevel.WAITER);
        manager.support(MessageType.QUERY_UPDATE, (message)->{
            try{
                List<ProductUpdate> updates = messageToolFactory.getListParser(ProductUpdate.class).parse(message.getField("updates"));
                stockService.applyUpdateList(updates);
                return new Message(MessageType.ACK_UPDATE, serverStateService.getServer(), message.getEmitter(), message.getUser(), message, null);
            }catch (ServerQueryException | NullPointerException e){
                Map<String, String> fields = new HashMap<>();
                fields.put(header_err_query, e.toString());
                return new Message(MessageType.ERROR_QUERY, serverStateService.getServer(), message.getEmitter(), message.getUser(), message, fields);
            }
        }, AccountLevel.MASTER);
        /*
        manager.support(MessageType.QUERY_REVERT_SALE, (message)->{

            try{
                saleService.submitSaleRevert(messageToolFactory.getObjectParser(Long.class).parse(message.getField("saleId")));
                return new Message(MessageType.ACK_REVERT_SALE, serverStateService.getServer(), message.getEmitter(), message.getUser(), message, null);
            }catch(ServerQueryException | NullPointerException e){
                Map<String, String> fields = new HashMap<>();
                fields.put(header_err_query, e.toString());
                return new Message(MessageType.ERROR_QUERY, serverStateService.getServer(), message.getEmitter(), message.getUser(), message, fields);
            }
         }, AccountLevel.MASTER);
         */
    }

    @Override
    public Message handleMessage(Message message) {

        Map<String, String> fields = new HashMap<>();

        if(!isAccountRegisteredAndConnected(message)){
            fields.put(header_ref_query, "Compte utilisateur inconnu");
            return new Message(MessageType.REFUSED_QUERY, serverStateService.getServer(), message.getEmitter(), message.getUser(), message, fields);
        }
        if(!checkAccountPermission(message)){
            fields.put(header_ref_query, "Compte utilisateur avec permission trop faible");
            return new Message(MessageType.REFUSED_QUERY, serverStateService.getServer(), message.getEmitter(), message.getUser(), message, fields);
        }
        if(manager.contains(message.getType())){
            return manager.getProcess(message.getType()).execute(message);
        }

        fields.put(header_err_query, "Type requête non pris en charge par ce serveur");
        return new Message(MessageType.ERROR_QUERY, serverStateService.getServer(), message.getEmitter(), message.getUser(), message, fields);
    }

    /**
     * Interface interne utilisé pour encapsuler le traitement des messages dans un objet
     */
    private interface IMessageProcess{
        Message execute(Message request);
    }

    private class MessageManager{
        private Map<MessageType, IMessageProcess> processes;
        private Map<MessageType, AccountLevel> levels;

        MessageManager(){
            processes = new HashMap<>();
            levels = new HashMap<>();
        }

        void support(MessageType type, IMessageProcess process, AccountLevel permissionLevel){
            processes.put(type, process);
            levels.put(type, permissionLevel);
        }

        IMessageProcess getProcess(MessageType messageType){
            return processes.get(messageType);
        }

        AccountLevel getLevel(MessageType messageType){
            return levels.get(messageType);
        }

        boolean contains(MessageType type) {
            return processes.containsKey(type);
        }
    }

    /**
     * Vérifie si le compte utilisé pour émettre la query contenu dans le message est enregistré et connecté.
     * @param message message dont l'expéditeur doit être vérifié
     * @return true si le compte est existant et connecté, false si au moins une des conditions est fausse.
     */
    private boolean isAccountRegisteredAndConnected(Message message){
        return accountService.isAccountRegistered(message.getUser()) && accountService.isAccountConnected(message.getUser());
    }

    /**
     * Vérifie si le compte émetteur de la query a les autorisations suffisantes pour effectuer la query.
     * <p/>
     * @param message message dont le niveau de permission de l'expéditeur doit être vérifié
     * @return true si l'expéditeur correspond à un compte et qu'il a les autorisations suffisantes, false sinon.
     */
    private boolean checkAccountPermission(Message message){
        return accountService.isAccountRegistered(message.getUser()) && accountService.getAccountPermissionLevel(message.getUser()).compareTo(manager.getLevel(message.getType()))>=0;
    }
}
