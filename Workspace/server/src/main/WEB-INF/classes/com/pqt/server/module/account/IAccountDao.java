package com.pqt.server.module.account;

import com.pqt.core.entities.user_account.Account;

//TODO écrire Javadoc
//TODO créer implémentaiton
public interface IAccountDao {

	boolean isAccountConnected(Account acc);

	boolean setAccountConnected(Account acc, boolean connected);

	boolean isAccountRegistered(Account acc);
}
