package com.pqt.client.module.stat.listeners;

import java.util.Map;

public interface IStatFirerer {

	public abstract void fireGetStatSuccess();

	public abstract void fireGetStatError(Throwable cause);

	public abstract void fireGetStatRefused(Throwable cause);

	void removeListener(IStatListener listener);

	void addListener(IStatListener listener);
}
