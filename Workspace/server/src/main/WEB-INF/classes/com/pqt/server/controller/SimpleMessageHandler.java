package com.pqt.server.controller;

import com.pqt.core.communication.GSonMessageToolFactory;
import com.pqt.core.communication.IMessageToolFactory;
import com.pqt.core.entities.members.Client;
import com.pqt.core.entities.messages.Message;
import com.pqt.core.entities.messages.MessageType;
import com.pqt.core.entities.product.LightweightProduct;
import com.pqt.core.entities.product.Product;
import com.pqt.core.entities.product.ProductUpdate;
import com.pqt.core.entities.sale.Sale;
import com.pqt.core.entities.user_account.Account;
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
public class SimpleMessageHandler implements IMessageHandler {

    private final String header_ref_query = "Detail_refus";
    private final String header_err_query = "Detail_erreur";

    private AccountService accountService;
    private SaleService saleService;
    private StatisticsService statisticsService;
    private StockService stockService;
    private ClientService clientService;
    private ServerStateService serverStateService;
    private IMessageToolFactory messageToolFactory;

    private Map<MessageType, IMessageProcess> queryHandlers;

    public SimpleMessageHandler() {
        serverStateService = new ServerStateService();
        accountService = new AccountService();
        clientService = new ClientService();
        saleService = new SaleService();
        stockService = new StockService();
        statisticsService = new StatisticsService(stockService, saleService);
        messageToolFactory = new GSonMessageToolFactory();

        queryHandlers = new HashMap<>();

        queryHandlers.put(MessageType.QUERY_STOCK, (message)->{
            Map<String, String> fields = new HashMap<>();
            fields.put("stock", messageToolFactory.getListFormatter(Product.class).format(stockService.getProductList()));
            return new Message(MessageType.MSG_STOCK, serverStateService.getServer(), message.getEmitter(), message.getUser(), message, fields);
        });
        queryHandlers.put(MessageType.QUERY_SALE, (message)->{
            Map<String, String> fields = new HashMap<>();
            try {
                long saleId = saleService.submitSale(messageToolFactory.getObjectParser(Sale.class).parse(message.getField("sale")));
                fields.put("saleId", Long.toString(saleId));
                return new Message(MessageType.ACK_SALE, serverStateService.getServer(), message.getEmitter(), message.getUser(), message, fields);
            }catch(ServerQueryException | NullPointerException e){
                fields.put(header_ref_query, e.toString());
                return new Message(MessageType.REFUSED_QUERY, serverStateService.getServer(), message.getEmitter(), message.getUser(), message, fields);
            }
        });
        queryHandlers.put(MessageType.QUERY_REVERT_SALE, (message)->{
            try{
                saleService.submitSaleRevert(messageToolFactory.getObjectParser(Long.class).parse(message.getField("saleId")));
                return new Message(MessageType.ACK_REVERT_SALE, serverStateService.getServer(), message.getEmitter(), message.getUser(), message, null);
            }catch(ServerQueryException | NullPointerException e){
                Map<String, String> fields = new HashMap<>();
                fields.put(header_err_query, e.toString());
                return new Message(MessageType.ERROR_QUERY, serverStateService.getServer(), message.getEmitter(), message.getUser(), message, fields);
            }
         });
        queryHandlers.put(MessageType.QUERY_STAT, (message)->{
            Map<String, String> fields = new HashMap<>();
            fields.put("total_sale_worth", Double.toString(statisticsService.getTotalSaleWorth()));
            fields.put("total_sale_amount", Integer.toString(statisticsService.getTotalAmountSale()));
            fields.put("total_money_made", Double.toString(statisticsService.getTotalMoneyMade()));
            fields.put("top_popular_products", messageToolFactory.getListFormatter(LightweightProduct.class).format(statisticsService.getTopPopularProducts(5)));
            fields.put("staff_sale_worth",Double.toString(statisticsService.getStaffSaleWorth()));
            fields.put("staff_sale_amount",Integer.toString(statisticsService.getStaffSaleAmount()));
            fields.put("guest_sale_worth",Double.toString(statisticsService.getGuestSaleAmount()));
            fields.put("guest_sale_amount",Integer.toString(statisticsService.getStaffSaleAmount()));

            return new Message(MessageType.MSG_STAT, serverStateService.getServer(), message.getEmitter(), message.getUser(), message, fields);
        });
        queryHandlers.put(MessageType.QUERY_UPDATE, (message)->{
            try{
                List<ProductUpdate> updates = messageToolFactory.getListParser(ProductUpdate.class).parse(message.getField("updates"));
                stockService.applyUpdateList(updates);
                return new Message(MessageType.ACK_UPDATE, serverStateService.getServer(), message.getEmitter(), message.getUser(), message, null);
            }catch (ServerQueryException | NullPointerException e){
                Map<String, String> fields = new HashMap<>();
                fields.put(header_err_query, e.toString());
                return new Message(MessageType.ERROR_QUERY, serverStateService.getServer(), message.getEmitter(), message.getUser(), message, fields);
            }
        });
    }

    @Override
    public Message handleMessage(Message message) {

        Map<String, String> fields = new HashMap<>();

        if(!isAccountRegistered(message)){
            fields.put(header_ref_query, "Compte utilisateur inconnu");
            return new Message(MessageType.REFUSED_QUERY, serverStateService.getServer(), message.getEmitter(), message.getUser(), message, fields);
        }
        if(!checkAccountPermission(message)){
            fields.put(header_ref_query, "Compte utilisateur avec permission trop faible");
            return new Message(MessageType.REFUSED_QUERY, serverStateService.getServer(), message.getEmitter(), message.getUser(), message, fields);
        }
        if(queryHandlers.containsKey(message.getType())){
            return queryHandlers.get(message.getType()).execute(message);
        }

        fields.put(header_err_query, "Type requête non pris en charge par ce serveur");
        return new Message(MessageType.ERROR_QUERY, serverStateService.getServer(), message.getEmitter(), message.getUser(), message, fields);
    }

    //TODO ajouter Javadoc
    private interface IMessageProcess{
        Message execute(Message request);
    }

    private boolean isAccountRegistered(Message message){
        //TODO faire ça
        return false;
    }

    private boolean checkAccountPermission(Message message){
        //TODO faire ça
        return false;
    }
}
