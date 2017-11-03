package com.pqt.client.module;

import com.pqt.client.module.account.AccountService;
import com.pqt.client.module.connection.ConnectionService;
import com.pqt.client.module.network.NetworkService;
import com.pqt.client.module.network.listeners.INetworkServiceListener;
import com.pqt.client.module.query.QueryExecutor;
import com.pqt.client.module.sale.SaleService;
import com.pqt.client.module.stat.StatService;
import com.pqt.client.module.stock.StockService;

public class ClientBackEndModuleManager {

    private final SaleService saleService;
    private final StockService stockService;
    private final AccountService accountService;
    private final StatService statService;
    private final NetworkService networkService;

    public ClientBackEndModuleManager(String serverUrl) {
        ConnectionService connectionService = new ConnectionService(serverUrl);
        QueryExecutor queryExecutor = new QueryExecutor(connectionService);
        saleService = new SaleService(queryExecutor);
        stockService = new StockService(queryExecutor);
        accountService = new AccountService(queryExecutor);
        statService = new StatService(queryExecutor);
        networkService = new NetworkService(queryExecutor, connectionService);

        queryExecutor.setAccountService(accountService);

        networkService.addListener(new INetworkServiceListener() {
            @Override
            public void onPQTPingSuccessEvent(String host, Integer port) {
                //No-op
            }

            @Override
            public void onPQTPingFailureEvent(String host, Integer port, Throwable cause) {
                //No-op
            }

            @Override
            public void onNewServerConfigData() {
                //On bind un refresh automatique des données
                //sur la réception d'une nouvelle config serveur
                stockService.refreshProductList();
                accountService.refreshAccounts();
                statService.refreshStats();
            }
        });
    }

    public SaleService getSaleService() {
        return saleService;
    }

    public StockService getStockService() {
        return stockService;
    }

    public AccountService getAccountService() {
        return accountService;
    }

    public StatService getStatService() {
        return statService;
    }

    public NetworkService getNetworkService() {
        return networkService;
    }
}
