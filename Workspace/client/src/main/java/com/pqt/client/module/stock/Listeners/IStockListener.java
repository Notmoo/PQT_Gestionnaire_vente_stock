package com.pqt.client.module.stock.Listeners;

import java.util.EventListener;

public interface IStockListener extends EventListener {

	void onGetProductListSuccessEvent();
	void onGetProductListErrorEvent(Throwable cause);
	void onGetProductListRefusedEvent(Throwable cause);

	void onProductListUpdateSuccessEvent(long id);
	void onProductListUpdateErrorEvent(long id, Throwable cause);
	void onProductListUpdateRefusedEvent(long id, Throwable cause);

	void onProductListChangedEvent();
}
