package com.pqt.client.gui.modules.stock_screen.product_manager_screen;

import com.pqt.client.gui.ressources.components.generics.creators.IFXCreatorComponent;
import com.pqt.client.gui.ressources.components.generics.validators.IFXValidatorComponent;
import com.pqt.client.gui.ressources.components.generics.validators.listeners.IValidatorComponentListener;
import com.pqt.client.module.stock.StockService;
import com.pqt.core.entities.product.Product;
import javafx.scene.layout.Pane;

public class ProductManagerScreen implements IFXCreatorComponent<Product>, IFXValidatorComponent {

    private ProductManagerScreenView view;
    private ProductManagerScreenModel model;
    private ProductManagerScreenController ctrl;

    public ProductManagerScreen(Product initialData, StockService stockService){
        model = new ProductManagerScreenModel(initialData, stockService);
        ctrl = new ProductManagerScreenController(model);
        view = new ProductManagerScreenView(ctrl);
        ctrl.setView(view);
        ctrl.updateView();
    }

    @Override
    public Product create() {
        return model.create();
    }

    @Override
    public boolean isCreationPossible() {
        return model.isProductCreationPossible();
    }

    @Override
    public Pane getPane() {
        return view.getPane();
    }

    @Override
    public void addListener(IValidatorComponentListener l) {
        ctrl.addListener(l);
    }

    @Override
    public void removeListener(IValidatorComponentListener l) {
        ctrl.removeListener(l);
    }

    public boolean hasInitialValue(){
        return model.hasInitialData();
    }

    public Product getInitialValueSnapshot(){
        return new Product(model.getInitialData());
    }

    public void delete(){
        model.delete();
        view.delete();
        ctrl.delete();
        view = null;
        ctrl = null;
        model = null;
    }
}
