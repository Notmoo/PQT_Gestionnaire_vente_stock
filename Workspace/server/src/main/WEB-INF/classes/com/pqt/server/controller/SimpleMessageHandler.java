package com.pqt.server.controller;

import com.pqt.core.entities.messages.Message;
import com.pqt.core.entities.messages.MessageType;
import com.pqt.server.module.account.AccountService;
import com.pqt.server.module.client.ClientService;
import com.pqt.server.module.sale.SaleService;
import com.pqt.server.module.state.ServerStateService;
import com.pqt.server.module.statistics.StatisticsService;
import com.pqt.server.module.stock.StockService;

import java.util.HashMap;
import java.util.Map;

public class SimpleMessageHandler implements IMessageHandler {

    private AccountService accountService;
    private SaleService saleService;
    private StatisticsService statisticsService;
    private StockService stockService;
    private ClientService clientService;
    private ServerStateService serverStateService;

    private Map<MessageType, IMessageProcess> queryHandlers;

    public SimpleMessageHandler() {
        serverStateService = new ServerStateService();
        accountService = new AccountService();
        clientService = new ClientService();
        saleService = new SaleService();
        stockService = new StockService();
        statisticsService = new StatisticsService(stockService, saleService);

        queryHandlers = new HashMap<>();
        //TODO ajouter callables à la map
    }

    @Override
    public Message handleMessage(Message message) {

        if(queryHandlers.containsKey(message.getType()))
            return queryHandlers.get(message.getType()).execute(message);

        return null;
    }

    //TODO ajouter Javadoc
    private interface IMessageProcess{
        Message execute(Message request);
    }
}
