package com.pqt.client.module.sale;

import com.pqt.client.module.query.QueryExecutor;
import com.pqt.client.module.query.query_callback.INoItemMessageCallback;
import com.pqt.client.module.sale.listeners.ISaleFirerer;
import com.pqt.client.module.sale.listeners.ISaleListener;
import com.pqt.client.module.sale.listeners.SimpleSaleFirerer;
import com.pqt.core.entities.sale.SaleType;

import java.util.List;

//TODO écrire javadoc
//TODO add log lines
public class SaleService {

    private long saleId;
    private ISaleFirerer eventFirerer;
    private QueryExecutor executor;

	public SaleService(QueryExecutor executor) {
	    saleId = 0;
	    eventFirerer = new SimpleSaleFirerer();
	    this.executor = executor;
	}

	public SaleBuilder getNewSaleBuilder() {
        return new SaleBuilder();
	}

	public long commitSale(SaleBuilder saleBuilder) {
        final long currentSaleId = saleId;
        if(saleId<Long.MAX_VALUE)
            saleId++;
        else
            saleId = 0;

        executor.executeSaleQuery(saleBuilder.build(), new INoItemMessageCallback() {
            @Override
            public void ack() {
                eventFirerer.fireSaleValidationSuccess(currentSaleId);
            }

            @Override
            public void err(Throwable cause) {
                eventFirerer.fireSaleValidationError(currentSaleId, cause);
            }

            @Override
            public void ref(Throwable cause) {
                eventFirerer.fireSaleValidationRefused(currentSaleId, cause);
            }
        });

        return currentSaleId;
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
