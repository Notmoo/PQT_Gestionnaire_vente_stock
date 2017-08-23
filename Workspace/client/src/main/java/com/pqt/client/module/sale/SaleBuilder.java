package com.pqt.client.module.sale;

import com.pqt.client.module.stock.StockDao;
import com.pqt.core.entities.members.Client;
import com.pqt.core.entities.product.Product;
import com.pqt.core.entities.sale.Sale;
import com.pqt.core.entities.sale.SaleStatus;
import com.pqt.core.entities.sale.SaleType;
import com.pqt.core.entities.user_account.Account;

import java.util.*;

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
        //Check for sufficient stock
        if(isAmountRemainingSufficient(product, 1)) {
            if(products.containsKey(product)){
                    products.replace(product, products.get(product) + 1);
            }else{
                products.put(product, 1);
            }
        }

        return this;
	}

	private boolean isAmountRemainingSufficient(Product newProduct, int requiredAmount){
        Map<Product, Integer> rank0Products = new HashMap<>();
        collectRank0Products(newProduct, requiredAmount, rank0Products);
        products.keySet().forEach(oldProduct->collectRank0Products(oldProduct, products.get(oldProduct), rank0Products));

        return rank0Products.keySet()
                .stream()
                .filter(rank0Product->rank0Product.getAmountRemaining()<rank0Products.get(rank0Product))
                .count()==0;
    }

    private void collectRank0Products(Product rootProduct, Integer rootProductAmount, Map<Product, Integer> collector){
	    if(rootProduct.getComponents().isEmpty()){
	        if(collector.containsKey(rootProduct))
	            collector.replace(rootProduct, collector.get(rootProduct)+rootProductAmount);
	        else
	            collector.put(rootProduct, rootProductAmount);
        }else{
	        rootProduct.getComponents().forEach(product->collectRank0Products(product, rootProductAmount, collector));
        }
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
