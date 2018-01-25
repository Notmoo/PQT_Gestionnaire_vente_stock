package com.pqt.client.gui.modules.account_screen;

import com.pqt.client.gui.modules.account_screen.listeners.IAccountScreenModelListener;
import com.pqt.client.module.account.AccountService;
import com.pqt.client.module.account.listeners.AccountUpdateBuilder;
import com.pqt.client.module.account.listeners.IAccountListener;
import com.pqt.core.entities.user_account.Account;
import com.pqt.core.entities.user_account.AccountLevel;

import javax.swing.event.EventListenerList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;

class AccountScreenModel {

    private AccountService accountService;
    private EventListenerList listenerList;

    AccountScreenModel(AccountService accountService) {
        this.accountService = accountService;
        listenerList = new EventListenerList();
        accountService.addListener(new IAccountListener() {
            @Override
            public void onAccountStatusChangedEvent(boolean status) {

            }

            @Override
            public void onAccountStatusNotChangedEvent(Throwable cause) {

            }

            @Override
            public void onAccountListChangedEvent() {
                Arrays.stream(listenerList.getListeners(IAccountScreenModelListener.class))
                        .forEach(IAccountScreenModelListener::onAccountListChangedEvent);
            }
        });
    }

    void modifyAccount(Account oldVal, Account newVal) {
        accountService.submitAccountUpdate(new AccountUpdateBuilder().modifyAccount(oldVal,newVal));
    }

    void addAccount(Account newVal) {
        accountService.submitAccountUpdate(new AccountUpdateBuilder().addAccount(newVal));
    }

    void removeAccount(Account oldVal) {
        accountService.submitAccountUpdate(new AccountUpdateBuilder().removeAccount(oldVal));
    }

    Collection<Account> getAccountCollection() {
        return accountService.getAllAccounts();
    }

    Account getCurrentAccount() {
        return accountService.getCurrentAccount();
    }

    Collection<AccountLevel> getLevels() {
        return EnumSet.allOf(AccountLevel.class);
    }

    public void addListener(IAccountScreenModelListener l){
        listenerList.add(IAccountScreenModelListener.class, l);
    }

    public void removeListener(IAccountScreenModelListener l){
        listenerList.remove(IAccountScreenModelListener.class, l);
    }
}
