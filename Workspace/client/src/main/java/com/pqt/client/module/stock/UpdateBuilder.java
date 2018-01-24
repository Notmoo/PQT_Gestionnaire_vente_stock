package com.pqt.client.module.stock;

import com.pqt.core.entities.product.Product;
import com.pqt.core.entities.product.ProductUpdate;

import java.util.*;

public class UpdateBuilder {

    private Set<Product> toAdd, toRemove;
    private Map<Product, Product> toModify;

    public UpdateBuilder() {
        toAdd = new HashSet<>();
        toRemove = new HashSet<>();
        toModify = new HashMap<>();
    }

    public UpdateBuilder addProduct(Product product) {
        if(!toAdd.contains(product)){
            toAdd.add(product);
        }
        return this;
	}

	public UpdateBuilder removeProduct(Product product) {
        if(!toRemove.contains(product)){
            toRemove.add(product);
        }
        return this;
	}

	public UpdateBuilder modifyProduct(Product oldVersion, Product newVersion) {
        toModify.put(oldVersion, newVersion);
        return this;
	}

	public List<ProductUpdate> build() {
		List<ProductUpdate> reply = new ArrayList<>();
		for(Product product : toAdd){
		    reply.add(new ProductUpdate(null, product));
        }
        for(Product product : toRemove){
            reply.add(new ProductUpdate(product, null));
        }
        for(Product product : toModify.keySet()){
            reply.add(new ProductUpdate(product, toModify.get(product)));
        }

        return reply;
	}

}
