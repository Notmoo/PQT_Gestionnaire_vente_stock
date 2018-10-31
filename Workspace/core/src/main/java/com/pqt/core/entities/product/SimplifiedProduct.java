package com.pqt.core.entities.product;

/**
 * Cette version de produit a pour but de communiquer avec une interface en javascript
 * Le javascript ne supporte pas les long, ils sont donc remplacés ici par des String
 * On se moque également ici de la catégorie puisque c'est destiné au panneau d'affichage en cuisine
 */
public class SimplifiedProduct {
    private String id;
    private String name;

    public SimplifiedProduct() {}

    public SimplifiedProduct(LightweightProduct product){
        this.id = String.valueOf(product.getId());
        this.name = product.getName();
    }

    public SimplifiedProduct(Product product){
        this(new LightweightProduct(product));
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
