package com.pqt.client.module.connection.listeners;

import java.util.EventListener;

public interface IConnectionListener extends EventListener {

	void onMessageReceivedEvent(String msg);

	void onConnectedEvent();

	void onDisconnectedEvent();

	void onTimeOutEvent();

}
