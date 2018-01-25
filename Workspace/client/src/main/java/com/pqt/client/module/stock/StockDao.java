package com.pqt.client.module.stock;

import com.pqt.client.module.query.QueryExecutor;
import com.pqt.client.module.query.query_callback.ICollectionItemMessageCallback;
import com.pqt.client.module.query.query_callback.INoItemMessageCallback;
import com.pqt.client.module.stock.Listeners.IStockFirerer;
import com.pqt.client.module.stock.Listeners.IStockListener;
import com.pqt.client.module.stock.Listeners.SimpleStockFirerer;
import com.pqt.core.entities.product.Product;
import com.pqt.core.entities.product.ProductUpdate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

//TODO Issue #5 : écrire javadoc
public class StockDao {

	private static Logger LOGGER = LogManager.getLogger(StockDao.class);

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
		LOGGER.trace("Demande de mise à jour du stock");
        executor.executeStockQuery(new ICollectionItemMessageCallback<Product>() {
			@Override
			public void ack(Collection<Product> obj) {
				replaceProductList(obj);
				eventFirerer.fireGetProductListSuccessEvent();
				LOGGER.trace("Mise à jour du stock");
			}

			@Override
			public void err(Throwable cause) {
				LOGGER.trace("Demande de mise à jour du stock");
				eventFirerer.fireGetProductListErrorEvent(cause);
			}

			@Override
			public void ref(Throwable cause) {
				LOGGER.trace("Demande de mise à jour du stock refusée : {}", cause);
				eventFirerer.fireGetProductListRefusedEvent(cause);
			}
		});
	}

	public Date getLastRefreshTimestamp(){
		return lastRefreshTimestamp;
	}

	private synchronized void replaceProductList(Collection<Product> products){
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
		LOGGER.trace("Demande de modification du stock : modification numéro {}", updateId);
        executor.executeStockUpdateQuery(updates, new INoItemMessageCallback() {
			@Override
			public void ack() {
				LOGGER.trace("Modification du stock numéro {} acceptée", updateId);
				refreshProductList();
				eventFirerer.fireProductListUpdateSuccessEvent(currentUpdateId);
			}

			@Override
			public void err(Throwable cause) {
				LOGGER.trace("Erreur durant la modification du stock numéro {} : {}", updateId, cause.getMessage());
                eventFirerer.fireProductListUpdateErrorEvent(currentUpdateId, cause);
			}

			@Override
			public void ref(Throwable cause) {
				LOGGER.trace("Modification du stock numéro {} refusée : {}", updateId, cause.getMessage());
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
