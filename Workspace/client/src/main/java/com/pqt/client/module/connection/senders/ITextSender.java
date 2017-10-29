package com.pqt.client.module.connection.senders;

import com.pqt.client.module.connection.listeners.IConnectionListener;

public interface ITextSender {
    void send(String url, String text, IConnectionListener listener);
}
