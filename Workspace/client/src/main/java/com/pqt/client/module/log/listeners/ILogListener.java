package com.pqt.client.module.log.listeners;

import java.util.EventListener;

public interface ILogListener extends EventListener {

	public abstract void onLogEvent();

}
