package com.pqt.client.module.gui.ressources.components.products.listeners;

import com.pqt.core.entities.product.Product;
import javafx.event.Event;

public interface IStockComponentFirerer {

    void addListener(IStockComponentListener l);
    void removeListener(IStockComponentListener l);

    void fireProductClickEvent(Event event, Product product);
    void fireAddProductRequestEvent();
    void fireRemoveProductRequestEvent(Product product);
    void fireDetailProductRequestEvent(Product product);
    void fireRefreshProductListRequestEvent();
}
