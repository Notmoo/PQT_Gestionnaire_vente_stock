package com.pqt.client.module.sale;

import com.pqt.client.module.query.QueryExecutor;
import com.pqt.client.module.query.QueryFactory;
import com.pqt.client.module.query.query_callback.IIdQueryCallback;
import com.pqt.client.module.sale.listeners.ISaleFirerer;
import com.pqt.client.module.sale.listeners.ISaleListener;
import com.pqt.client.module.sale.listeners.SimpleSaleFirerer;
import com.pqt.core.entities.sale.SaleType;

import java.util.List;

//TODO écrire javadoc
//TODO add log lines
public class SaleService {

    private ISaleFirerer eventFirerer;

	public SaleService() {
	    eventFirerer = new SimpleSaleFirerer();
	}

	public SaleBuilder getNewSaleBuilder() {
        return new SaleBuilder();
	}

	public long commitSale(SaleBuilder saleBuilder) {
        return QueryExecutor.INSTANCE.execute(QueryFactory.newSaleQuery(saleBuilder.build()), new IIdQueryCallback() {
            @Override
            public void ack(long id) {
                eventFirerer.fireSaleValidationSuccess(id);
            }

            @Override
            public void err(long id, Throwable cause) {
                eventFirerer.fireSaleValidationError(id, cause);
            }

            @Override
            public void ref(long id, Throwable cause) {
                eventFirerer.fireSaleValidationRefused(id, cause);
            }
        });
	}

	/*
	TODO implémenter le revert de commande
	public void revertSale(int saleId) {

	}
	*/

	public void addListener(ISaleListener listener) {
        eventFirerer.addListener(listener);
	}

	public void removeListener(ISaleListener listener) {
        eventFirerer.removeListener(listener);
	}

    public List<SaleType> getSaleTypes() {
	    //TODO
        return null;
    }
}
