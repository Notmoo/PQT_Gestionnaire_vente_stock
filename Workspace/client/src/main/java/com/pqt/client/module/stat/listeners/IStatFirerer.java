package com.pqt.client.module.stat.listeners;

import java.util.Map;

public interface IStatFirerer {

	void fireGetStatSuccess();
	void fireGetStatError(Throwable cause);
	void fireGetStatRefused(Throwable cause);
	void fireStatChangedEvent();

	void removeListener(IStatListener listener);
	void addListener(IStatListener listener);
}
