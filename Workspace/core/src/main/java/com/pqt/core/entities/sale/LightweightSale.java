package com.pqt.core.entities.sale;

import com.pqt.core.entities.members.Client;
import com.pqt.core.entities.product.Product;
import com.pqt.core.entities.user_account.Account;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LightweightSale {

    private int id;
    private Map<Long, Integer> products;
    private Date orderedAt;
    private Client orderedWith;
    private Account orderedBy;
    private Account orderedFor;
    private SaleType type;
    private SaleStatus status;

    public LightweightSale() {
    }

    public LightweightSale(Sale sale) {
        this.id = sale.getId();
        this.orderedAt = sale.getOrderedAt();
        this.orderedWith = sale.getOrderedWith();
        this.orderedBy = sale.getOrderedBy();
        this.orderedFor = sale.getOrderedFor();
        this.type = sale.getType();
        this.status = sale.getStatus();
        products = new HashMap<>();
        for(Product product : sale.getProducts().keySet()){
            products.put(product.getId(), sale.getProducts().get(product));
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Map<Long, Integer> getProducts() {
        return products;
    }

    public void setProducts(Map<Long, Integer> products) {
        this.products = products;
    }

    public Date getOrderedAt() {
        return orderedAt;
    }

    public void setOrderedAt(Date orderedAt) {
        this.orderedAt = orderedAt;
    }

    public Client getOrderedWith() {
        return orderedWith;
    }

    public void setOrderedWith(Client orderedWith) {
        this.orderedWith = orderedWith;
    }

    public Account getOrderedBy() {
        return orderedBy;
    }

    public void setOrderedBy(Account orderedBy) {
        this.orderedBy = orderedBy;
    }

    public Account getOrderedFor() {
        return orderedFor;
    }

    public void setOrderedFor(Account orderedFor) {
        this.orderedFor = orderedFor;
    }

    public SaleType getType() {
        return type;
    }

    public void setType(SaleType type) {
        this.type = type;
    }

    public SaleStatus getStatus() {
        return status;
    }

    public void setStatus(SaleStatus status) {
        this.status = status;
    }
}
