package com.pqt.client.module.gui.ressources.components.products.listeners;

import com.pqt.core.entities.product.Product;
import javafx.event.Event;

import javax.swing.event.EventListenerList;
import java.util.Arrays;

public class SimpleStockComponentFirerer implements IStockComponentFirerer {

    private EventListenerList listenerList;

    public SimpleStockComponentFirerer() {
        listenerList = new EventListenerList();
    }

    @Override
    public void addListener(IStockComponentListener l) {
        listenerList.add(IStockComponentListener.class, l);
    }

    @Override
    public void removeListener(IStockComponentListener l) {
        listenerList.remove(IStockComponentListener.class, l);
    }

    @Override
    public void fireProductClickEvent(Event event, Product product) {
        Arrays.stream(listenerList.getListeners(IStockComponentListener.class)).forEach(l->l.onProductClickEvent(event, product));
    }

    @Override
    public void fireAddProductRequestEvent() {
        Arrays.stream(listenerList.getListeners(IStockComponentListener.class)).forEach(IStockComponentListener::onAddProductRequestEvent);
    }

    @Override
    public void fireRemoveProductRequestEvent(Product product) {
        Arrays.stream(listenerList.getListeners(IStockComponentListener.class)).forEach(l->l.onRemoveProductRequestEvent(product));
    }

    @Override
    public void fireDetailProductRequestEvent(Product product) {
        Arrays.stream(listenerList.getListeners(IStockComponentListener.class)).forEach(l->l.onDetailProductRequestEvent(product));
    }

    @Override
    public void fireRefreshProductListRequestEvent() {
        Arrays.stream(listenerList.getListeners(IStockComponentListener.class)).forEach(IStockComponentListener::onRefreshProductListRequestEvent);
    }
}
