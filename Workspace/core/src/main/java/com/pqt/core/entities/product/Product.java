package com.pqt.core.entities.product;

import com.pqt.core.entities.log.ILoggable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Notmoo on 18/07/2017.
 */
public class Product implements ILoggable, Serializable{
    private int id;
    private String name;
    private int amountRemaining;
    private int amountSold;
    private boolean sellable;
    private boolean price;
    private List<Product> components;
    private Category category;

    public Product() {
        components = new ArrayList<>();
    }

    public Product(int id, String name, int amountRemaining, int amountSold, boolean sellable, boolean price, List<Product> components, Category category) {
        this.id = id;
        this.name = name;
        this.amountRemaining = amountRemaining;
        this.amountSold = amountSold;
        this.sellable = sellable;
        this.price = price;
        this.components = components;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public boolean isPrice() {
        return price;
    }

    public void setPrice(boolean price) {
        this.price = price;
    }

    public List<Product> getComponents() {
        return components;
    }

    public void setComponents(List<Product> components) {
        this.components = components;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
