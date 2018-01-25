package com.pqt.client.module.query;

import com.pqt.client.module.account.AccountService;
import com.pqt.client.module.connection.ConnectionService;
import com.pqt.client.module.connection.listeners.IConnectionListener;
import com.pqt.client.module.query.exceptions.HeaderNotFoundException;
import com.pqt.client.module.query.exceptions.MessageTimeoutException;
import com.pqt.client.module.query.query_callback.*;
import com.pqt.core.communication.GSonMessageToolFactory;
import com.pqt.core.communication.IMessageToolFactory;
import com.pqt.core.entities.messages.Message;
import com.pqt.core.entities.messages.MessageType;
import com.pqt.core.entities.product.Product;
import com.pqt.core.entities.product.ProductUpdate;
import com.pqt.core.entities.sale.LightweightSale;
import com.pqt.core.entities.user_account.Account;
import com.pqt.core.entities.user_account.AccountUpdate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

//TODO Issue #5 : écrire javadoc
public class QueryExecutor {

    private static Logger LOGGER = LogManager.getLogger(QueryExecutor.class);

	private IMessageToolFactory messageToolFactory;
	private ConnectionService connectionService;
	private QueryMessageFactory messageFactory;

	public QueryExecutor(ConnectionService connectionService){
	    LOGGER.info("Initialisation du QueryExecutor");
	    messageToolFactory = new GSonMessageToolFactory();
	    this.connectionService = connectionService;
	    this.messageFactory = new QueryMessageFactory(messageToolFactory);
        LOGGER.info("QueryExecutor initialisé");
	}

	public void setAccountService(AccountService accountService){
	    messageFactory.setAccountService(accountService);
    }

    public void executeSaleQuery(LightweightSale sale, INoItemMessageCallback callback) {
        sendMessage(messageFactory.newSaleMessage(sale), callback, MessageType.ACK_SALE);
    }

    public void executePingQuery(INoItemMessageCallback callback){
        sendMessage(messageFactory.newPingMessage(), callback, MessageType.ACK_PING);
    }

    public void executeStockUpdateQuery(List<ProductUpdate> updates, INoItemMessageCallback callback) {
        sendMessage(messageFactory.newStockUpdateMessage(updates), callback, MessageType.ACK_STOCK_UPDATE);
    }

    public void executeAccountUpdateQuery(List<AccountUpdate> updates, INoItemMessageCallback callback) {
        sendMessage(messageFactory.newAccountUpdateMessage(updates), callback, MessageType.ACK_ACCOUNT_UPDATE);
    }

    public void executeConnectAccountQuery(Account account, boolean desiredState, INoItemMessageCallback callback){
        sendMessage(messageFactory.newConnectAccountMessage(account,desiredState), callback, MessageType.ACK_CONNECT_ACCOUNT);
    }

    private void sendMessage(Message message, INoItemMessageCallback callback, MessageType responseType){
	    LOGGER.debug("Envoi d'un message de type '{}' en mode no-item", message.getType().name());
        connectionService.sendText(messageToolFactory.getObjectFormatter(Message.class).format(message), new IConnectionListener() {
            @Override
            public void onMessageReceivedEvent(String msg) {
                LOGGER.trace("Réception d'une réponse : {}",msg);
                Message response = messageToolFactory.getObjectParser(Message.class).parse(msg);
                if(response.getType().equals(responseType))
                    callback.ack();
                else
                    handleUnexpectedTypeInResponse(response, callback);
            }

            @Override
            public void onConnectedEvent() {
                LOGGER.trace("Connexion au serveur");
            }

            @Override
            public void onDisconnectedEvent() {
                LOGGER.trace("Déconnexion au serveur");
            }

            @Override
            public void onTimeOutEvent() {
                LOGGER.trace("Timeout du server");
                callback.err(new MessageTimeoutException());
            }

            @Override
            public void onConnexionError(Throwable e) {
                LOGGER.warn("Erreur durant l'envoi d'un message : {}", e);
                callback.err(e);
            }
        });
    }

    public void executeStockQuery(ICollectionItemMessageCallback<Product> callback) {
        sendMessage(messageFactory.newStockMessage(), callback, Product.class, MessageType.MSG_STOCK, "stock");
    }

