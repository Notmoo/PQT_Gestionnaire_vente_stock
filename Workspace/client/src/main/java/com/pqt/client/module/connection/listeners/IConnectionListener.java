package com.pqt.client.module.connection.listeners;

import java.util.EventListener;

public interface IConnectionListener extends EventListener {

	public abstract void onMessageReceivedEvent(String msg);

	public abstract void onConnectedEvent();

	public abstract void onDisconnectedEvent();

}
