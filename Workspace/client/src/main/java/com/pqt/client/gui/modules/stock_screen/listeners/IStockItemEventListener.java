package com.pqt.client.gui.modules.stock_screen.listeners;

import com.pqt.core.entities.product.Product;

public interface IStockItemEventListener {
    void onProductActivated(Product product);
}
