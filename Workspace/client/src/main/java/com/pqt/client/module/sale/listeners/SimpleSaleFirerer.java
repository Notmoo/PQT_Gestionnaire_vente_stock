package com.pqt.client.module.sale.listeners;

import javax.swing.event.EventListenerList;

//TODO écrire javadoc
public class SimpleSaleFirerer implements ISaleFirerer {


	private EventListenerList listeners;

	public SimpleSaleFirerer() {
		listeners = new EventListenerList();
	}

	/**
	 * @see com.pqt.client.module.sale.listeners.ISaleFirerer#fireSaleValidationSuccess(long)
	 */
	public void fireSaleValidationSuccess(long saleId) {
        for(ISaleListener l : listeners.getListeners(ISaleListener.class)){
            l.onSaleValidationSuccess(saleId);
        }
	}


	/**
	 * @see com.pqt.client.module.sale.listeners.ISaleFirerer#fireSaleValidationError(long, Throwable)
	 */
	public void fireSaleValidationError(long saleId, Throwable cause) {
        for(ISaleListener l : listeners.getListeners(ISaleListener.class)){
            l.onSaleValidationError(saleId, cause);
        }
	}


	/**
	 * @see com.pqt.client.module.sale.listeners.ISaleFirerer#fireSaleValidationRefused(long, Throwable)
	 */
	public void fireSaleValidationRefused(long saleId, Throwable cause) {
        for(ISaleListener l : listeners.getListeners(ISaleListener.class)){
            l.onSaleValidationRefused(saleId, cause);
        }
	}


	/**
	 * @see com.pqt.client.module.sale.listeners.ISaleFirerer#addListener(com.pqt.client.module.sale.listeners.ISaleListener)
	 */
	public void addListener(ISaleListener listener) {
        listeners.add(ISaleListener.class, listener);
	}


	/**
	 * @see com.pqt.client.module.sale.listeners.ISaleFirerer#removeListener(com.pqt.client.module.sale.listeners.ISaleListener)
	 */
	public void removeListener(ISaleListener listener) {
        listeners.remove(ISaleListener.class, listener);
	}

}
