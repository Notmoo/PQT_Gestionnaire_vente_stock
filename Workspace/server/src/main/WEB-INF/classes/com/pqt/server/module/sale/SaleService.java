package com.pqt.server.module.sale;

import com.pqt.core.entities.sale.Sale;
import com.pqt.server.exception.ServerQueryException;
import com.pqt.server.module.stock.StockService;

//TODO écrire Javadoc
//TODO ajouter logs
public class SaleService {

    private ISaleDao dao;

    public SaleService(StockService stockService) {
        dao = new NoRevertFileSaleDao(stockService);
    }

    public long submitSale(Sale sale) throws ServerQueryException {
		return dao.submitSale(sale);
	}

	public void submitSaleRevert(long id) throws ServerQueryException {
        dao.submitSaleRevert(id);
	}

}
