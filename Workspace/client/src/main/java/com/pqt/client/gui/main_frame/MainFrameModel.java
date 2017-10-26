package com.pqt.client.gui.main_frame;

import com.pqt.client.gui.main_frame.listeners.IMainFrameModelListener;
import com.pqt.client.module.account.AccountService;
import com.pqt.client.module.account.listeners.IAccountListener;
import com.pqt.core.entities.user_account.Account;

import javax.swing.event.EventListenerList;
import java.util.Arrays;
import java.util.Collection;

class MainFrameModel {

    private EventListenerList listenerList;
    private AccountService accountService;

    MainFrameModel(AccountService accountService) {
        listenerList = new EventListenerList();
        this.accountService = accountService;
        this.accountService.addListener(new IAccountListener() {
            @Override
            public void onAccountStatusChangedEvent(boolean status) {
                if(!status){
                    MainFrameModel.this.fireAccountDisconnectedEvent();
                }
            }

            @Override
            public void onAccountStatusNotChangedEvent(Throwable cause) {

            }

            @Override
            public void onAccountListChangedEvent() {
            }
        });
    }

    private void fireAccountDisconnectedEvent() {
        Arrays.stream(listenerList.getListeners(IMainFrameModelListener.class)).forEach(IMainFrameModelListener::onAccountDisconnectedEvent);
    }

    void disconnectCurrentAccount() {
        fireAccountDisconnectedEvent();
        //TODO uncomment code when test are to be done
        /*
        accountService.logOutCurrentAccount();
        */
    }

    void addListener(IMainFrameModelListener listener){
        listenerList.add(IMainFrameModelListener.class, listener);
    }

    void removeListener(IMainFrameModelListener listener){
        listenerList.remove(IMainFrameModelListener.class, listener);
    }

    Account getCurrentAccount() {
        return accountService.getCurrentAccount();
    }
}
