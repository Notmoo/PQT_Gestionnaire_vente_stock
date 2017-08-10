package com.pqt.client.module.gui.ressources.specifics.sale.listeners;

import com.pqt.client.module.gui.ressources.generics.displayers.listeners.SimpleDisplayerComponentFirerer;
import com.pqt.core.entities.product.Product;
import com.pqt.core.entities.sale.Sale;
import javafx.event.Event;

import java.util.Arrays;

public class SimpleSaleComponentFirerer extends SimpleDisplayerComponentFirerer<Sale, ISaleComponentListener> {

    public SimpleSaleComponentFirerer() {
        super(ISaleComponentListener.class);
    }

    public void fireComponentClickEvent(Event event, Product product) {
        Arrays.stream(listenerList.getListeners(ISaleComponentListener.class)).forEach(l->l.onComponentClickEvent(event, product));
    }
}
