package com.pqt.client.module.query.query_callback;

public interface IUpdateQueryCallback {
    public void ack(long id);
    public void err(long id, Throwable cause);
    public void ref(long id, Throwable cause);
}
