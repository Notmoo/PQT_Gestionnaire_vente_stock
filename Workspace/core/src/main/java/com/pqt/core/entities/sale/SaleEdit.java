package com.pqt.core.entities.sale;

import java.util.Map;

public class SaleEdit {
    private int id;
    private Map<Long, Boolean> products;

    public SaleEdit(int id, Map<Long, Boolean> products) {
        this.id = id;
        this.products = products;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "SaleEdit{" +
                "id=" + id +
                ", products=" + products +
                '}';
    }

    public Map<Long, Boolean> getProducts() {
        return products;
    }
}
