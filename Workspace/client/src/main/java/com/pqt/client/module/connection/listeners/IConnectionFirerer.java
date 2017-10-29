package com.pqt.client.module.connection.listeners;

public interface IConnectionFirerer {

	void fireMessageReceivedEvent(String msg);

	void fireConnectedEvent();

	void fireDisconnectedEvent();

	void fireTimeOutEvent();

	void addListener(IConnectionListener listener);

	void removeListener(IConnectionListener listener);

}
