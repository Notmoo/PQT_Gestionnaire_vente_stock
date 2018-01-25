package com.pqt.client.module.sale;

import com.pqt.client.module.query.QueryExecutor;
import com.pqt.client.module.query.query_callback.INoItemMessageCallback;
import com.pqt.client.module.sale.listeners.ISaleFirerer;
import com.pqt.client.module.sale.listeners.ISaleListener;
import com.pqt.client.module.sale.listeners.SimpleSaleFirerer;
import com.pqt.core.entities.sale.SaleType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

//TODO Issue #5 : écrire javadoc
public class SaleService {

    private static Logger LOGGER = LogManager.getLogger(SaleService.class);

    private long saleId;
    private ISaleFirerer eventFirerer;
    private QueryExecutor executor;

	public SaleService(QueryExecutor executor) {
        LOGGER.info("Initialisation du service 'Sale'");
	    saleId = 0;
	    eventFirerer = new SimpleSaleFirerer();
	    this.executor = executor;
        LOGGER.info("Service 'Sale' initialisé");
	}

	public SaleBuilder getNewSaleBuilder() {
        return new SaleBuilder();
	}

	public long commitSale(SaleBuilder saleBuilder) {
        final long currentSaleId = saleId;
        if(saleId<Long.MAX_VALUE)
            saleId++;
        else
            saleId = 0;

        LOGGER.debug("Soumission de la commande numéro {}", saleId);
        executor.executeSaleQuery(saleBuilder.buildLightweight(), new INoItemMessageCallback() {
            @Override
            public void ack() {
                LOGGER.debug("Commande {} acceptée", saleId);
                eventFirerer.fireSaleValidationSuccess(currentSaleId);
            }

            @Override
            public void err(Throwable cause) {
                LOGGER.debug("Erreur durant la soumission de la commande {} : {}", saleId, cause.getMessage());
                eventFirerer.fireSaleValidationError(currentSaleId, cause);
            }

            @Override
            public void ref(Throwable cause) {
                LOGGER.debug("Commande {} refusée : {}", saleId, cause.getMessage());
                eventFirerer.fireSaleValidationRefused(currentSaleId, cause);
            }
        });

        return currentSaleId;
	}

	/*
	TODO implémenter le revert de commande
	public void revertSale(int saleId) {

	}
	*/

	public void addListener(ISaleListener listener) {
        eventFirerer.addListener(listener);
	}

	public void removeListener(ISaleListener listener) {
        eventFirerer.removeListener(listener);
	}

    public Collection<SaleType> getSaleTypes() {
        //TODO faire en sorte que cette liste soit donnée par le serveur

	    Set<SaleType> types = new HashSet<>();
	    types.add(SaleType.CASH);
        types.add(SaleType.OFFERED_GUEST);
        types.add(SaleType.OFFERED_STAFF_MEMBER);
        return types;
    }

    public void shutdown() {
        LOGGER.info("Fermeture du service 'Sale'");
	    //Nothing to do
    }
}
