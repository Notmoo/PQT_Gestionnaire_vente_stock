package com.pqt.client.module.sale.listeners;

import com.pqt.core.entities.sale.Sale;

public class SaleListenerAdapter implements ISaleListener {


	/**
	 * @see com.pqt.client.module.sale.listeners.ISaleListener#onSaleValidationSuccess(long)
	 * 
	 *  
	 */
	public void onSaleValidationSuccess(long saleId) {

	}


	/**
	 * @see com.pqt.client.module.sale.listeners.ISaleListener#onSaleValidationError(long, Throwable)
	 */
	public void onSaleValidationError(long saleId, Throwable cause) {

	}


	/**
	 * @see com.pqt.client.module.sale.listeners.ISaleListener#onSaleValidationRefused(long, Throwable)
	 */
	public void onSaleValidationRefused(long saleId, Throwable cause) {

	}

}
