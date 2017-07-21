package com.pqt.client.module.stock.Listeners;

public class StockListenerAdapter implements IStockListener {

	@Override
	public void onGetProductListSuccessEvent() {

	}

	@Override
	public void onGetProductListErrorEvent(Throwable cause) {

	}

	@Override
	public void onGetProductListRefusedEvent(Throwable cause) {

	}

	@Override
	public void onProductListUpdateSuccessEvent(long id) {

	}

	@Override
	public void onProductListUpdateErrorEvent(long id, Throwable cause) {

	}

	@Override
	public void onProductListUpdateRefusedEvent(long id, Throwable cause) {

	}

	@Override
	public void onProductListChangedEvent() {

	}
}
