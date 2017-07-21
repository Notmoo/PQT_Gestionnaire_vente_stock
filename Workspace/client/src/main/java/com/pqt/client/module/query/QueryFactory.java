package com.pqt.client.module.query;

import com.pqt.core.entities.product.ProductUpdate;
import com.pqt.core.entities.query.IQuery;
import com.pqt.core.entities.sale.Sale;
import com.pqt.core.entities.user_account.Account;

import java.util.List;

//TODO écrire contenu méthodes
//TODO écrire javadoc
public class QueryFactory {

	public static IQuery newConnectQuery(String serverAddress) {
		return null;
	}

	public static IQuery newSaleQuery(Sale sale) {
		return null;
	}

	public static IQuery newStockQuery() {
		return null;
	}

	public static IQuery newStatQuery() {
		return null;
	}

	public static IQuery newLogQuery(Account account, boolean state) {
		return null;
	}

	public static IQuery newUpdateQuery(List<ProductUpdate> updates) {
		return null;
	}

}
