package com.pqt.client.gui.ressources.components.generics.validators.listeners;

import javax.swing.event.EventListenerList;
import java.util.Arrays;

public class SimpleValidatorComponentFirerer implements IValidatorComponentFirerer {

    private EventListenerList listenerList;

    public SimpleValidatorComponentFirerer() {
        listenerList = new EventListenerList();
    }

    @Override
    public void addListener(IValidatorComponentListener l) {
        listenerList.add(IValidatorComponentListener.class, l);
    }

    @Override
    public void removeListener(IValidatorComponentListener l) {
        listenerList.remove(IValidatorComponentListener.class, l);
    }

    @Override
    public void fireValidationEvent() {
        Arrays.stream(listenerList.getListeners(IValidatorComponentListener.class)).forEach(IValidatorComponentListener::onValidationEvent);
    }

    @Override
    public void fireCancelEvent() {
        Arrays.stream(listenerList.getListeners(IValidatorComponentListener.class)).forEach(IValidatorComponentListener::onCancelEvent);
    }
}
