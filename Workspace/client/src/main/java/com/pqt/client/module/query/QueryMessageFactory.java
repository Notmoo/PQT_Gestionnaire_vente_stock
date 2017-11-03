package com.pqt.client.module.query;

import com.pqt.client.module.account.AccountService;
import com.pqt.core.communication.IMessageToolFactory;
import com.pqt.core.entities.messages.Message;
import com.pqt.core.entities.messages.MessageType;
import com.pqt.core.entities.product.ProductUpdate;
import com.pqt.core.entities.sale.Sale;
import com.pqt.core.entities.user_account.Account;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//TODO écrire javadoc
class QueryMessageFactory {

    private final IMessageToolFactory messageToolFactory;
    private AccountService accountService;

    QueryMessageFactory(IMessageToolFactory messageToolFactory) {
        this.messageToolFactory = messageToolFactory;
        accountService = null;
    }

    void setAccountService(AccountService accountService){
        this.accountService = accountService;
    }

    Message newSaleMessage(Sale sale) {
        Map<String, String> fields = new HashMap<>();
        fields.put("sale", messageToolFactory.getObjectFormatter(Sale.class).format(sale));
        return newSimpleMessage(MessageType.QUERY_SALE, fields);
	}

	Message newStockMessage() {
        return newSimpleMessage(MessageType.QUERY_STOCK);
	}

	Message newStatMessage() {
        return newSimpleMessage(MessageType.QUERY_STAT);
	}

	Message newUpdateMessage(List<ProductUpdate> updates) {
        Map<String, String> fields = new HashMap<>();
        fields.put("updates", messageToolFactory.getListFormatter(ProductUpdate.class).format(updates));
        return newSimpleMessage(MessageType.QUERY_UPDATE, fields);
	}

	Message newAccountListMessage(){
        return newSimpleMessage(MessageType.QUERY_ACCOUNT_LIST);
	}

	Message newConnectAccountMessage(Account account, boolean desiredState){
        Map<String, String> fields = new HashMap<>();
        fields.put("account", messageToolFactory.getObjectFormatter(Account.class).format(account));
        fields.put("desired_state", messageToolFactory.getObjectFormatter(Boolean.class).format(desiredState));
        return newSimpleMessage(MessageType.QUERY_CONNECT_ACCOUNT, fields);
	}

	Message newPingMessage(){
        return newSimpleMessage(MessageType.QUERY_PING);
	}

	Message newConfigListMessage(){
        return newSimpleMessage(MessageType.QUERY_CONFIG_LIST);
	}

    private  Message newSimpleMessage(MessageType type, Map<String, String> fields){
	    //TODO add emitter
        Account account = accountService!=null?accountService.getCurrentAccount():null;
        return new Message(type, null, null, account, null, fields);
	}

    private  Message newSimpleMessage(MessageType type){
        return newSimpleMessage(type, null);
    }
}
