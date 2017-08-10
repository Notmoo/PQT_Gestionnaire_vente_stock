package com.pqt.client.module.gui.ressources.specifics.products.listeners;

import com.pqt.client.module.gui.ressources.generics.displayers.listeners.SimpleDisplayerComponentFirerer;
import com.pqt.core.entities.product.Product;

public class SimpleStockComponentFirerer extends SimpleDisplayerComponentFirerer<Product, IStockComponentListener> {

    public SimpleStockComponentFirerer() {
        super(IStockComponentListener.class);
    }
}
