package com.pqt.client.gui.frames.startup_frame;

import com.pqt.client.gui.frames.startup_frame.listeners.frame.IStartupFrameModelEventFirerer;
import com.pqt.client.gui.frames.startup_frame.listeners.frame.IStartupFrameModelListener;
import com.pqt.client.gui.frames.startup_frame.listeners.frame.SimpleStartupFrameModelEventFirerer;
import com.pqt.client.gui.frames.startup_frame.listeners.procedure.IStartupProcedureListener;
import com.pqt.client.module.account.AccountService;
import com.pqt.client.module.network.NetworkService;

public class StartupFrameModel {

    private final AccountService accountService;
    private final NetworkService networkService;
    private final IStartupFrameModelEventFirerer firerer;

    private boolean startupProcessBegan;

    public StartupFrameModel(AccountService accountService, NetworkService networkService) {
        this.accountService = accountService;
        this.networkService = networkService;
        firerer = new SimpleStartupFrameModelEventFirerer();
        startupProcessBegan = false;
    }

    public void addListener(IStartupFrameModelListener ctrl) {
        firerer.addListener(ctrl);
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
                    .addListener(new IStartupProcedureListener() {
                        @Override
                        public void onServerFoundEvent(String URL, Integer Port) {

                        }

                        @Override
                        public void onUserAccountUnknownEvent(String username) {

                        }

                        @Override
                        public void onUserAccountConnectedEvent(String username) {
                            firerer.fireStartupValidated();
                        }

                        @Override
                        public void onUserAccountDisconnectedEvent(String username) {

                        }
                    })
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
