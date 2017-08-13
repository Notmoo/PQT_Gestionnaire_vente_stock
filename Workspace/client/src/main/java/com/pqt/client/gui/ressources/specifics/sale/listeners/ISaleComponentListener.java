package com.pqt.client.gui.ressources.specifics.sale.listeners;

import com.pqt.client.gui.ressources.generics.displayers.listeners.IDisplayerComponentListener;
import com.pqt.core.entities.product.Product;
import com.pqt.core.entities.sale.Sale;
import javafx.event.Event;

public interface ISaleComponentListener extends IDisplayerComponentListener<Sale> {
    void onComponentClickEvent(Event event, Product product);
}
