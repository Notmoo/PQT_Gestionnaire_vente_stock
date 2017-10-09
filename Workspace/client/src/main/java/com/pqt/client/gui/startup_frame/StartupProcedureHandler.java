package com.pqt.client.gui.startup_frame;

import com.pqt.client.module.account.AccountService;
import com.pqt.client.module.account.listeners.AccountListenerAdapter;
import com.pqt.client.module.account.listeners.IAccountListener;
import com.pqt.client.module.network.NetworkService;
import com.pqt.client.module.network.listeners.INetworkServiceListener;
import com.pqt.core.entities.user_account.Account;

class StartupProcedureHandler {

    private NetworkService networkService;
    private AccountService accountService;

    private String host, username, password;
    private Integer port;

    StartupProcedureHandler(NetworkService networkService, AccountService accountService) {
        this.networkService = networkService;
        this.accountService = accountService;
    }

    StartupProcedureHandler init(String host, Integer port, String username, String password){
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;

        return this;
    }

    void handle(){
        testConnection();
    }

    private void testConnection(){
        networkService.addListener(getPingListener());
        networkService.sendPQTPing(host, port);
    }

    private void useRequestedServer(){
        //TODO notify this
        networkService.setActiveServer(host, port);
        accountService.addListener(getUpdateAccountListListener());
        accountService.refreshAccounts();
    }

    private void connectAccount(){
        Account match = accountService.getAllAccounts().stream()
                .filter(account -> account.getUsername().equals(username))
                .findFirst()
                .orElse(null);

        if(match==null){
            //TODO notify this
        }else{
            accountService.setCurrentAccount(match);
            accountService.addListener(getConnectAccountListener());
            accountService.logInCurrentAccount(StartupProcedureHandler.this.password);
        }
    }

    private INetworkServiceListener getPingListener(){
        return new INetworkServiceListener() {
            @Override
            public void onPQTPingSuccessEvent(String host, Integer port) {
                if(StartupProcedureHandler.this.host.equals(host)
                        && StartupProcedureHandler.this.port.equals(port)){
                    useRequestedServer();
                }
            }

            @Override
            public void onPQTPingFailureEvent(String host, Integer port, Throwable cause) {

            }

            @Override
            public void onNewServerConfigData() {

            }
        };
    }

    private IAccountListener getUpdateAccountListListener(){
        return new AccountListenerAdapter(){
            @Override
            public void onAccountListChangedEvent(){
                connectAccount();
            }
        };
    }

    private IAccountListener getConnectAccountListener(){
        return new AccountListenerAdapter(){
            @Override
            public void onAccountStatusChangedEvent(boolean status) {
                if(status){
                    //TODO notify this
                }else{
                    //TODO notify this
                }
            }
        };
    }
}
