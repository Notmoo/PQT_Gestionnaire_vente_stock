package com.pqt.core.entities.product;

import java.util.ArrayList;
import java.util.List;

public class LightweightProduct {
    private long id;
    private String name;
    private int amountRemaining;
    private int amountSold;
    private boolean sellable;
    private double price;
    private List<Long> componentIds;
    private Category category;

    public LightweightProduct() {
        componentIds = new ArrayList<>();
    }

    public LightweightProduct(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.amountRemaining = product.getAmountRemaining();
        this.amountSold = product.getAmountSold();
        this.sellable = product.isSellable();
        this.price = product.getPrice();
        this.category = product.getCategory();
        this.componentIds = new ArrayList<>();
        if(componentIds!=null){
            product.getComponents().stream().forEach((component -> componentIds.add(component.getId())));
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmountRemaining() {
        return amountRemaining;
    }

    public void setAmountRemaining(int amountRemaining) {
        this.amountRemaining = amountRemaining;
    }

    public int getAmountSold() {
        return amountSold;
    }

    public void setAmountSold(int amountSold) {
        this.amountSold = amountSold;
    }

    public boolean isSellable() {
        return sellable;
    }

    public void setSellable(boolean sellable) {
        this.sellable = sellable;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<Long> getComponentIds() {
        return componentIds;
    }

    public void setComponentIds(List<Long> componentIds) {
        this.componentIds = componentIds;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
