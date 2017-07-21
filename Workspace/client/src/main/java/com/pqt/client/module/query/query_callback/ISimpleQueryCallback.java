package com.pqt.client.module.query.query_callback;

public interface ISimpleQueryCallback {
	public void ack();
	public void err(Throwable cause);
	public void ref(Throwable cause);
}
