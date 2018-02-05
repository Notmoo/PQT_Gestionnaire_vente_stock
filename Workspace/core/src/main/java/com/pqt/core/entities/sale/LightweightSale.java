package com.pqt.core.entities.sale;

import com.pqt.core.entities.members.Client;
import com.pqt.core.entities.product.LightweightProduct;
import com.pqt.core.entities.product.Product;
import com.pqt.core.entities.user_account.Account;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LightweightSale {

    private int id;
    private Map<Long, Integer> products;
    private Map<Long, Boolean> serving;
    private Client orderedWith;
    private Account orderedBy;
    private Account orderedFor;
    private SaleType type;
    private SaleStatus status;
    private Double price;
    private Double worth;

    public LightweightSale(Sale sale) {
        this.id = sale.getId();
        this.orderedWith = sale.getOrderedWith();
        this.orderedBy = sale.getOrderedBy();
        this.orderedFor = sale.getOrderedFor();
        this.type = sale.getType();
        this.status = sale.getStatus();
        serving = new HashMap<>();
        products = new HashMap<>();
        for(Product product : sale.getProducts().keySet()){
            products.put(product.getId(), sale.getProducts().get(product));
            serving.put(product.getId(), sale.getServing().get(product));
        }
        price = sale.getTotalPrice();
        worth = sale.getTotalWorth();
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getWorth() {
        return worth;
    }

    public void setWorth(Double worth) {
        this.worth = worth;
    }

    public Map<Long, Boolean> getServing() {
        return serving;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, products, orderedBy, orderedFor, orderedWith, type);
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;

        if(!this.getClass().isInstance(obj))
            return false;

        LightweightSale other = LightweightSale.class.cast(obj);
        return this.id == other.id
                && Objects.equals(this.products, other.products)
                && Objects.equals(this.orderedBy, other.orderedBy)
                && Objects.equals(this.orderedFor, other.orderedFor)
                && Objects.equals(this.orderedWith, other.orderedWith)
                && Objects.equals(this.type, other.type);
    }
}