    public void executeAccountListQuery(ICollectionItemMessageCallback<Account> callback){
        sendMessage(messageFactory.newAccountListMessage(), callback, Account.class, MessageType.MSG_ACCOUNT_LIST, "accounts");
    }

    private <T> void sendMessage(Message message, ICollectionItemMessageCallback<T> callback, Class<T> clazz, MessageType responseType, String itemHeader){
        LOGGER.debug("Envoi d'un message de type '{}' en mode collection-item", message.getType().name());
        connectionService.sendText(messageToolFactory.getObjectFormatter(Message.class).format(message), new IConnectionListener() {
            @Override
            public void onMessageReceivedEvent(String msg) {
                LOGGER.trace("Réception d'une réponse : {}",msg);
                Message response = messageToolFactory.getObjectParser(Message.class).parse(msg);
                if(response.getType().equals(responseType)) {
                    String item = response.getField(itemHeader);
                    if (item != null)
                        callback.ack(messageToolFactory.getListParser(clazz).parse(item));
                    else
                        callback.err(new HeaderNotFoundException("Missing expected header \""+
                                itemHeader+"\" in response \""+responseType.name()+"\""));
                }else
                    handleUnexpectedTypeInResponse(response, callback);
            }

            @Override
            public void onConnectedEvent() {
                LOGGER.trace("Connexion au serveur");
            }

            @Override
            public void onDisconnectedEvent() {
                LOGGER.trace("Déconnexion au serveur");
            }

            @Override
            public void onTimeOutEvent() {
                LOGGER.trace("Timeout du server");
                callback.err(new MessageTimeoutException());
            }

            @Override
            public void onConnexionError(Throwable e) {
                LOGGER.warn("Erreur durant l'envoi d'un message : {}", e);
                callback.err(e);
            }
        });
    }

    public void executeStatQuery(IMapItemMessageCallback<String, String> callback) {
        sendMessage(messageFactory.newStatMessage(), callback, MessageType.MSG_STAT);
    }

    public void executeConfigListQuery(IMapItemMessageCallback<String, String> callback){
        sendMessage(messageFactory.newConfigListMessage(), callback, MessageType.MSG_CONFIG_LIST);
    }

    //TODO à rendre générique pour toute Map<T, U> au lieu de Map<String, String>
    private void sendMessage(Message message, IMapItemMessageCallback<String, String> callback, MessageType responseType){
        LOGGER.debug("Envoi d'un message de type '{}' en mode map-item", message.getType().name());
        connectionService.sendText(messageToolFactory.getObjectFormatter(Message.class).format(message), new IConnectionListener() {
            @Override
            public void onMessageReceivedEvent(String msg) {
                LOGGER.trace("Réception d'une réponse : {}",msg);
                Message response = messageToolFactory.getObjectParser(Message.class).parse(msg);
                if(response.getType().equals(responseType)){
                    callback.ack(response.getFields());
                }else
                    handleUnexpectedTypeInResponse(response, callback);
            }

            @Override
            public void onConnectedEvent() {
                LOGGER.trace("Connexion au serveur");
            }

            @Override
            public void onDisconnectedEvent() {
                LOGGER.trace("Déconnexion au serveur");
            }

            @Override
            public void onTimeOutEvent() {
                LOGGER.trace("Timeout du server");
                callback.err(new MessageTimeoutException());
            }

            @Override
            public void onConnexionError(Throwable e) {
                LOGGER.warn("Erreur durant l'envoi d'un message : {}", e);
                callback.err(e);
            }
        });
    }

    private void handleUnexpectedTypeInResponse(Message response, IMessageCallback callback){
        switch (response.getType()) {
            case ERROR_QUERY:
                try{
                    callback.err(new Exception(response.getField("Detail_erreur")));
                }catch(Throwable e){
                    e.printStackTrace();
                    callback.err(null);
                }
                break;
            case REFUSED_QUERY:
                try{
                    callback.ref(new Exception(response.getField("Detail_refus")));
                }catch(Throwable e){
                    e.printStackTrace();
                    callback.ref(null);
                }
                break;
            default:
                callback.err(new IllegalArgumentException(
                        "Illegal message type for response : " +
                                "expected \"ACK_SALE\"`, found \"" + response.getType().name() + "\""
                ));
                break;
        }
    }

    public void shutdown() {
        LOGGER.info("Fermeture du QueryExecutor");
        //Nothing to do
    }
}
