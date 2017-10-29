package com.pqt.client.gui.modules.sale_screen;

import com.pqt.client.gui.modules.sale_screen.listeners.ISaleScreenModelListener;
import com.pqt.client.module.account.AccountService;
import com.pqt.client.module.account.listeners.IAccountListener;
import com.pqt.client.module.sale.SaleBuilder;
import com.pqt.client.module.sale.SaleService;
import com.pqt.client.module.sale.listeners.ISaleListener;
import com.pqt.client.module.stock.Listeners.IStockListener;
import com.pqt.client.module.stock.StockService;
import com.pqt.core.entities.product.Product;
import com.pqt.core.entities.sale.Sale;
import com.pqt.core.entities.sale.SaleStatus;
import com.pqt.core.entities.sale.SaleType;
import com.pqt.core.entities.user_account.Account;
import com.pqt.core.entities.user_account.AccountLevel;

import javax.swing.event.EventListenerList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

class SaleScreenModel {

    private EventListenerList listeners;

    private AccountService accountService;
    private StockService stockService;
    private SaleService saleService;

    private SaleBuilder currentSaleBuilder;
    private long tempSaleId;

    SaleScreenModel(AccountService accountService, StockService stockService, SaleService saleService) {
        if(accountService==null || stockService==null || saleService==null)
            throw new NullPointerException("At least one of the following services is null : account, stock, sale");

        listeners = new EventListenerList();
        this.accountService = accountService;
        this.stockService = stockService;
        this.saleService = saleService;

        saleService.addListener(new ISaleListener() {
            @Override
            public void onSaleValidationSuccess(long saleId) {
                if(saleId == SaleScreenModel.this.tempSaleId){
                    SaleScreenModel.this.fireSaleValidatedEvent();
                }
            }

            @Override
            public void onSaleValidationError(long saleId, Throwable cause) {
                if(saleId == SaleScreenModel.this.tempSaleId){
                    SaleScreenModel.this.fireSaleNotValidatedEvent(SaleStatus.ABORTED, cause);
                }
            }

            @Override
            public void onSaleValidationRefused(long saleId, Throwable cause) {
                if(saleId == SaleScreenModel.this.tempSaleId){
                    SaleScreenModel.this.fireSaleNotValidatedEvent(SaleStatus.REFUSED, cause);
                }
            }
        });
        stockService.addListener(new IStockListener() {
            @Override
            public void onGetProductListSuccessEvent() {

            }

            @Override
            public void onGetProductListErrorEvent(Throwable cause) {

            }

            @Override
            public void onGetProductListRefusedEvent(Throwable cause) {

            }

            @Override
            public void onProductListUpdateSuccessEvent(long id) {

            }

            @Override
            public void onProductListUpdateErrorEvent(long id, Throwable cause) {

            }

            @Override
            public void onProductListUpdateRefusedEvent(long id, Throwable cause) {

            }

            @Override
            public void onProductListChangedEvent() {
                fireStockUpdatedEvent();
            }
        });
        accountService.addListener(new IAccountListener() {
            @Override
            public void onAccountStatusChangedEvent(boolean status) {
                fireAccountConnectedStatusUpdateEvent();
            }

            @Override
            public void onAccountStatusNotChangedEvent(Throwable cause) {

            }

            @Override
            public void onAccountListChangedEvent() {
                fireAccountListUpdatedEvent();
            }
        });

        clearSale();
    }

    private void fireSaleValidatedEvent() {
        Arrays.stream(listeners.getListeners(ISaleScreenModelListener.class))
                .forEach(ISaleScreenModelListener::onSaleValidatedEvent);
    }

    private void fireSaleNotValidatedEvent(SaleStatus status, Throwable cause) {
        Arrays.stream(listeners.getListeners(ISaleScreenModelListener.class))
                .forEach(l->l.onSaleNotValidatedEvent(status, cause));
    }

    private void fireStockUpdatedEvent(){
        Arrays.stream(listeners.getListeners(ISaleScreenModelListener.class))
                .forEach(ISaleScreenModelListener::onStockUpdatedEvent);
    }

    private void fireAccountListUpdatedEvent(){
        Arrays.stream(listeners.getListeners(ISaleScreenModelListener.class))
                .forEach(ISaleScreenModelListener::onAccountListUpdatedEvent);
    }

    private void fireAccountConnectedStatusUpdateEvent() {
        Arrays.stream(listeners.getListeners(ISaleScreenModelListener.class))
                .forEach(ISaleScreenModelListener::onAccountConnectedStateUpdatedEvent);
    }

    Collection<Account> getAccountList() {
        return accountService.getAllAccounts();
    }

    List<SaleType> getSaleTypeList() {
        return saleService.getSaleTypes();
    }

    Sale getCurrentSale() {
        return currentSaleBuilder!=null?currentSaleBuilder.build():null;
    }

    List<Product> getProductList() {
        return stockService.getProducts().stream().filter(Product::isSellable).collect(Collectors.toList());
    }

    void clearSale() {
        currentSaleBuilder = getNewSaleBuilder();
        tempSaleId = -1;
    }

    private SaleBuilder getNewSaleBuilder(){
        SaleBuilder saleBuilder = saleService.getNewSaleBuilder();
        saleBuilder.orderedBy(accountService.getCurrentAccount());
        saleBuilder.saleType(SaleType.CASH);
        return saleBuilder;
    }

    boolean commitSale() {
        if(!checkValidity(currentSaleBuilder.build()))
            return false;

        tempSaleId = saleService.commitSale(currentSaleBuilder);
        return tempSaleId!=-1;
    }

    boolean checkValidity(Sale sale) {
        return sale.getProducts().size()>0
                && sale.getOrderedBy()!=null
                && sale.getType()!=null;
    }

    long getTempSaleId(){
        return tempSaleId;
    }

    void addProductToSale(Product product) {
        if(currentSaleBuilder!=null)
            currentSaleBuilder.addProduct(product);
    }

    void removeProductFromSale(Product product) {
        if(currentSaleBuilder!=null)
            currentSaleBuilder.removeProduct(product);
    }

    void setSaleType(SaleType saleType) {
        if(currentSaleBuilder!=null)
            currentSaleBuilder.saleType(saleType);
    }

    void setSaleBeneficiary(Account saleBeneficiary) {
        if(currentSaleBuilder!=null)
            currentSaleBuilder.orderedFor(saleBeneficiary);
    }

    void addListener(ISaleScreenModelListener listener){
        listeners.add(ISaleScreenModelListener.class, listener);
    }

    void removeListener(ISaleScreenModelListener listener){
        listeners.remove(ISaleScreenModelListener.class, listener);
    }

    boolean isCurrentAccountConnected() {
        return accountService.isCurrentAccountLoggedIn();
    }

    AccountLevel getCurrentAccountLevel() {
        if(accountService.getCurrentAccount()!=null)
            return accountService.getCurrentAccount().getPermissionLevel();
        else
            return AccountLevel.getLowest();
    }
}