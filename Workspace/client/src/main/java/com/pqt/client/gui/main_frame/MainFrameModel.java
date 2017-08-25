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
                MainFrameModel.this.fireAccountStatusChangedEvent(status);
            }

            @Override
            public void onAccountStatusNotChangedEvent(Throwable cause) {

            }

            @Override
            public void onAccountListChangedEvent() {
                MainFrameModel.this.fireAccountCollectionChangedEvent();
            }
        });
    }

    private void fireAccountCollectionChangedEvent() {
        Arrays.stream(listenerList.getListeners(IMainFrameModelListener.class)).forEach(IMainFrameModelListener::onAccountCollectionChangedEvent);
    }

    private void fireAccountStatusChangedEvent(boolean status) {
        Arrays.stream(listenerList.getListeners(IMainFrameModelListener.class)).forEach(l->l.onAccountStatusChangedEvent(status));
    }

    void connectAccount(Account account) {
        accountService.setCurrentAccount(account);
        accountService.logInCurrentAccount(account.getPassword());
    }

    void disconnectCurrentAccount() {
        accountService.logOutCurrentAccount();
    }

    Collection<Account> getAccounts(){
        return accountService.getAllAccounts();
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
