package com.pqt.client.module.gui.ressources.generics.displayers.listeners;

import javafx.event.Event;

import javax.swing.event.EventListenerList;
import java.util.Arrays;

public class SimpleDisplayerComponentFirerer<T, U extends IDisplayerComponentListener<T>> implements IDisplayerComponentFirerer<T> {

    protected EventListenerList listenerList;
    protected Class<U> clazz;

    public SimpleDisplayerComponentFirerer(Class<U> clazz) {
        listenerList = new EventListenerList();
        this.clazz = clazz;
    }

    @Override
    public void fireRefreshContentRequestEvent() {
        Arrays.stream(listenerList.getListeners(clazz)).forEach(IDisplayerComponentListener::onRefreshContentRequestEvent);
    }

    @Override
    public void fireContentClickEvent(Event event, T eventTarget) {
        Arrays.stream(listenerList.getListeners(clazz)).forEach(l->l.onContentClickEvent(event, eventTarget));
    }

    @Override
    public void fireAddContentRequestEvent() {
        Arrays.stream(listenerList.getListeners(clazz)).forEach(IDisplayerComponentListener::onAddContentRequestEvent);
    }

    @Override
    public void fireRemoveContentRequestEvent(T content) {
        Arrays.stream(listenerList.getListeners(clazz)).forEach(l->l.onRemoveContentRequestEvent(content));
    }

    @Override
    public void fireDetailContentRequestEvent(T content) {
        Arrays.stream(listenerList.getListeners(clazz)).forEach(l->l.onDetailContentRequestEvent(content));
    }

    @Override
    public void addListener(IDisplayerComponentListener<T> l) throws IllegalArgumentException {
        if(clazz.isInstance(l)){
            listenerList.add(clazz, clazz.cast(l));
        }else{
            throw new IllegalArgumentException("Listener must implement the following interface : "+clazz.getName());
        }
    }

    @Override
    public void removeListener(IDisplayerComponentListener<T> l) {
        if (clazz.isInstance(l)) {
            listenerList.remove(clazz, clazz.cast(l));
        }
    }
}
