package com.pqt.client.gui.frames.startup_frame.listeners.frame;

import javax.swing.event.EventListenerList;
import java.util.Arrays;

public class SimpleStartupFrameModelEventFirerer implements IStartupFrameModelEventFirerer {

    private final EventListenerList listenerList;

    public SimpleStartupFrameModelEventFirerer() {
        this.listenerList = new EventListenerList();
    }

    @Override
    public void fireStartupValidated() {
        Arrays.stream(listenerList.getListeners(IStartupFrameModelListener.class))
                .forEach(IStartupFrameModelListener::onStartupValidated);
    }

    @Override
    public void fireStartupFailed() {
        Arrays.stream(listenerList.getListeners(IStartupFrameModelListener.class))
                .forEach(IStartupFrameModelListener::onStartupFailed);
    }

    @Override
    public void addListener(IStartupFrameModelListener l) {
        listenerList.add(IStartupFrameModelListener.class, l);
    }

    @Override
    public void removeListener(IStartupFrameModelListener l) {
        listenerList.remove(IStartupFrameModelListener.class, l);
    }
}
