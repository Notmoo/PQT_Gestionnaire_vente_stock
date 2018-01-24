package com.pqt.client.gui.modules.stock_screen.product_manager_screen;

import com.pqt.client.gui.ressources.components.generics.validators.listeners.IValidatorComponentListener;
import com.pqt.core.entities.product.Category;
import com.pqt.core.entities.product.Product;
import javafx.beans.value.ChangeListener;

import javax.swing.event.EventListenerList;
import java.util.Arrays;

class ProductManagerScreenController {

    private EventListenerList listenerList;
    private ProductManagerScreenModel model;
    private ProductManagerScreenView view;

    ProductManagerScreenController(ProductManagerScreenModel model) {
        listenerList = new EventListenerList();
        this.model = model;
    }

    void setView(ProductManagerScreenView view){
        this.view = view;
    }

    ChangeListener<? super Product> getProductComponentSelectionListener() {
        return (obs, oldValue, newValue)->{
            if(newValue!=null) {
                if (model.isComponent(newValue))
                    model.removeComponent(newValue);
                else
                    model.addComponent(newValue);
                updateView();
            }
        };
    }

    void updateView() {
        view.setProduct(model.getActualProductState());
        view.setCategoryCollection(model.getCategoryCollection());
        view.setProductCollection(model.getEligibleComponentList());
    }

    IValidatorComponentListener getValidatorListener() {
        return new IValidatorComponentListener() {
            @Override
            public void onValidationEvent() {
                Arrays.stream(listenerList.getListeners(IValidatorComponentListener.class)).forEach(IValidatorComponentListener::onValidationEvent);
            }

            @Override
            public void onCancelEvent() {
                Arrays.stream(listenerList.getListeners(IValidatorComponentListener.class)).forEach(IValidatorComponentListener::onCancelEvent);
            }
        };
    }

    public void addListener(IValidatorComponentListener l) {
        listenerList.add(IValidatorComponentListener.class, l);
    }

    public void removeListener(IValidatorComponentListener l) {
        listenerList.remove(IValidatorComponentListener.class, l);
    }

    boolean isProductHighlighted(Product product) {
        return model.getActualProductState().getComponents().contains(product);
    }

    void onNameChanged(String oldVal, String newVal) {
        model.changeName(newVal);
    }

    void onPriceChanged(double oldVal, double newVal) {
        model.changePrice(newVal);
    }

    void onCategoryChanged(Category oldVal, Category newVal) {
        model.changeCategory(newVal);
    }

    void onAmountRemainingChanged(int oldVal, int newVal) {
        model.changeAmountRemaining(newVal);
    }

    void onAmountSoldChanged(int oldVal, int newVal) {
        model.changeAmountSold(newVal);
    }

    void onSellableStateChanged(boolean oldVal, boolean newVal) {
        model.setSellable(newVal);
    }

    public void delete() {
        view = null;
        model = null;
        listenerList = null;
    }
}
