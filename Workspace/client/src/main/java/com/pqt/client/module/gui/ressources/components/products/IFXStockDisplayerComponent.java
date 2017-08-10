package com.pqt.client.module.gui.ressources.components.products;

import com.pqt.client.module.gui.ressources.components.IFXComponent;
import com.pqt.client.module.gui.ressources.components.products.listeners.IStockComponentListener;
import com.pqt.core.entities.product.Product;

import java.util.Collection;

public interface IFXStockDisplayerComponent extends IFXComponent {
    void display(Collection<Product> products);
    void addListener(IStockComponentListener l);
    void removeListener(IStockComponentListener l);
}
