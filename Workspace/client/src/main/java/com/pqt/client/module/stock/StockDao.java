package com.pqt.client.module.stock;

import com.pqt.client.module.query.QueryExecutor;
import com.pqt.client.module.query.QueryFactory;
import com.pqt.client.module.query.query_callback.IStockQueryCallback;
import com.pqt.client.module.query.query_callback.IIdQueryCallback;
import com.pqt.client.module.stock.Listeners.IStockFirerer;
import com.pqt.client.module.stock.Listeners.IStockListener;
import com.pqt.client.module.stock.Listeners.SimpleStockFirerer;
import com.pqt.core.entities.product.Product;
import com.pqt.core.entities.product.ProductUpdate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

//TODO écrire javadoc
public class StockDao {

	private IStockFirerer eventFirerer;
	private Date lastRefreshTimestamp;
	private List<Product> products;

	public StockDao() {
		eventFirerer = new SimpleStockFirerer();
		products = new ArrayList<>();
		lastRefreshTimestamp = null;
	}

	public synchronized List<Product> getProducts() {
		return new ArrayList<>(products);
	}

	public synchronized Product getProduct(final int id) {
		return products.stream().filter((product->product.getId()==id)).findFirst().orElse(null);
    }

	public void refreshProductList() {
		QueryExecutor.INSTANCE.execute(QueryFactory.newStockQuery(), new IStockQueryCallback() {
			@Override
			public void ack(List<Product> products) {
				replaceProductList(products);
				eventFirerer.fireGetProductListSuccessEvent();
				//TODO add log line
			}

			@Override
			public void err(Throwable cause) {
				//TODO add log line
				eventFirerer.fireGetProductListErrorEvent(cause);
			}

			@Override
			public void ref(Throwable cause) {
				//TODO add log line
				eventFirerer.fireGetProductListRefusedEvent(cause);
			}
		});
	}

	public Date getLastRefreshTimestamp(){
		return lastRefreshTimestamp;
	}

	private synchronized void replaceProductList(List<Product> products){
		this.products.clear();
		this.products.addAll(products);
		this.lastRefreshTimestamp = new Date();
		eventFirerer.fireProductListChangedEvent();
	}

	public long commitUpdate(List<ProductUpdate> updates){
		return QueryExecutor.INSTANCE.execute(QueryFactory.newUpdateQuery(updates),new IIdQueryCallback(){

			@Override
			public void ack(long id) {
				//TODO add log line
				refreshProductList();
				eventFirerer.fireProductListUpdateSuccessEvent(id);
			}

			@Override
			public void err(long id, Throwable cause) {
				//TODO add log line
				eventFirerer.fireProductListUpdateErrorEvent(id, cause);
			}

			@Override
			public void ref(long id, Throwable cause) {
				//TODO add log line
				eventFirerer.fireProductListUpdateRefusedEvent(id, cause);
			}
		});
	}

	/**
	 * Ajoute un listener à la liste de diffusion des événements pouvant être levé par le service.
	 * @param listener
	 * @see {@link IStockListener}
	 */
	public void addListener(IStockListener listener) {
		eventFirerer.addListener(listener);
	}

	/**
	 * Retire un listener d'événement donné de la liste de diffusion des événements.
	 * @param listener
	 * @see {@link IStockListener}
	 */
	public void removeListener(IStockListener listener) {
		eventFirerer.removeListener(listener);
	}
}
