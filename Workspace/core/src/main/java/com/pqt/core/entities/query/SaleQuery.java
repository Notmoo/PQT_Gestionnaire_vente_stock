package com.pqt.core.entities.query;

import com.pqt.core.entities.sale.Sale;

public class SaleQuery extends SimpleQuery {

	private Sale sale;

	public SaleQuery(Sale sale) {
		super(QueryType.SALE);
		this.sale = sale;
	}

	public Sale getSale() {
		return sale;
	}

    public void setSale(Sale sale) {
        this.sale = sale;
    }
}
