package com.pqt.client.module.network.listeners;

import java.util.EventListener;

public interface INetworkServiceListener extends EventListener {
    void onPQTPingSuccessEvent(String host, Integer port);
    void onPQTPingFailureEvent(String host, Integer port, Throwable cause);
    void onNewServerConfigData();
}
