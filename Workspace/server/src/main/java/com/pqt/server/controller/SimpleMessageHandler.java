package com.pqt.server.controller;

import com.pqt.core.common_resources.statistics.StatisticFields;
import com.pqt.core.communication.GSonMessageToolFactory;
import com.pqt.core.communication.IMessageToolFactory;
import com.pqt.core.entities.messages.Message;
import com.pqt.core.entities.messages.MessageType;
import com.pqt.core.entities.product.LightweightProduct;
import com.pqt.core.entities.product.Product;
import com.pqt.core.entities.product.ProductUpdate;
import com.pqt.core.entities.sale.LightweightSale;
import com.pqt.core.entities.server_config.ServerConfig;
import com.pqt.core.entities.user_account.Account;
import com.pqt.core.entities.user_account.AccountLevel;
import com.pqt.core.entities.user_account.AccountUpdate;
import com.pqt.server.exception.ServerQueryException;
import com.pqt.server.module.account.AccountService;
import com.pqt.server.module.sale.SaleService;
import com.pqt.server.module.state.ServerStateService;
import com.pqt.server.module.statistics.StatisticsService;
import com.pqt.server.module.stock.StockService;
import com.pqt.server.tools.io.ISerialFileManager;
import com.pqt.server.tools.io.SimpleSerialFileManagerFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

//TODO Paramétrer les supports de query et leurs permissions via un meilleur système (config file, etc ...)
/**
 * Implémentation de l'interface {@link IMessageHandler}. Cette classe définit le gestionnaire de message par défaut du
 * serveur de données du projet PQT.
 * <p/>
 * Liste des requêtes supportées :<br/>
 *  <ul>
 *      <li>QUERY_STOCK (WAITER)</li>
 *      <li>QUERY_SALE (WAITER)</li>
 *      <li>QUER_STAT (WAITER)</li>
 *      <li>QUERY_UPDATE (MASTER)</li>
 *      <li>QUERY_ACCOUNT_LIST (NONE)</li>
 *      <li>QUERY_CONNECT_ACCOUNT (NONE)</li>
 *      <li>QUERY_PING (NONE)</li>
 *      <li>QUERY_CONFIG_LIST (NONE)</li>
 *  </ul>
 * <p/>
 * Liste des requêtes non-supportées :<br/>
 *  <ul>
 *      <li>QUERY_REVERT_SALE</li>
 *      <li>QUERY_LAST_SALES_LIST</li>
 *  </ul>
 * @see IMessageHandler
 * @version 1.0
 * @author Guillaume "Cess" Prost
 */
public class SimpleMessageHandler implements IMessageHandler {

    private static Logger LOGGER = LogManager.getLogger(SimpleMessageHandler.class);

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

