package com.pqt.server.module.sale;

import com.pqt.core.entities.sale.Sale;

//TODO écrire Javadoc
//TODO Créer implémentation
public interface ISaleDao {

	long submitSale(Sale sale);

	void submitSaleRevert(long id);

}
