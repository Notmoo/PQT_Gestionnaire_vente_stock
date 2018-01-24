package com.pqt.client.module.account.listeners;

import com.pqt.core.entities.user_account.Account;
import com.pqt.core.entities.user_account.AccountUpdate;

import java.util.*;

public class AccountUpdateBuilder {

    private Set<Account> toAdd, toRemove;
    private Map<Account, Account> toModify;

    public AccountUpdateBuilder() {
        toAdd = new HashSet<>();
        toRemove = new HashSet<>();
        toModify = new HashMap<>();
    }

    public AccountUpdateBuilder addAccount(Account account) {
        if(!toAdd.contains(account)){
            toAdd.add(account);
        }
        return this;
    }

    public AccountUpdateBuilder removeAccount(Account account) {
        if(!toRemove.contains(account)){
            toRemove.add(account);
        }
        return this;
    }

    public AccountUpdateBuilder modifyAccount(Account oldVersion, Account newVersion) {
        toModify.put(oldVersion, newVersion);
        return this;
    }

    public List<AccountUpdate> build() {
        List<AccountUpdate> reply = new ArrayList<>();
        for(Account account : toAdd){
            reply.add(new AccountUpdate(null, account));
        }
        for(Account account : toRemove){
            reply.add(new AccountUpdate(account, null));
        }
        for(Account account : toModify.keySet()){
            reply.add(new AccountUpdate(account, toModify.get(account)));
        }

        return reply;
    }
}