    public SimpleMessageHandler(String ressourceFolderPathStr) {
        LOGGER.info("Initialisation du gestionnaire de messages entrant");
        LOGGER.info("Emplacement des ressources du serveur : {}", ressourceFolderPathStr);
        serverStateService = new ServerStateService();
        accountService = new AccountService(ressourceFolderPathStr);
        //clientService = new ClientService();
        stockService = new StockService(ressourceFolderPathStr);
        saleService = new SaleService(stockService, ressourceFolderPathStr);
        statisticsService = new StatisticsService(stockService, saleService);
        messageToolFactory = new GSonMessageToolFactory();

        manager = new MessageManager();

        /*
        WAITER-restricted queries
         */
        manager.supportForConnectedAccounts(MessageType.QUERY_STOCK, (message)->{
            Map<String, String> fields = new HashMap<>();
            fields.put("stock", messageToolFactory.getListFormatter(Product.class).format(stockService.getProductList()));
            return new Message(MessageType.MSG_STOCK, serverStateService.getServer(), message.getEmitter(), message.getUser(), message, fields);
        }, AccountLevel.WAITER);
        manager.supportForConnectedAccounts(MessageType.QUERY_SALE, (message)->{
            Map<String, String> fields = new HashMap<>();
            try {
                long saleId = saleService.submitSale(messageToolFactory.getObjectParser(LightweightSale.class).parse(message.getField("lightweight_sale")));
                fields.put("saleId", Long.toString(saleId));
                return new Message(MessageType.ACK_SALE, serverStateService.getServer(), message.getEmitter(), message.getUser(), message, fields);
            }catch(NullPointerException e){
                fields.put(header_ref_query, e.toString());
                return new Message(MessageType.REFUSED_QUERY, serverStateService.getServer(), message.getEmitter(), message.getUser(), message, fields);
            }
        }, AccountLevel.WAITER);
        manager.supportForConnectedAccounts(MessageType.QUERY_STAT, (message)->{
            Map<String, String> fields = new HashMap<>();
            try{
                fields.put(StatisticFields.TOTAL_SALE_WORTH.getStr(), Double.toString(statisticsService.getTotalSaleWorth()));
                fields.put(StatisticFields.TOTAL_SALE_AMOUNT.getStr(), Integer.toString(statisticsService.getTotalAmountSale()));
                fields.put(StatisticFields.TOTAL_MONEY_MADE.getStr(), Double.toString(statisticsService.getTotalMoneyMade()));
                fields.put(StatisticFields.TOP_POPULAR_PRODUCTS.getStr(), messageToolFactory.getListFormatter(LightweightProduct.class).format(statisticsService.getTopPopularProducts(5)));
                fields.put(StatisticFields.STAFF_SALE_WORTH.getStr(), Double.toString(statisticsService.getStaffSaleWorth()));
                fields.put(StatisticFields.STAFF_SALE_AMOUNT.getStr(), Integer.toString(statisticsService.getStaffSaleAmount()));
                fields.put(StatisticFields.GUEST_SALE_WORTH.getStr(), Double.toString(statisticsService.getGuestSaleWorth()));
                fields.put(StatisticFields.GUEST_SALE_AMOUNT.getStr(), Integer.toString(statisticsService.getGuestSaleAmount()));
            }catch(Exception e){
                e.printStackTrace();
            }
            return new Message(MessageType.MSG_STAT, serverStateService.getServer(), message.getEmitter(), message.getUser(), message, fields);
        }, AccountLevel.WAITER);

        /*
        MASTER-restricted queries
         */
        manager.supportForConnectedAccounts(MessageType.QUERY_STOCK_UPDATE, (message)->{
            try{
                List<ProductUpdate> updates = messageToolFactory.getListParser(ProductUpdate.class).parse(message.getField("updates"));
                stockService.applyUpdateList(updates);
                return new Message(MessageType.ACK_STOCK_UPDATE, serverStateService.getServer(), message.getEmitter(), message.getUser(), message, null);
            }catch (Exception e){
                Map<String, String> fields = new HashMap<>();
                fields.put(header_err_query, e.toString());
                return new Message(MessageType.ERROR_QUERY, serverStateService.getServer(), message.getEmitter(), message.getUser(), message, fields);
            }
        }, AccountLevel.MASTER);

        manager.supportForConnectedAccounts(MessageType.QUERY_ACCOUNT_UPDATE, (message)->{
            try{
                List<AccountUpdate> updates = messageToolFactory.getListParser(AccountUpdate.class).parse(message.getField("updates"));
                accountService.applyUpdateList(updates);
                return new Message(MessageType.ACK_ACCOUNT_UPDATE, serverStateService.getServer(), message.getEmitter(), message.getUser(), message, null);
            }catch (Exception e){
                Map<String, String> fields = new HashMap<>();
                fields.put(header_err_query, e.toString());
                return new Message(MessageType.ERROR_QUERY, serverStateService.getServer(), message.getEmitter(), message.getUser(), message, fields);
            }
        }, AccountLevel.MASTER);
        /*
        Queries without account connection requirements
         */
        manager.support(MessageType.QUERY_ACCOUNT_LIST, (message)->{
            Map<String, String> fields = new HashMap<>();
            fields.put("accounts", messageToolFactory.getListFormatter(Account.class).format(accountService.getAccountList()));
            return new Message(MessageType.MSG_ACCOUNT_LIST, serverStateService.getServer(), message.getEmitter(), message.getUser(), message, fields);
        }, AccountLevel.getLowest(), false);
        manager.support(MessageType.QUERY_CONNECT_ACCOUNT, (message)->{
            final String desiredStateFieldHeader = "desired_state",
                    accountCredentialsFieldHeader = "account";

            if(message.getFields().containsKey(desiredStateFieldHeader)){
                if(message.getFields().containsKey(accountCredentialsFieldHeader)){
                    boolean desiredState = messageToolFactory.getObjectParser(Boolean.class).parse(message.getField(desiredStateFieldHeader));
                    Account accountCredentials = messageToolFactory.getObjectParser(Account.class).parse(message.getField(accountCredentialsFieldHeader));

                    if(accountService.submitAccountCredentials(accountCredentials, desiredState)){
                        return new Message(MessageType.ACK_CONNECT_ACCOUNT, serverStateService.getServer(), message.getEmitter(), message.getUser(), message, null);
                    }else{
                        Map<String, String> fields = new HashMap<>();
                        fields.put(header_ref_query, "Impossible d'effectuer l'action : identifiants invalides ou état désiré déjà atteint");

                        return new Message(MessageType.REFUSED_QUERY, serverStateService.getServer(), message.getEmitter(), message.getUser(), message, fields);
                    }
                }else{
                    return getMissingArgumentQueryReplyMessage(message, accountCredentialsFieldHeader);
                }
            }else{
                return getMissingArgumentQueryReplyMessage(message, desiredStateFieldHeader);
            }
        }, AccountLevel.getLowest(), false);
        manager.support(MessageType.QUERY_PING, (message)->new Message(MessageType.ACK_PING, serverStateService.getServer(), message.getEmitter(), message.getUser(), message, null), AccountLevel.getLowest(), false);
        manager.support(MessageType.QUERY_CONFIG_LIST, (message)->{
            Map<String, String> fields = new HashMap<>();
            fields.put("config", messageToolFactory.getObjectFormatter(ServerConfig.class).format(serverStateService.getConfig()));
            return new Message(MessageType.MSG_CONFIG_LIST, serverStateService.getServer(), message.getEmitter(), message.getUser(), message, fields);
        }, AccountLevel.getLowest(), false);
    }

