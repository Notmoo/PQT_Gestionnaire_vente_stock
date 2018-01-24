package com.pqt.client.gui.modules.stock_screen.product_manager_screen;

import com.pqt.client.module.stock.StockService;
import com.pqt.core.entities.product.Category;
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
        return (initialData==null || !areProductsEqual(initialData, currentData))
                && !currentData.getName().isEmpty()
                && currentData.getCategory()!=null
                && currentData.getPrice()>=0;
    }

    private boolean areProductsEqual(Product p1, Product p2){

        /*
            Ces listes sont des différentiels entre les deux listes de composants des deux produits.

            Elles sont toutes les deux vides ssi les produits ont les mêmes composants.

            Si la liste diff de p1 contient un produit après le diff, c'est que ce produit n'était pas un composant de
            p2, et vice-versa.
         */

        List<Product> p1Diff = new ArrayList<>(p1.getComponents()),
                p2Diff = new ArrayList<>(p2.getComponents());
        p1Diff.removeAll(p2.getComponents());
        p2Diff.removeAll(p1.getComponents());

        return p1.getName().equals(p2.getName())
                && p1.getPrice() == p2.getPrice()
                && p1.getCategory() == p2.getCategory()
                && p1.getAmountSold() == p2.getAmountSold()
                && p1.getAmountRemaining() == p2.getAmountRemaining()
                && p1.isSellable() == p2.isSellable()
                && p1Diff.size()==0
                && p2Diff.size()==0;
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

    public void delete() {

    }
}
