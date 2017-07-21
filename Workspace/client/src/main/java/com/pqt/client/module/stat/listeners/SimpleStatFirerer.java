package com.pqt.client.module.stat.listeners;

import javax.swing.event.EventListenerList;

//TODO écrire javadoc
public class SimpleStatFirerer implements IStatFirerer {

	private EventListenerList listeners;

	public SimpleStatFirerer() {
		listeners = new EventListenerList();
	}

	/**
	 * @see com.pqt.client.module.stat.listeners.IStatFirerer#fireGetStatSuccess()
	 */
	public void fireGetStatSuccess() {
		for(IStatListener l : listeners.getListeners(IStatListener.class)){
			l.onGetStatSuccess();
		}
	}


	/**
	 * @see com.pqt.client.module.stat.listeners.IStatFirerer#fireGetStatError(Throwable)
	 */
	public void fireGetStatError(Throwable cause) {
		for(IStatListener l : listeners.getListeners(IStatListener.class)){
			l.onGetStatError(cause);
		}
	}


	/**
	 * @see com.pqt.client.module.stat.listeners.IStatFirerer#fireGetStatRefused(Throwable)
	 */
	public void fireGetStatRefused(Throwable cause) {
		for(IStatListener l : listeners.getListeners(IStatListener.class)){
			l.onGetStatRefused(cause);
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
