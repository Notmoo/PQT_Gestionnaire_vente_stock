package com.pqt.client.gui.frames.startup_frame;

import com.pqt.client.gui.frames.startup_frame.listeners.procedure.IStartupProcedureEventFirerer;
import com.pqt.client.gui.frames.startup_frame.listeners.procedure.IStartupProcedureListener;
import com.pqt.client.gui.frames.startup_frame.listeners.procedure.SimpleStartupProcedureEventFirerer;
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

    private IAccountListener updateAccountListListener, connectAccountListener;
    private  INetworkServiceListener pingListener;

    private IStartupProcedureEventFirerer firerer;

    StartupProcedureHandler(NetworkService networkService, AccountService accountService) {
        this.networkService = networkService;
        this.accountService = accountService;
        firerer = new SimpleStartupProcedureEventFirerer();
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
        //TODO remove sysout
        System.out.println("Test de connexion");
        networkService.addListener(getPingListener(networkService));
        networkService.setActiveServer(host, port);
        networkService.sendPQTPing(host, port);
    }

    private void useRequestedServer(){
        //Server found
        //TODO remove sysout
        System.out.println("Serveur trouvé");
        firerer.fireServerFoundEvent(host, port);
        accountService.addListener(getUpdateAccountListListener());
        accountService.refreshAccounts();
    }

    private void connectAccount(){

        //TODO remove sysout
        System.out.println("Connexion de compte");

        //TODO remove try-catch(Throwable)
        try {
            Account match = accountService.getAllAccounts().stream()
                    .filter(account -> account.getUsername().equals(username))
                    .findFirst()
                    .orElse(null);

            //TODO remove sysout
            System.out.println("Account match value : "+match);

            if(match==null){
                //TODO remove sysout
                System.out.println(" --> Compte inconnu");
                //Compte spécifié inconnu
                firerer.fireUserAccountUnknownEvent(username);
                firerer.fireStartupProcedureFinishedEvent(false);
            }else{
                //TODO remove sysout
                System.out.println(" --> Compte connu");
                accountService.setCurrentAccount(match);
                accountService.addListener(getConnectAccountListener());
                accountService.logInCurrentAccount(StartupProcedureHandler.this.password);
            }
        }catch(Throwable e){
            e.printStackTrace();
            firerer.fireStartupProcedureFinishedEvent(false);
        }
    }

    private INetworkServiceListener getPingListener(NetworkService networkService){
        if(pingListener==null) {
            pingListener = new INetworkServiceListener() {
                @Override
                public void onPQTPingSuccessEvent(String host, Integer port) {
                    if (StartupProcedureHandler.this.host.equals(host)
                            && StartupProcedureHandler.this.port.equals(port)) {
                        useRequestedServer();
                    }
                    StartupProcedureHandler.this.removePingListener();
                }

                @Override
                public void onPQTPingFailureEvent(String host, Integer port, Throwable cause) {
                    StartupProcedureHandler.this.removePingListener();
                    firerer.fireStartupProcedureFinishedEvent(false);
                }

                @Override
                public void onNewServerConfigData() {
                    StartupProcedureHandler.this.removePingListener();
                }
            };
        }
        return pingListener;
    }

    private void removePingListener(){
        if(pingListener!=null){
            networkService.removeListener(pingListener);
            pingListener = null;
        }
    }

    private IAccountListener getUpdateAccountListListener(){
        if(updateAccountListListener==null){
            updateAccountListListener =  new AccountListenerAdapter(){
                @Override
                public void onAccountListChangedEvent(){
                    connectAccount();
                    StartupProcedureHandler.this.removeUpdateAccountListListener();
                }
            };
        }
        return updateAccountListListener;
    }

    private void removeUpdateAccountListListener(){
        if(updateAccountListListener!=null){
            accountService.removeListener(updateAccountListListener);
            updateAccountListListener=null;
        }
    }

    private IAccountListener getConnectAccountListener(){
        if(connectAccountListener==null){
            connectAccountListener = new AccountListenerAdapter(){
                @Override
                public void onAccountStatusChangedEvent(boolean status) {
                    if(status){
                        //Compte connecté
                        //TODO remove sysout
                        System.out.println("Connecté en tant que '"+username+"'");

                        StartupProcedureHandler.this.removeConnectAccountListener();
                        firerer.fireUserAccountConnectedEvent(username);
                        firerer.fireStartupProcedureFinishedEvent(true);
                    }else{
                        //Compte non-connecté
                        //TODO remove sysout
                        System.out.println("Non-connecté en tant que '"+username+"'");

                        firerer.fireUserAccountDisconnectedEvent(username);
                        firerer.fireStartupProcedureFinishedEvent(false);
                    }
                }

                @Override
                public void onAccountStatusNotChangedEvent(Throwable cause) {
                    StartupProcedureHandler.this.removeConnectAccountListener();
                    firerer.fireStartupProcedureFinishedEvent(false);
                }
            };
        }
        return connectAccountListener;
    }

    private void removeConnectAccountListener(){
        if(connectAccountListener!=null){
            accountService.removeListener(connectAccountListener);
            connectAccountListener = null;
        }
    }

    public StartupProcedureHandler addListener(IStartupProcedureListener l){
        firerer.addListener(l);
        return this;
    }

    public StartupProcedureHandler removeListener(IStartupProcedureListener l){
        firerer.removeListener(l);
        return this;
    }
}
