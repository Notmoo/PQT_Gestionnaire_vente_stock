package com.pqt.client.gui.modules.stock_screen;

import com.pqt.client.gui.modules.stock_screen.listeners.IStockScreenModelListener;
import com.pqt.client.module.account.AccountService;
import com.pqt.client.module.account.listeners.IAccountListener;
import com.pqt.client.module.stock.Listeners.StockListenerAdapter;
import com.pqt.client.module.stock.StockService;
import com.pqt.core.entities.product.Product;
import com.pqt.core.entities.user_account.AccountLevel;

import javax.swing.event.EventListenerList;
import java.util.Arrays;
import java.util.Collection;

class StockScreenModel {

    private StockService stockService;
    private AccountService accountService;
    private EventListenerList listenerList;

    StockScreenModel(StockService stockService, AccountService accountService) {
        listenerList = new EventListenerList();
        this.stockService = stockService;
        this.stockService.addListener(new StockListenerAdapter(){
            @Override
            public void onProductListChangedEvent() {
                System.out.println("Product list changed event");
                StockScreenModel.this.fireProductCollectionChanged();
            }
        });
        this.accountService = accountService;
        this.accountService.addListener(new IAccountListener() {
            @Override
            public void onAccountStatusChangedEvent(boolean status) {
                StockScreenModel.this.fireConnectedStatusChanged();
            }

            @Override
            public void onAccountStatusNotChangedEvent(Throwable cause) {

            }

            @Override
            public void onAccountListChangedEvent() {

            }
        });
    }

    private void fireConnectedStatusChanged() {
        Arrays.stream(listenerList.getListeners(IStockScreenModelListener.class))
                .forEach(IStockScreenModelListener::onAcccountConnectedStatusUpdatedEvent);
    }

    private void fireProductCollectionChanged() {
        Arrays.stream(listenerList.getListeners(IStockScreenModelListener.class))
                .forEach(IStockScreenModelListener::onStockUpdatedEvent);
    }

    Collection<Product> getProductCollection() {
        {//TODO delete print block
            System.out.println("------------------------------------------");
            System.out.println("Stock service's list : ");
            for(Product p : stockService.getProducts()){
                System.out.println(p);
            }
            System.out.println("------------------------------------------");
        }
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

    boolean isAccountConnected() {
        return accountService.isCurrentAccountLoggedIn();
    }

    AccountLevel getConnectedAccountLevel() {
        if(accountService.getCurrentAccount()!=null)
            return accountService.getCurrentAccount().getPermissionLevel();
        else
            return AccountLevel.getLowest();
    }
}
