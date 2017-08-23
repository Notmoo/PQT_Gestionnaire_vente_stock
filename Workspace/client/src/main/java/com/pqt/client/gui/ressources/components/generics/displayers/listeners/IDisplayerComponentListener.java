package com.pqt.client.gui.ressources.components.generics.displayers.listeners;

import javafx.event.Event;

import java.util.EventListener;

public interface IDisplayerComponentListener<T> extends EventListener {
    void onRefreshContentRequestEvent();
    void onContentClickEvent(Event event, T eventTarget);
    void onAddContentRequestEvent();
    void onRemoveContentRequestEvent(T content);
    void onDetailContentRequestEvent(T content);
}
