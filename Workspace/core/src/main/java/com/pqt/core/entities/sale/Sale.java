package com.pqt.core.entities.sale;

import com.pqt.core.entities.log.ILoggable;
import com.pqt.core.entities.client.Client;
import com.pqt.core.entities.product.Product;
import com.pqt.core.entities.user_account.Account;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * Created by Notmoo on 18/07/2017.
 */
public class Sale implements ILoggable, Serializable{

    private int id;
    private Map<Product, Integer> products;
    private Date orderedAt;
    private Client orderedWith;
    private Account orderedBy;
    private Account orderedFor;
    private SaleType type;
    private SaleStatus status;

    public Sale() {
    }

    public Sale(int id, Map<Product, Integer> products, Date orderedAt, Client orderedWith, Account orderedBy, Account orderedFor, SaleType type, SaleStatus status) {
        this.id = id;
        this.products = products;
        this.orderedAt = orderedAt;
        this.orderedWith = orderedWith;
        this.orderedBy = orderedBy;
        this.orderedFor = orderedFor;
        this.type = type;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Map<Product, Integer> getProducts() {
        return products;
    }

    public void setProducts(Map<Product, Integer> products) {
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
