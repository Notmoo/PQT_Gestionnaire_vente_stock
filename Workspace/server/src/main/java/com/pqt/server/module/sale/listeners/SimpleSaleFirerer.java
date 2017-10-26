package com.pqt.server.module.sale.listeners;

import com.pqt.core.entities.sale.Sale;

import javax.swing.event.EventListenerList;

public class SimpleSaleFirerer implements ISaleFirerer {

    private EventListenerList listeners;

    public SimpleSaleFirerer() {
        listeners = new EventListenerList();
    }

    @Override
    public void addListener(ISaleListener l) {
        listeners.add(ISaleListener.class, l);
    }

    @Override
    public void removeListener(ISaleListener l) {
        listeners.remove(ISaleListener.class, l);
    }

    @Override
    public void fireSaleValidatedEvent(Sale sale) {
        for(ISaleListener l : listeners.getListeners(ISaleListener.class)){
            l.onSaleValidatedEvent(sale);
        }
    }
}
