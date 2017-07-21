package com.pqt.client.module.connection.listeners;

public interface IConnectionFirerer {

	public abstract void fireMessageReceivedEvent(String msg);

	public abstract void fireConnectedEvent();

	public abstract void fireDisconnectedEvent();

	public abstract void addListener(IConnectionListener listener);

	public abstract void removeListener(IConnectionListener listener);

}
