package com.pqt.client.module.stat;

import com.pqt.client.module.query.QueryExecutor;
import com.pqt.client.module.query.query_callback.IMapItemMessageCallback;
import com.pqt.client.module.stat.listeners.IStatFirerer;
import com.pqt.client.module.stat.listeners.IStatListener;
import com.pqt.client.module.stat.listeners.SimpleStatFirerer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

//TODO Issue #5 : écrire javadoc
public class StatDao {

    private static Logger LOGGER = LogManager.getLogger(StatDao.class);

    private Date lastRefreshTimestamp;
    private Map<String, String> stats;
    private IStatFirerer eventFirerer;
    private QueryExecutor executor;

    public StatDao(QueryExecutor executor) {
        eventFirerer = new SimpleStatFirerer();
        stats = new HashMap<>();
        lastRefreshTimestamp = null;
        this.executor = executor;
    }

    public synchronized Map<String, String> getStats() {
        return new HashMap<>(stats);
    }

    public void refreshStats() {
        LOGGER.trace("Mise à jour des statistiques");
        executor.executeStatQuery(new IMapItemMessageCallback<String, String>() {
            @Override
            public void err(Throwable cause) {
                eventFirerer.fireGetStatError(cause);
                LOGGER.trace("Erreur de mise à jour des statistiques : {}", cause);
            }

            @Override
            public void ref(Throwable cause) {
                eventFirerer.fireGetStatRefused(cause);
                LOGGER.trace("Mise à jour des statistiques refusée : {}", cause);
            }

            @Override
            public void ack(Map<String, String> stats) {
                replaceStats(stats);
                eventFirerer.fireGetStatSuccess();
                LOGGER.trace("Mise à jour des statistiques finie");
            }
        });
    }

    public Date getLastRefreshTimestamp(){
        return lastRefreshTimestamp;
    }

    private synchronized void replaceStats(Map<String, String> stats) {
        this.stats = new HashMap<>(stats);
        this.lastRefreshTimestamp = new Date();
        eventFirerer.fireStatChangedEvent();
    }

    public void removeListener(IStatListener listener) {
        eventFirerer.removeListener(listener);
    }

    public void addListener(IStatListener listener) {
        eventFirerer.addListener(listener);
    }
}
