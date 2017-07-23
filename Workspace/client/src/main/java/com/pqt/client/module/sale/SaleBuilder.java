package com.pqt.client.module.sale;

import com.pqt.core.entities.members.Client;
import com.pqt.core.entities.product.Product;
import com.pqt.core.entities.sale.Sale;
import com.pqt.core.entities.sale.SaleStatus;
import com.pqt.core.entities.sale.SaleType;
import com.pqt.core.entities.user_account.Account;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//TODO écrire javadoc
public class SaleBuilder {

	private Map<Product, Integer> products;
	private Account orderedFor, orderedBy;
	private Client orderedWith;
	private SaleType type;

    public SaleBuilder() {
        this.products = new HashMap<>();
        orderedFor = null;
        orderedBy = null;
    }

    public SaleBuilder addProduct(Product product) {
		if(products.containsKey(product)){
		    //Check for sufficient stock
		    if(product.getAmountRemaining()<products.get(product)) {
                products.replace(product, products.get(product) + 1);
            }
        }else{
		    products.put(product, 1);
        }

        return this;
	}

	public SaleBuilder removeProduct(Product product) {
        if(products.containsKey(product)) {
            if (products.get(product) == 1)
                products.remove(product);
            else
                products.replace(product, products.get(product) - 1);
        }

        return this;
	}

	public SaleBuilder orderedFor(Account account){
        orderedFor = account;
        return this;
    }

    public SaleBuilder orderedBy(Account account){
        orderedBy = account;
        return this;
    }

    public SaleBuilder orderedWith(Client client){
        this.orderedWith = client;
        return this;
    }

    public SaleBuilder saleType(SaleType type){
        this.type = type;
        return this;
    }

	public Sale build() {
        return new Sale(0, products, new Date(), orderedWith, orderedBy, orderedFor, type, SaleStatus.PENDING);
	}

}
