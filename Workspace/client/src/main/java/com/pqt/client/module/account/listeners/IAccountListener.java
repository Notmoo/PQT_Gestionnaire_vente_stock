package com.pqt.client.module.account.listeners;

import java.util.EventListener;

public interface IAccountListener extends EventListener {

	void onAccountStatusChangedEvent(boolean status);
	void onAccountStatusNotChangedEvent(Throwable cause);
	void onAccountListChangedEvent();
}
