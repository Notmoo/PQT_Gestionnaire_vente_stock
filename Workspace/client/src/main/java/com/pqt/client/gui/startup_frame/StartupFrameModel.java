package com.pqt.client.gui.startup_frame;

import com.pqt.client.gui.startup_frame.listeners.IStartupFrameModelListener;
import com.pqt.client.module.account.AccountService;
import com.pqt.client.module.account.listeners.AccountListenerAdapter;
import com.pqt.client.module.account.listeners.IAccountListener;
import com.pqt.client.module.network.NetworkService;
import com.pqt.client.module.network.listeners.INetworkServiceListener;

import javax.swing.event.EventListenerList;

public class StartupFrameModel {

    private final AccountService accountService;
    private final NetworkService networkService;
    private final EventListenerList listenerList;

    private boolean startupProcessBegan;

    public StartupFrameModel(AccountService accountService, NetworkService networkService) {
        this.accountService = accountService;
        this.networkService = networkService;
        this.listenerList = new EventListenerList();
        startupProcessBegan = false;
    }

    public void addListener(IStartupFrameModelListener ctrl) {
        listenerList.add(IStartupFrameModelListener.class, ctrl);
    }

    public boolean isStartupProcessRunning() {
        return startupProcessBegan;
    }

    public void beginStartupProcess(String requiredHost, String requiredPort, String username, String password) {
        if(!startupProcessBegan){
            checkParameters(requiredHost, requiredPort, username, password);
            startupProcessBegan = true;

            Integer requiredIntPort = Integer.parseInt(requiredPort);

            new StartupProcedureHandler(networkService, accountService)
                    .init(requiredHost, requiredIntPort, username, password)
                    .handle();
        }
    }

    private void checkParameters(String host, String port, String username, String password) {
        if(host==null || port == null || username == null || password == null)
            throw new NullPointerException("Null parameters are not allowed on startup");

        if(username.isEmpty() || host.isEmpty() || port.isEmpty())
            throw new IllegalArgumentException("The following parameters must be filled : host, port, username");

        if(!port.matches("^\\d+$"))
            throw new IllegalArgumentException("Given port is not a positive integer");
    }
}
