package com.pqt.client.gui.frames.startup_frame.listeners.procedure;

import javax.swing.event.EventListenerList;
import java.util.Arrays;

public class SimpleStartupProcedureEventFirerer implements IStartupProcedureEventFirerer {

    private final EventListenerList listenerList;

    public SimpleStartupProcedureEventFirerer() {
        this.listenerList = new EventListenerList();
    }

    @Override
    public void fireServerFoundEvent(String URL, Integer port) {
        Arrays.stream(listenerList.getListeners(IStartupProcedureListener.class))
                .forEach(l->l.onServerFoundEvent(URL, port));
    }

    @Override
    public void fireUserAccountUnknownEvent(String username) {
        Arrays.stream(listenerList.getListeners(IStartupProcedureListener.class))
                .forEach(l->l.onUserAccountUnknownEvent(username));
    }

    @Override
    public void fireUserAccountConnectedEvent(String username) {
        Arrays.stream(listenerList.getListeners(IStartupProcedureListener.class))
                .forEach(l->l.onUserAccountConnectedEvent(username));
    }

    @Override
    public void fireUserAccountDisconnectedEvent(String username) {
        Arrays.stream(listenerList.getListeners(IStartupProcedureListener.class))
                .forEach(l->l.onUserAccountDisconnectedEvent(username));
    }

    @Override
    public void fireStartupProcedureFinishedEvent(boolean success) {
        Arrays.stream(listenerList.getListeners(IStartupProcedureListener.class))
                .forEach(l->l.onStartupProcedureFinishedEvent(success));
    }

    @Override
    public void addListener(IStartupProcedureListener l) {
        listenerList.add(IStartupProcedureListener.class, l);
    }

    @Override
    public void removeListener(IStartupProcedureListener l) {
        listenerList.remove(IStartupProcedureListener.class, l);
    }
}
