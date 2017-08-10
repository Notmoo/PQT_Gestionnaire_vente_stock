package com.pqt.client.module.gui.ressources.specifics.products;

import com.pqt.client.module.gui.ressources.specifics.products.listeners.IStockComponentListener;
import com.pqt.client.module.gui.ressources.generics.displayers.IFXDisplayerComponent;
import com.pqt.core.entities.product.Product;

import java.util.Collection;

public interface IFXProductsDisplayerComponent extends IFXDisplayerComponent<Collection<Product>, IStockComponentListener> {
}
