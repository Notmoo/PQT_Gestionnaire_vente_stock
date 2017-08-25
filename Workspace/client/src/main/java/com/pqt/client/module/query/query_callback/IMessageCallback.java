package com.pqt.client.module.query.query_callback;

public interface IMessageCallback {
    void err(Throwable cause);
    void ref(Throwable cause);
}
