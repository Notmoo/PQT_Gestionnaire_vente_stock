package com.pqt.client.module.sale.listeners;

import com.pqt.core.entities.sale.Sale;

public interface ISaleFirerer {

	public abstract void fireSaleValidationSuccess(Sale sale);

	public abstract void fireSaleValidationError(Sale sale, Throwable cause);

	public abstract void fireSaleValidationRefused(Sale sale, Throwable cause);

	public abstract void addListener(ISaleListener listener);

	public abstract void removeListener(ISaleListener listener);

}
