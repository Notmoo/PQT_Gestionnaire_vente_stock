package com.pqt.client.module.query.query_callback;


import java.util.Map;

public interface IStatQueryCallback {
	public void ack(Map<String,String> stats);
	public void err(Throwable cause);
	public void ref(Throwable cause);
}
