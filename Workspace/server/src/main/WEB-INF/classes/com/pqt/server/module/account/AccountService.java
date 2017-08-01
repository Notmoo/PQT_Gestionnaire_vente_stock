package com.pqt.server.module.account;

import com.pqt.core.entities.user_account.Account;
import com.pqt.core.entities.user_account.AccountLevel;

//TODO écrire Javadoc
//TODO ajouter logs
public class AccountService {

	private IAccountDao dao;

    public AccountService() {
		dao = new FileAccountDao();
    }

    public boolean isAccountConnected(Account account) {
		return dao.isAccountConnected(account);
	}

	public boolean submitAccountCredentials(Account acc, boolean desiredState) {
		return dao.submitAccountCredentials(acc, desiredState);
	}

	public boolean isAccountRegistered(Account account){
		return dao.isAccountRegistered(account);
	}

	public AccountLevel getAccountPermissionLevel(Account account){
		return dao.getAccountPermissionLevel(account);
	}
}
