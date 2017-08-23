package com.pqt.client.gui.modules.account_screen.account_manager_screen;

import com.pqt.core.entities.user_account.Account;
import com.pqt.core.entities.user_account.AccountLevel;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

class AccountManagerScreenModel {
    private Account currentValue;
    private Account initialValue;
    private Set<AccountLevel> levels;

    AccountManagerScreenModel(Account initialValue, Collection<AccountLevel> availableLevels) {
        levels = new HashSet<>();
        if(availableLevels!=null)
            levels.addAll(availableLevels);
        this.initialValue = initialValue;
        this.currentValue = (initialValue!=null?new Account(initialValue):getNewAccount());
    }

    Account getCurrentValue() {
        return currentValue;
    }

    Account getInitialValue() {
        return initialValue;
    }

    boolean isCurrentValueValid() {
        return !currentValue.equals(initialValue)
                && currentValue.getUsername()!=null
                && !currentValue.getUsername().isEmpty()
                && currentValue.getPermissionLevel()!=null;
    }

    Collection<AccountLevel> getAccountLevelCollection(){
        return levels;
    }

    void changeName(String username) {
        currentValue.setUsername(username);
    }

    void changePassword(String password) {
        currentValue.setPassword(password);
    }

    void changeLevel(AccountLevel level) {
        currentValue.setPermissionLevel(level);
    }

    private Account getNewAccount() {
        Account account = new Account();
        account.setUsername("");
        account.setPassword("");
        account.setPermissionLevel(levels.stream().min(Enum::compareTo).orElse(AccountLevel.getLowest()));
        return account;
    }
}
