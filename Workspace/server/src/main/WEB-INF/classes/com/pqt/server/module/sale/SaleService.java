package com.pqt.server.module.sale;

import com.pqt.core.entities.sale.Sale;
import com.pqt.server.exception.ServerQueryException;
import com.pqt.server.module.sale.listeners.ISaleFirerer;
import com.pqt.server.module.sale.listeners.ISaleListener;
import com.pqt.server.module.sale.listeners.SimpleSaleFirerer;
import com.pqt.server.module.stock.StockService;

//TODO écrire Javadoc
//TODO ajouter logs
public class SaleService {

    private ISaleDao dao;
    private ISaleFirerer eventFirerer;

    public SaleService(StockService stockService) {
        dao = new NoRevertFileSaleDao(stockService);
        eventFirerer = new SimpleSaleFirerer();
    }

    public long submitSale(Sale sale) throws ServerQueryException {
        long id = dao.submitSale(sale);
        if(id!=-1) eventFirerer.fireSaleValidatedEvent(sale);
		return id;
	}

	public void submitSaleRevert(long id) throws ServerQueryException {
        dao.submitSaleRevert(id);
	}

    public void addListener(ISaleListener l) {
        eventFirerer.addListener(l);
    }

    public void removeListener(ISaleListener l){
        eventFirerer.addListener(l);
    }
}
