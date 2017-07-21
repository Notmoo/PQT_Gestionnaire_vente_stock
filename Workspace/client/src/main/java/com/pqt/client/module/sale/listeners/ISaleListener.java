package com.pqt.client.module.sale.listeners;

import com.pqt.core.entities.sale.Sale;

import java.util.EventListener;

public interface ISaleListener extends EventListener {

	public abstract void onSaleValidationSuccess(long saleId);

	public abstract void onSaleValidationError(long saleId, Throwable cause);

	public abstract void onSaleValidationRefused(long saleId, Throwable cause);

}
