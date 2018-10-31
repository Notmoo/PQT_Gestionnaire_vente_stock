package com.pqt.server.module.serving;

import com.pqt.core.entities.product.Product;
import com.pqt.core.entities.sale.LightweightSale;
import com.pqt.core.entities.sale.Sale;
import com.pqt.core.entities.sale.SaleEdit;
import com.pqt.core.entities.sale.SaleStatus;

import java.util.*;

public class ServingDao implements IServingDao {

    private List<Sale> saleList;
    private int version;


    public ServingDao() {
        saleList = new LinkedList<>();
        version = 0;
    }

    @Override
    public void addSale(Sale sale) {
        sale.setStatus(SaleStatus.SERVICE_PENDING);
        saleList.add(sale);
        version++;
    }

    @Override
    public void completeCommand(SaleEdit saleEdit) {
        Sale selected = saleList.stream().filter(sale -> sale.getId() == saleEdit.getId()).findAny().orElse(null);
        if (selected != null){
            Set<Long> ids = saleEdit.getProducts().keySet();
            Map<Product, Boolean> map = selected.getServing();
            for (Product prod: map.keySet()){
                System.out.println(prod);
                if (ids.contains(prod.getId())){
                    System.out.println("found");
                    map.put(prod, saleEdit.getProducts().get(prod.getId()));
                }
            }
            if (selected.isAllServed())
                saleList.remove(selected);
            version++;
        }
    }

    @Override
    public int getVersion() {
        return version;
    }

    @Override
    public List<LightweightSale> getSaleList() {
        List<LightweightSale> lwSaleList = new ArrayList<>();
        for (Sale sale: saleList){
            lwSaleList.add(new LightweightSale(sale));
        }
        return lwSaleList;
    }
}
