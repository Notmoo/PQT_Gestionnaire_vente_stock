package com.pqt.client.module.sale.listeners;

import com.pqt.core.entities.sale.Sale;

//TODO écrire contenu méthodes
//TODO écrire javadoc
public class SaleListenerAdapter implements ISaleListener {


	/**
	 * @see com.pqt.client.module.sale.listeners.ISaleListener#onSaleValidationSuccess(com.pqt.core.entities.sale.Sale)
	 * 
	 *  
	 */
	public void onSaleValidationSuccess(Sale sale) {

	}


	/**
	 * @see com.pqt.client.module.sale.listeners.ISaleListener#onSaleValidationError(com.pqt.core.entities.sale.Sale, Throwable)
	 */
	public void onSaleValidationError(Sale sale, Throwable cause) {

	}


	/**
	 * @see com.pqt.client.module.sale.listeners.ISaleListener#onSaleValidationRefused(com.pqt.core.entities.sale.Sale, Throwable)
	 */
	public void onSaleValidationRefused(Sale sale, Throwable cause) {

	}

}
