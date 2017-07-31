package com.pqt.server.module.account;

import com.pqt.core.entities.user_account.Account;

//TODO écrire Javadoc
public interface IAccountDao {

	boolean isAccountConnected(Account account);

	boolean submitAccountCredentials(Account acc, boolean desiredState);

	boolean isAccountRegistered(Account account);
}
