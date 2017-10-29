package com.pqt.client.module.connection.listeners;

import javax.swing.event.EventListenerList;
import java.util.Arrays;

public class SimpleConnectionFirerer implements IConnectionFirerer {

	private EventListenerList listenerList;

	public SimpleConnectionFirerer() {
		listenerList = new EventListenerList();
	}

	/**
	 * @see com.pqt.client.module.connection.listeners.IConnectionFirerer#fireMessageReceivedEvent(String)
	 *
	 */
    @Override
	public void fireMessageReceivedEvent(String msg) {
		Arrays.stream(listenerList.getListeners(IConnectionListener.class)).forEach(listener->listener.onMessageReceivedEvent(msg));
	}


	/**
	 * @see com.pqt.client.module.connection.listeners.IConnectionFirerer#fireConnectedEvent()
	 */
    @Override
	public void fireConnectedEvent() {
		Arrays.stream(listenerList.getListeners(IConnectionListener.class)).forEach(IConnectionListener::onConnectedEvent);
	}


	/**
	 * @see com.pqt.client.module.connection.listeners.IConnectionFirerer#fireDisconnectedEvent()
	 */
    @Override
	public void fireDisconnectedEvent() {
		Arrays.stream(listenerList.getListeners(IConnectionListener.class)).forEach(IConnectionListener::onDisconnectedEvent);
	}

    /**
     * @see com.pqt.client.module.connection.listeners.IConnectionFirerer#fireTimeOutEvent()
     */
    @Override
    public void fireTimeOutEvent() {
        Arrays.stream(listenerList.getListeners(IConnectionListener.class)).forEach(IConnectionListener::onDisconnectedEvent);
    }


    /**
	 * @see com.pqt.client.module.connection.listeners.IConnectionFirerer#addListener(com.pqt.client.module.connection.listeners.IConnectionListener)
	 */
    @Override
	public void addListener(IConnectionListener listener) {
		listenerList.add(IConnectionListener.class, listener);
	}


	/**
	 * @see com.pqt.client.module.connection.listeners.IConnectionFirerer#removeListener(com.pqt.client.module.connection.listeners.IConnectionListener)
	 */
    @Override
	public void removeListener(IConnectionListener listener) {
		listenerList.remove(IConnectionListener.class, listener);
	}

}
