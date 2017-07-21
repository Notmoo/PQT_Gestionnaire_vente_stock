package com.pqt.client.module.stat.listeners;

import java.util.EventListener;
import java.util.Map;

public interface IStatListener extends EventListener {

	public abstract void onGetStatSuccess();

	public abstract void onGetStatError(Throwable cause);

	public abstract void onGetStatRefused(Throwable cause);

}
