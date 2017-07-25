package com.pqt.server.module.sale;

import com.pqt.core.entities.sale.Sale;

//TODO écrire Javadoc
//TODO ajouter logs
public class SaleService {

    private ISaleDao dao;

    public SaleService() {
    }

    public long submitSale(Sale sale) {
		return dao.submitSale(sale);
	}

	public void submitSaleRevert(long id) {
        dao.submitSaleRevert(id);
	}

}
