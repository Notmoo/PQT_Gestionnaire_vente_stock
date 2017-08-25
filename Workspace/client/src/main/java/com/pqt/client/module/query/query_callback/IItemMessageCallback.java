package com.pqt.client.module.query.query_callback;

public interface IItemMessageCallback<T> extends IMessageCallback {
    void ack(T obj);
}
