package com.pqt.client.module.stat.listeners;

import java.util.EventListener;
import java.util.Map;

public interface IStatListener extends EventListener {

	void onGetStatSuccess();

	void onGetStatError(Throwable cause);

	void onGetStatRefused(Throwable cause);

	void onStatChangedEvent();

}
