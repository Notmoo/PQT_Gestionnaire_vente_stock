package com.pqt.client.module.sale.listeners;

import java.util.EventListener;

public interface ISaleListener extends EventListener {

	void onSaleValidationSuccess(long saleId);

	void onSaleValidationError(long saleId, Throwable cause);

	void onSaleValidationRefused(long saleId, Throwable cause);

}
