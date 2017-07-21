package com.pqt.client.module.account.listeners;

public interface IAccountFirerer {

	public void fireAccountStatusChanged(boolean status);

	public abstract void addListener(IAccountListener listener);

	public abstract void removeListener(IAccountListener listener);

}
