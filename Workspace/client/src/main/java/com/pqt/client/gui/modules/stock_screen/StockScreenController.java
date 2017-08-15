package com.pqt.client.gui.modules.stock_screen;


import com.pqt.client.gui.modules.stock_screen.listeners.IStockItemEventListener;
import com.pqt.client.gui.modules.stock_screen.listeners.IStockScreenModelListener;
import com.pqt.client.gui.ressources.strings.GUIStringTool;
import com.pqt.core.entities.product.Product;

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
        detailProduct(view.getSelectedProduct());
    }

    private void detailProduct(Product product){
        //TODO à faire
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

    private void refreshView(){
        view.display(model.getProductCollection());
    }

    @Override
    public void onStockUpdatedEvent() {
        refreshView();
    }
}
