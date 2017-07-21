package com.pqt.client.module.stock.Listeners;

public interface IStockFirerer {
	void fireGetProductListSuccessEvent();
	void fireGetProductListErrorEvent(Throwable cause);
	void fireGetProductListRefusedEvent(Throwable cause);
	void fireProductListChangedEvent();

	void fireProductListUpdateSuccessEvent(long id);
	void fireProductListUpdateErrorEvent(long id, Throwable cause);
	void fireProductListUpdateRefusedEvent(long id, Throwable cause);

	void addListener(IStockListener listener);
	void removeListener(IStockListener listener);

}
