package com.pqt.client.gui.startup_frame;

import com.pqt.client.gui.startup_frame.listeners.IStartupFrameModelListener;
import com.pqt.client.module.account.AccountService;
import com.pqt.client.module.network.NetworkService;

import javax.swing.event.EventListenerList;

public class StartupFrameModel {

    private final AccountService accountService;
    private final NetworkService networkService;
    private final EventListenerList listenerList;

    public StartupFrameModel(AccountService accountService, NetworkService networkService) {
        this.accountService = accountService;
        this.networkService = networkService;
        this.listenerList = new EventListenerList();
    }

    public void addListener(IStartupFrameModelListener ctrl) {
        listenerList.add(IStartupFrameModelListener.class, ctrl);
    }
}
