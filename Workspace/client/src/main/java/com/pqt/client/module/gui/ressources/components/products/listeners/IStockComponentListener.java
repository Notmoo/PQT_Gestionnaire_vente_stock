package com.pqt.client.module.gui.ressources.components.products.listeners;

import com.pqt.core.entities.product.Product;
import javafx.event.Event;

import java.util.EventListener;

public interface IStockComponentListener extends EventListener {
    void onProductClickEvent(Event event, Product product);
    void onAddProductRequestEvent();
    void onRemoveProductRequestEvent(Product product);
    void onDetailProductRequestEvent(Product product);
    void onRefreshProductListRequestEvent();
}
