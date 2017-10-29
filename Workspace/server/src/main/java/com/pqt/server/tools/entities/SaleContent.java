package com.pqt.server.tools.entities;

import com.pqt.core.entities.product.Product;
import com.pqt.core.entities.sale.Sale;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SaleContent {
    private Map<Product, Integer> content;

    public SaleContent() {
        content = new HashMap<>();
    }

    public SaleContent(Sale sale){
        content = new HashMap<>(sale.getProducts());
    }

    public void addProduct(Product product, Integer amount){
        if(content.containsKey(product)){
            content.replace(product, content.get(product)+amount);
        }else{
            content.put(product, amount);
        }
    }

    public Collection<Product> getProductList(){
        return content.keySet();
    }

    public boolean contains(Product product){
        return content.containsKey(product);
    }

    public Integer getProductAmount(Product product){
        if(content.containsKey(product))
            return content.get(product);

        return null;
    }
}
