package com.pqt.client.gui.modules.stock_screen;

import com.pqt.client.gui.modules.stock_screen.listeners.IStockScreenModelListener;
import com.pqt.client.module.stock.Listeners.StockListenerAdapter;
import com.pqt.client.module.stock.StockService;
import com.pqt.core.entities.product.Product;

import javax.swing.event.EventListenerList;
import java.util.Arrays;
import java.util.Collection;

class StockScreenModel {

    private StockService stockService;
    private EventListenerList listenerList;

    StockScreenModel(StockService stockService) {
        listenerList = new EventListenerList();
        this.stockService = stockService;
        this.stockService.addListener(new StockListenerAdapter(){
            @Override
            public void onProductListChangedEvent() {
                StockScreenModel.this.fireProductCollectionChanged();
            }
        });
    }

    private void fireProductCollectionChanged() {
        Arrays.stream(listenerList.getListeners(IStockScreenModelListener.class))
                .forEach(IStockScreenModelListener::onStockUpdatedEvent);
    }

    Collection<Product> getProductCollection() {
        return stockService.getProducts();
    }

    void commitProductDeletion(Product product) {
        stockService.commitUpdate(stockService.getNewUpdateBuilder().removeProduct(product));
    }

    void commitProductModification(Product oldProduct, Product newProduct) {
        stockService.commitUpdate(stockService.getNewUpdateBuilder().modifyProduct(oldProduct, newProduct));
    }

    void commitProductAddition(Product product) {
        stockService.commitUpdate(stockService.getNewUpdateBuilder().addProduct(product));
    }

    void addListener(IStockScreenModelListener l){
        listenerList.add(IStockScreenModelListener.class, l);
    }

    void removeListener(IStockScreenModelListener l){
        listenerList.remove(IStockScreenModelListener.class, l);
    }
}
