package com.pqt.client.module.sale.listeners;

import com.pqt.core.entities.sale.Sale;

import java.util.EventListener;

public interface ISaleListener extends EventListener {

	public abstract void onSaleValidationSuccess(Sale sale);

	public abstract void onSaleValidationError(Sale sale, Throwable cause);

	public abstract void onSaleValidationRefused(Sale sale, Throwable cause);

}
