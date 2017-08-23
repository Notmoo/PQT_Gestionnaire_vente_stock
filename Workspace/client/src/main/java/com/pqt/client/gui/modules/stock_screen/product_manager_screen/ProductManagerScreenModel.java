package com.pqt.client.gui.modules.stock_screen.product_manager_screen;

import com.pqt.client.gui.ressources.strings.GUIStringTool;
import com.pqt.client.module.stock.StockService;
import com.pqt.core.entities.product.Category;
import com.pqt.core.entities.product.LightweightProduct;
import com.pqt.core.entities.product.Product;

import java.util.*;
import java.util.stream.Collectors;

class ProductManagerScreenModel {

    private StockService stockService;
    private Product initialData;
    private Product currentData;

    ProductManagerScreenModel(Product initialData, StockService stockService) {
        this.stockService = stockService;
        this.initialData = initialData;
        if(initialData==null)
            currentData = new Product();
        else
            currentData = new Product(initialData);
    }

    List<Product> getEligibleComponentList() {
        return stockService.getProductsExcluding(currentData);
    }

    Product getActualProductState(){
        return currentData;
    }

    Product create() {
        if(isProductCreationPossible())
            return currentData;
        else
            return null;
    }

    boolean isProductCreationPossible() {
        return (initialData!=null && !currentData.equals(initialData))
                && !currentData.getName().isEmpty()
                && currentData.getCategory()!=null
                && currentData.getPrice()>=0;
    }

    void addComponent(Product product) {
        currentData.getComponents().add(product);
    }

    void removeComponent(Product product) {
        currentData.getComponents().remove(product);
    }

    void changeCategory(Category category){
        this.currentData.setCategory(category);
    }

    void changePrice(double price){
        this.currentData.setPrice(price);
    }

    void changeName(String name){
        this.currentData.setName(name);
    }

    void changeAmountRemaining(int amount){
        this.currentData.setAmountRemaining(amount);
    }

    void changeAmountSold(int amount){
        this.currentData.setAmountSold(amount);
    }

    void setSellable(boolean sellable){
        this.currentData.setSellable(sellable);
    }

    Collection<Category> getCategoryCollection() {
        return stockService.getProducts()
                .stream()
                .map(Product::getCategory)
                .distinct()
                .collect(Collectors.toList());
    }

    boolean isComponent(Product product) {
        return currentData.getComponents().contains(product);
    }

    boolean hasInitialData(){
        return initialData!=null;
    }

    Product getInitialData(){
        return initialData;
    }
}