    private Message getUnsupportedQueryReplyMessage(Message message){
        final String msg_ref_unsupported_query = "Ce type de requêtes n'est actuellement pas supportée par ce serveur.";
        Map<String, String> fields = new HashMap<>();
        fields.put(header_ref_query, msg_ref_unsupported_query);

        return new Message(MessageType.REFUSED_QUERY,
                serverStateService.getServer(),
                message.getEmitter(),
                message.getUser(),
                message,
                fields);
    }

    private Message getMissingArgumentQueryReplyMessage(Message message, String missingArgumentHeader){
        Map<String, String> fields = new HashMap<>();
        fields.put(header_err_query, "The following required header is missing : "+missingArgumentHeader);

        return new Message(MessageType.ERROR_QUERY,
                serverStateService.getServer(),
                message.getEmitter(),
                message.getUser(),
                message,
                fields);
    }

    private Message getExceptionOccuredQueryReplyMessage(Message message, Exception exception){
        Map<String, String> fields = new HashMap<>();
        fields.put(header_err_query, exception.getMessage());

        return new Message(MessageType.ERROR_QUERY,
                serverStateService.getServer(),
                message.getEmitter(),
                message.getUser(),
                message,
                fields);
    }

    @Override
    public Message handleMessage(Message message) {

        Map<String, String> fields = new HashMap<>();

        if(manager.contains(message.getType())){
            if(manager.isQueryRestrictedToConnectedAccount(message.getType())) {
                if (!isAccountRegisteredAndConnected(message)) {
                    fields.put(header_ref_query, "Compte utilisateur inconnu ou déconnecté.");
                    return new Message(MessageType.REFUSED_QUERY, serverStateService.getServer(), message.getEmitter(), message.getUser(), message, fields);
                }
                if (!checkAccountPermission(message)) {
                    fields.put(header_ref_query, "Compte utilisateur avec permission trop faible");
                    return new Message(MessageType.REFUSED_QUERY, serverStateService.getServer(), message.getEmitter(), message.getUser(), message, fields);
                }
            }
            try{
                return manager.getProcess(message.getType()).execute(message);
            }catch(Exception e){
                return getExceptionOccuredQueryReplyMessage(message, e);
            }
        }

        return getUnsupportedQueryReplyMessage(message);
    }

