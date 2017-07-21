package com.pqt.client.module.sale.listeners;

import com.pqt.core.entities.sale.Sale;

//TODO écrire contenu méthodes
//TODO écrire javadoc
public class SimpleSaleFirerer implements ISaleFirerer {


	/**
	 * @see com.pqt.client.module.sale.listeners.ISaleFirerer#fireSaleValidationSuccess(com.pqt.core.entities.sale.Sale)
	 */
	public void fireSaleValidationSuccess(Sale sale) {

	}


	/**
	 * @see com.pqt.client.module.sale.listeners.ISaleFirerer#fireSaleValidationError(com.pqt.core.entities.sale.Sale, Throwable)
	 */
	public void fireSaleValidationError(Sale sale, Throwable cause) {

	}


	/**
	 * @see com.pqt.client.module.sale.listeners.ISaleFirerer#fireSaleValidationRefused(com.pqt.core.entities.sale.Sale, Throwable)
	 */
	public void fireSaleValidationRefused(Sale sale, Throwable cause) {

	}


	/**
	 * @see com.pqt.client.module.sale.listeners.ISaleFirerer#addListener(com.pqt.client.module.sale.listeners.ISaleListener)
	 */
	public void addListener(ISaleListener listener) {

	}


	/**
	 * @see com.pqt.client.module.sale.listeners.ISaleFirerer#removeListener(com.pqt.client.module.sale.listeners.ISaleListener)
	 */
	public void removeListener(ISaleListener listener) {

	}

}
