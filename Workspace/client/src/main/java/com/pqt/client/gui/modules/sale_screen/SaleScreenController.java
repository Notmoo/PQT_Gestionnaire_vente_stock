package com.pqt.client.gui.modules.sale_screen;

import com.pqt.client.gui.modules.sale_screen.listeners.ISaleScreenModelListener;
import com.pqt.client.gui.modules.sale_screen.sale_validation_screen.listeners.ISaleValidationScreenListener;
import com.pqt.client.gui.ressources.components.specifics.products.listeners.IStockComponentListener;
import com.pqt.client.gui.ressources.components.generics.validators.listeners.IValidatorComponentListener;
import com.pqt.client.gui.ressources.components.specifics.sale.listeners.ISaleComponentListener;
import com.pqt.core.entities.product.Product;
import com.pqt.core.entities.sale.Sale;
import com.pqt.core.entities.sale.SaleStatus;
import com.pqt.core.entities.sale.SaleType;
import com.pqt.core.entities.user_account.Account;
import com.pqt.core.entities.user_account.AccountLevel;
import javafx.event.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class SaleScreenController {

    private SaleScreenModel model;
    private SaleScreenView view;

    SaleScreenController(SaleScreenModel model) {
        this.model = model;
        this.model.addListener(new ISaleScreenModelListener() {
            @Override
            public void onSaleValidatedEvent() {
                SaleScreenController.this.onSaleValidationSuccess();
            }

            @Override
            public void onSaleNotValidatedEvent(SaleStatus status, Throwable cause) {
                onSaleValidationError(status, cause);
            }

            @Override
            public void onStockUpdatedEvent() {
                view.setProducts(model.getProductList());
            }

            @Override
            public void onAccountConnectedStateUpdatedEvent() {
                updateActionLock();
            }

            @Override
            public void onAccountListUpdatedEvent() {
                view.setAccounts(new ArrayList<>(model.getAccountList()));
            }


        });
    }

    private void onSaleValidationSuccess() {
        view.setSaleStatus(SaleStatus.ACCEPTED);
    }

    private void onSaleValidationError(SaleStatus status, Throwable cause) {
        view.setSaleStatus(status);
    }

    void setView(SaleScreenView view) {
        this.view = view;
    }

    void onAccountSelectedAsBeneficiary(Account account){
        model.setSaleBeneficiary(account);
    }

    private Sale getCurrentSale(){
        return model.getCurrentSale();
    }

    private void updateSale(){
        view.setSale(getCurrentSale());
        updateActionLock();
    }

    private void updateActionLock() {
        boolean validationButtonEnabled = model.checkValidity(getCurrentSale())
                && model.isCurrentAccountConnected()
                && model.getCurrentAccountLevel().compareTo(AccountLevel.WAITER)>=0;
        view.setValidationButtonEnabled(validationButtonEnabled);
    }

    private void updateData(){
        view.setProducts(fetchProductList());
        view.setSaleTypes(fetchSaleTypeList());
        view.setAccounts(fetchAccountList());
    }

    void updateView(){
        updateData();
        updateSale();
    }

    private List<Product> fetchProductList(){
        return model.getProductList();
    }
    private List<Account> fetchAccountList(){
        return new ArrayList<>(model.getAccountList());
    }
    private List<SaleType> fetchSaleTypeList(){
        return model.getSaleTypeList();
    }

    ISaleComponentListener getSaleDisplayerListener() {
        return new ISaleComponentListener() {
            @Override
            public void onComponentClickEvent(Event event, Product product) {
                model.removeProductFromSale(product);
                SaleScreenController.this.updateSale();
            }

            @Override
            public void onRefreshContentRequestEvent() {}

            @Override
            public void onContentClickEvent(Event event, Sale eventTarget) {}

            @Override
            public void onAddContentRequestEvent() {}

            @Override
            public void onRemoveContentRequestEvent(Sale content) {}

            @Override
            public void onDetailContentRequestEvent(Sale content) {}
        };
    }

    IStockComponentListener getStockDisplayerListener() {
        return new IStockComponentListener() {
            @Override
            public void onRefreshContentRequestEvent() {}

            @Override
            public void onContentClickEvent(Event event, Product eventTarget) {
                if(eventTarget!=null) {
                    model.addProductToSale(eventTarget);
                    SaleScreenController.this.updateSale();
                }
            }

            @Override
            public void onAddContentRequestEvent() {}

            @Override
            public void onRemoveContentRequestEvent(Product content) {}

            @Override
            public void onDetailContentRequestEvent(Product content) {}
        };
    }

    Account getDefaultAccount() {
        return new Account(null, null, AccountLevel.getLowest());
    }

    IValidatorComponentListener getValidatorListener() {
        return new IValidatorComponentListener() {
            @Override
            public void onValidationEvent() {
                if(model.commitSale())
                    view.switchToSaleValidationWaitingMode(model.getTempSaleId(), model.getCurrentSale());
            }

            @Override
            public void onCancelEvent() {
                model.clearSale();
                SaleScreenController.this.updateView();
            }
        };
    }

    void onSaleTypeSelected(SaleType saleType) {
        model.setSaleType(saleType);
    }

    ISaleValidationScreenListener getSaleValidationScreenListener() {
        return saleValidatedSuccessfully -> {
            view.switchToSaleCompositionMode();
            if(saleValidatedSuccessfully){
                model.clearSale();
            }

            updateView();
        };
    }
}
