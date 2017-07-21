package com.pqt.client.module.log.listeners;

public interface ILogFirerer {

	public abstract void addListener(ILogListener listener);

	public abstract void removeListener(ILogListener listener);

	public abstract void fireLogEvent();

}
