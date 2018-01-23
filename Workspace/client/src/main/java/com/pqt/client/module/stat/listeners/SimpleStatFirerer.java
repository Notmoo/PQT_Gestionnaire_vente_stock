package com.pqt.client.module.stat.listeners;

import javax.swing.event.EventListenerList;

public class SimpleStatFirerer implements IStatFirerer {

	private EventListenerList listeners;

	public SimpleStatFirerer() {
		listeners = new EventListenerList();
	}

	/**
	 * @see com.pqt.client.module.stat.listeners.IStatFirerer#fireGetStatSuccess()
	 */
	@Override
	public void fireGetStatSuccess() {
		for(IStatListener l : listeners.getListeners(IStatListener.class)){
			l.onGetStatSuccess();
		}
	}


	/**
	 * @see com.pqt.client.module.stat.listeners.IStatFirerer#fireGetStatError(Throwable)
	 */
	@Override
	public void fireGetStatError(Throwable cause) {
		for(IStatListener l : listeners.getListeners(IStatListener.class)){
			l.onGetStatError(cause);
		}
	}


	/**
	 * @see com.pqt.client.module.stat.listeners.IStatFirerer#fireGetStatRefused(Throwable)
	 */
	@Override
	public void fireGetStatRefused(Throwable cause) {
		for(IStatListener l : listeners.getListeners(IStatListener.class)){
			l.onGetStatRefused(cause);
		}
	}

	@Override
	public void fireStatChangedEvent() {
		for(IStatListener l : listeners.getListeners(IStatListener.class)){
			l.onStatChangedEvent();
		}
	}

	@Override
	public void removeListener(IStatListener listener) {
		listeners.remove(IStatListener.class, listener);
	}

	@Override
	public void addListener(IStatListener listener) {
		listeners.add(IStatListener.class, listener);
	}

}