    /**
     * Interface interne utilisé pour encapsuler le traitement des messages dans un objet
     */
    private interface IMessageProcess{
        Message execute(Message request);
    }

    private class MessageTypeEntry{
        private MessageType type;
        private IMessageProcess process;
        private AccountLevel level;
        private boolean connectedAccountRestriction;

        MessageTypeEntry(MessageType type, IMessageProcess process, AccountLevel level, boolean connectedAccountRestriction) {
            this.type = type;
            this.process = process;
            this.level = level;
            this.connectedAccountRestriction = connectedAccountRestriction;
        }

        IMessageProcess getProcess() {
            return process;
        }

        AccountLevel getLevel() {
            return level;
        }

        boolean isConnectedAccountRestriction() {
            return connectedAccountRestriction;
        }

        boolean matches(MessageType type){
            return type.equals(this.type);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MessageTypeEntry that = (MessageTypeEntry) o;

            return connectedAccountRestriction == that.connectedAccountRestriction && type == that.type && process.equals(that.process) && level == that.level;
        }

        @Override
        public int hashCode() {
            int result = type.hashCode();
            result = 31 * result + process.hashCode();
            result = 31 * result + level.hashCode();
            result = 31 * result + (connectedAccountRestriction ? 1 : 0);
            return result;
        }
    }

    private class MessageManager{

        private Set<MessageTypeEntry> entries;


        MessageManager(){
            entries = new HashSet<>();
        }

        void supportForConnectedAccounts(MessageType type, IMessageProcess process, AccountLevel permissionLevel){
            support(type, process, permissionLevel, true);
        }

        void support(MessageType type, IMessageProcess process, AccountLevel permissionLevel, boolean accountConnectionRequired){
            LOGGER.info("Ajout du support du type {} pour le niveau {} (connexion requise : {})", type.name(), permissionLevel.name(), accountConnectionRequired);
            entries.add(new MessageTypeEntry(type, process, permissionLevel, accountConnectionRequired));
        }

        private MessageTypeEntry getFirstMatch(MessageType type){
            return entries.stream().filter(entry->entry.matches(type)).findFirst().orElse(null);
        }

        IMessageProcess getProcess(MessageType messageType){
             MessageTypeEntry entry = getFirstMatch(messageType);
             if(entry!=null)
                 return entry.getProcess();

            return null;
        }

        AccountLevel getLevel(MessageType messageType){
            MessageTypeEntry entry = getFirstMatch(messageType);
            if(entry!=null)
                return entry.getLevel();

            return null;
        }

        boolean contains(MessageType type) {
            return getFirstMatch(type)!=null;
        }

        boolean isQueryRestrictedToConnectedAccount(MessageType type) {
            MessageTypeEntry entry = getFirstMatch(type);
            return entry != null && entry.isConnectedAccountRestriction();
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
