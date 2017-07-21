package com.pqt.client.module.account.listeners;

import java.util.EventListener;

public interface IAccountListener extends EventListener {

	public void onAccountStatusChanged(boolean status);

}
