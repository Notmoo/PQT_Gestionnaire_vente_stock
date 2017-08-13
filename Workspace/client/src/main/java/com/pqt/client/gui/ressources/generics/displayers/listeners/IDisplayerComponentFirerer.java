package com.pqt.client.gui.ressources.generics.displayers.listeners;

import javafx.event.Event;

public interface IDisplayerComponentFirerer<T> {
    void fireRefreshContentRequestEvent();
    void fireContentClickEvent(Event event, T eventTarget);
    void fireAddContentRequestEvent();
    void fireRemoveContentRequestEvent(T content);
    void fireDetailContentRequestEvent(T content);
    void addListener(IDisplayerComponentListener<T> l);
    void removeListener(IDisplayerComponentListener<T> l);
}