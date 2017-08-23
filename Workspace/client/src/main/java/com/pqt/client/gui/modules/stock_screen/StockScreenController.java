package com.pqt.client.gui.modules.stock_screen;


import com.pqt.client.gui.modules.stock_screen.listeners.IStockItemEventListener;
import com.pqt.client.gui.modules.stock_screen.listeners.IStockScreenModelListener;
import com.pqt.client.gui.modules.stock_screen.product_manager_screen.ProductManagerScreen;
import com.pqt.client.gui.ressources.components.generics.validators.listeners.IValidatorComponentListener;
import com.pqt.client.gui.ressources.strings.GUIStringTool;
import com.pqt.core.entities.product.Product;
import com.pqt.core.entities.user_account.AccountLevel;

class StockScreenController implements IStockScreenModelListener{

    private StockScreenModel model;
    private StockScreenView view;

    StockScreenController(StockScreenModel model) {
        this.model = model;
    }

    void setView(StockScreenView view) {
        this.view = view;
    }

    void onAddProductRequest() {
        detailProduct(null);
    }

    void onDetailProductRequest() {
        if(view.getSelectedProduct()!=null)
            detailProduct(view.getSelectedProduct());
    }

    private void detailProduct(Product product){
        view.switchToDetailMode(product);
    }

    IValidatorComponentListener getDetailScreenValidationListener(){
        return new IValidatorComponentListener() {
            @Override
            public void onValidationEvent() {
                if(view.isDetailScreenCreationPossible()) {
                    if (view.hasDetailScreenInitialValue()){
                        modifyProduct(view.getDetailScreenInitialValue(), view.getDetailScreenCreation());
                    }else{
                        addProduct(view.getDetailScreenCreation());
                    }
                    view.switchToGeneralMode();
                }
            }

            @Override
            public void onCancelEvent() {
                view.switchToGeneralMode();
            }
        };
    }

    void onDeleteProductRequest() {
        deleteProduct(view.getSelectedProduct());
    }

    private void addProduct(Product product){
        model.commitProductAddition(product);
    }

    private void modifyProduct(Product oldProduct, Product newProduct){
        model.commitProductModification(oldProduct, newProduct);
    }

    private void deleteProduct(Product product){
        model.commitProductDeletion(product);;
    }

    void onRefreshProductsRequest() {
        refreshView();
    }

    IStockItemEventListener getProductActivationListener() {
        return this::detailProduct;
    }

    void refreshView(){
        view.display(model.getProductCollection());
    }

    @Override
    public void onStockUpdatedEvent() {
        refreshView();
    }

    @Override
    public void onAcccountConnectedStatusUpdatedEvent() {
        updateViewActionLock();
    }

    void updateViewActionLock(){
        if(model.isAccountConnected() && model.getConnectedAccountLevel().compareTo(AccountLevel.MASTER)>=0){
            view.setAddProductActionLocked(false);
            view.setEditProductActionLocked(view.getSelectedProduct()==null);
            view.setRemoveProductActionLocked(view.getSelectedProduct()==null);
        }else{
            view.setAddProductActionLocked(true);
            view.setEditProductActionLocked(true);
            view.setRemoveProductActionLocked(true);
        }
    }

    void onProductSelectedChange(){
        updateViewActionLock();
    }
}
