package com.pqt.client.module.sale.listeners;

import com.pqt.core.entities.sale.Sale;

public interface ISaleFirerer {

	public abstract void fireSaleValidationSuccess(long saleId);

	public abstract void fireSaleValidationError(long saleId, Throwable cause);

	public abstract void fireSaleValidationRefused(long saleId, Throwable cause);

	public abstract void addListener(ISaleListener listener);

	public abstract void removeListener(ISaleListener listener);

}
