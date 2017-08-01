package com.pqt.server.module.account;

import com.pqt.core.entities.user_account.Account;
import com.pqt.core.entities.user_account.AccountLevel;

//TODO écrire Javadoc
public interface IAccountDao {

	boolean isAccountConnected(Account account);

	boolean submitAccountCredentials(Account acc, boolean desiredState);

	boolean isAccountRegistered(Account account);

    AccountLevel getAccountPermissionLevel(Account account);
}
