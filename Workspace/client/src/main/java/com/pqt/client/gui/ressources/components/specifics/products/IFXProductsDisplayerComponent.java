package com.pqt.client.gui.ressources.components.specifics.products;

import com.pqt.client.gui.ressources.components.generics.displayers.IFXDisplayerComponent;
import com.pqt.client.gui.ressources.components.specifics.products.listeners.IStockComponentListener;
import com.pqt.core.entities.product.Product;

import java.util.Collection;

public interface IFXProductsDisplayerComponent extends IFXDisplayerComponent<Collection<Product>, IStockComponentListener> {
}
