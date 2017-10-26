package com.pqt.client.gui.modules.account_screen;

import com.pqt.client.module.account.AccountService;
import com.pqt.core.entities.user_account.Account;
import com.pqt.core.entities.user_account.AccountLevel;

import java.util.Collection;
import java.util.EnumSet;

//TODO MAJ les méthds modif, add et remove une fois que l'accountService prend en compte les modifs
class AccountScreenModel {

    private AccountService accountService;

    AccountScreenModel(AccountService accountService) {
        this.accountService = accountService;
    }

    void modifyAccount(Account oldVal, Account newVal) {
        //accountService.submitAccountUpdate(oldVal, newVal);
    }

    void addAccount(Account newVal) {
        //accountService.submitAccountUpdate(null, newVal);
    }

    void removeAccount(Account oldVal) {
        //accountService.submitAccountUpdate(oldVal, null);
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
}
