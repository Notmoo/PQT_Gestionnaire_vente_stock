package com.pqt.core.entities.query;

import com.pqt.core.entities.user_account.Account;

public class LogQuery extends SimpleQuery {

	private Account account;

	private boolean newDesiredState;

    public LogQuery(Account account, boolean newDesiredState) {
        super(QueryType.LOG);
        this.account = account;
        this.newDesiredState = newDesiredState;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public boolean isNewDesiredState() {
        return newDesiredState;
    }

    public void setNewDesiredState(boolean newDesiredState) {
        this.newDesiredState = newDesiredState;
    }
}
