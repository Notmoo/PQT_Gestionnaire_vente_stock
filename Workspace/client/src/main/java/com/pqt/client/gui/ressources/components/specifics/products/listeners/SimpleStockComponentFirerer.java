package com.pqt.client.gui.ressources.components.specifics.products.listeners;

import com.pqt.client.gui.ressources.components.generics.displayers.listeners.SimpleDisplayerComponentFirerer;
import com.pqt.core.entities.product.Product;

public class SimpleStockComponentFirerer extends SimpleDisplayerComponentFirerer<Product, IStockComponentListener> {

    public SimpleStockComponentFirerer() {
        super(IStockComponentListener.class);
    }
}
