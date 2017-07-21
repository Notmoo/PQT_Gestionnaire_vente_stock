package com.pqt.client.module.stock.Listeners;


import javax.swing.event.EventListenerList;

//TODO écrire javadoc
public class SimpleStockFirerer implements IStockFirerer {

	private EventListenerList listeners;

	public SimpleStockFirerer() {
		listeners = new EventListenerList();
	}

	@Override
	public void fireGetProductListSuccessEvent() {
		for(IStockListener l : listeners.getListeners(IStockListener.class)){
			l.onGetProductListSuccessEvent();
		}
	}

	@Override
	public void fireGetProductListErrorEvent(Throwable cause) {
		for(IStockListener l : listeners.getListeners(IStockListener.class)){
			l.onGetProductListErrorEvent(cause);
		}
	}

	@Override
	public void fireGetProductListRefusedEvent(Throwable cause) {
		for(IStockListener l : listeners.getListeners(IStockListener.class)){
			l.onGetProductListRefusedEvent(cause);
		}
	}

	@Override
	public void fireProductListChangedEvent() {
		for(IStockListener l : listeners.getListeners(IStockListener.class)){
			l.onProductListChangedEvent();
		}
	}

	@Override
	public void fireProductListUpdateSuccessEvent(long id) {
		for(IStockListener l : listeners.getListeners(IStockListener.class)){
			l.onProductListUpdateSuccessEvent(id);
		}
	}

	@Override
	public void fireProductListUpdateErrorEvent(long id, Throwable cause) {
		for(IStockListener l : listeners.getListeners(IStockListener.class)){
			l.onProductListUpdateErrorEvent(id, cause);
		}
	}

	@Override
	public void fireProductListUpdateRefusedEvent(long id, Throwable cause) {
		for(IStockListener l : listeners.getListeners(IStockListener.class)){
			l.onProductListUpdateRefusedEvent(id, cause);
		}
	}

	@Override
	public void addListener(IStockListener listener) {
		listeners.add(IStockListener.class, listener);
	}

	@Override
	public void removeListener(IStockListener listener) {
		listeners.remove(IStockListener.class, listener);
	}
}
