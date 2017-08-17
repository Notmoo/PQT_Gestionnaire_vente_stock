package com.pqt.client.gui.modules.stock_screen.product_manager_screen;

import com.pqt.client.module.stock.StockService;
import com.pqt.core.entities.product.Product;

public final class ProductManagerScreenFactory {

    private StockService stockService;

    public ProductManagerScreenFactory(StockService stockService) {
        this.stockService = stockService;
    }

    public ProductManagerScreen create(Product product){
        return new ProductManagerScreen(product, stockService);
    }
}
