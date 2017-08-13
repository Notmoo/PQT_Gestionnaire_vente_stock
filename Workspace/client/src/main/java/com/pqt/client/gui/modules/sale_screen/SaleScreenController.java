package com.pqt.client.gui.modules.sale_screen;

import com.pqt.client.gui.modules.sale_screen.listeners.ISaleScreenModelListener;
import com.pqt.client.gui.ressources.components.sale_validation_screen.listeners.ISaleValidationScreenListener;
import com.pqt.client.gui.ressources.specifics.products.listeners.IStockComponentListener;
import com.pqt.client.gui.ressources.generics.validators.listeners.IValidatorComponentListener;
import com.pqt.client.gui.ressources.specifics.sale.listeners.ISaleComponentListener;
import com.pqt.client.gui.ressources.strings.GUIStringTool;
import com.pqt.core.entities.product.Product;
import com.pqt.core.entities.sale.Sale;
import com.pqt.core.entities.sale.SaleStatus;
import com.pqt.core.entities.sale.SaleType;
import com.pqt.core.entities.user_account.Account;
import com.pqt.core.entities.user_account.AccountLevel;
import javafx.event.Event;
import javafx.scene.control.Alert;

import java.util.List;

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
                SaleScreenController.this.onSaleValidationError(status, cause);
            }

            @Override
            public void onStockUpdatedEvent() {
                view.setProducts(model.getProductList());
            }

            @Override
            public void onAccountListUpdatedEvent() {
                view.setAccounts(model.getAccountList());
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

    void updateView(){
        view.setProducts(fetchProductList());
        view.setSaleTypes(fetchSaleTypeList());
        view.setAccounts(fetchAccountList());

        view.setSale(getCurrentSale());
    }

    private List<Product> fetchProductList(){
        return model.getProductList();
    }
    private List<Account> fetchAccountList(){
        return model.getAccountList();
    }
    private List<SaleType> fetchSaleTypeList(){
        return model.getSaleTypeList();
    }

    ISaleComponentListener getSaleDisplayerListener() {
        return new ISaleComponentListener() {
            @Override
            public void onComponentClickEvent(Event event, Product product) {
                model.removeProductFromSale(product);
                SaleScreenController.this.updateView();
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
                model.addProductToSale(eventTarget);
                SaleScreenController.this.updateView();
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
        return new Account(" - ", null, AccountLevel.getLowest());
    }

    IValidatorComponentListener getValidatorListener() {
        return new IValidatorComponentListener() {
            @Override
            public void onValidationEvent() {
                model.commitSale();
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
