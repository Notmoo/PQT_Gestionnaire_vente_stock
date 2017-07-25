package com.pqt.server.module.account;

import com.pqt.core.entities.user_account.Account;

//TODO écrire Javadoc
//TODO ajouter logs
public class AccountService {

	private IAccountDao dao;

    public AccountService() {
        //TODO add dao instanciation
    }

    public boolean isAccountConnected(Account acc) {
		return dao.isAccountConnected(acc);
	}

	public boolean setAccountConnected(Account acc, boolean connected) {
		return dao.setAccountConnected(acc, connected);
	}

}
