package com.pqt.client.module.account;

import com.pqt.core.entities.user_account.Account;
import com.pqt.client.module.account.listeners.IAccountListener;

import java.util.List;

//TODO écrire contenu méthodes
//TODO écrire javadoc
//TODO add log lines
public class AccountService {

	public Account getCurrentAccount() {
		return null;
	}

	public void setCurrentAccount(Account account) {

	}

	public boolean isCurrentAccountLoggedIn() {
		return false;
	}

	public void logInCurrentAccount(String password) {

	}

	public void logOutCurrentAccount() {

	}

	public List<Account> getRecentAccounts() {
		return null;
	}

	public void addListener(IAccountListener listener) {

	}

	public void removeListener(IAccountListener listener) {

	}

    public List<Account> getAllAccounts() {
        return null;
    }
}
