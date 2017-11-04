package com.pqt.client.module.stock;

import com.pqt.client.module.query.QueryExecutor;
import com.pqt.client.module.query.query_callback.ICollectionItemMessageCallback;
import com.pqt.client.module.query.query_callback.INoItemMessageCallback;
import com.pqt.client.module.stock.Listeners.IStockFirerer;
import com.pqt.client.module.stock.Listeners.IStockListener;
import com.pqt.client.module.stock.Listeners.SimpleStockFirerer;
import com.pqt.core.entities.product.Product;
import com.pqt.core.entities.product.ProductUpdate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

//TODO écrire javadoc
public class StockDao {

    private long updateId;
	private IStockFirerer eventFirerer;
	private Date lastRefreshTimestamp;
	private List<Product> products;
	private QueryExecutor executor;

	public StockDao(QueryExecutor executor) {
	    this.executor = executor;
	    updateId = 0;
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
        executor.executeStockQuery(new ICollectionItemMessageCallback<Product>() {
			@Override
			public void ack(Collection<Product> obj) {
				replaceProductList(obj);
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

	private synchronized void replaceProductList(Collection<Product> products){
        {//TODO delete print block
            System.out.println("------------------------------------------");
            System.out.println("Stock dao's list : ");
            for(Product p : products){
                System.out.println(p);
            }
            System.out.println("------------------------------------------");
        }
		this.products.clear();
		this.products.addAll(products);
		this.lastRefreshTimestamp = new Date();
		eventFirerer.fireProductListChangedEvent();
	}

	public long commitUpdate(List<ProductUpdate> updates){
        final long currentUpdateId = updateId;
        if(updateId<Long.MAX_VALUE)
            updateId++;
        else
            updateId = 0;
        executor.executeUpdateQuery(updates, new INoItemMessageCallback() {
			@Override
			public void ack() {
				//TODO add log line
				refreshProductList();
				eventFirerer.fireProductListUpdateSuccessEvent(currentUpdateId);
			}

			@Override
			public void err(Throwable cause) {
                //TODO add log line
                eventFirerer.fireProductListUpdateErrorEvent(currentUpdateId, cause);
			}

			@Override
			public void ref(Throwable cause) {
                //TODO add log line
                eventFirerer.fireProductListUpdateRefusedEvent(currentUpdateId, cause);
			}
		});

		return currentUpdateId;
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
