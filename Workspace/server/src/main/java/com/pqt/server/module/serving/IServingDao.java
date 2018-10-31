package com.pqt.server.module.serving;

import com.pqt.core.entities.sale.LightweightSale;
import com.pqt.core.entities.sale.Sale;
import com.pqt.core.entities.sale.SaleEdit;

import java.util.List;

public interface IServingDao {

    void addSale(Sale sale);
    void completeCommand(SaleEdit saleEdit);
    int getVersion();

    List<LightweightSale> getSaleList();

}
