package com.pqt.client.module.stat;

import com.pqt.client.module.query.QueryExecutor;
import com.pqt.client.module.query.QueryMessageFactory;
import com.pqt.client.module.query.query_callback.IMapItemMessageCallback;
import com.pqt.client.module.stat.listeners.IStatFirerer;
import com.pqt.client.module.stat.listeners.IStatListener;
import com.pqt.client.module.stat.listeners.SimpleStatFirerer;

import java.util.*;

//TODO écrire javadoc
public class StatDao {

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
            executor.executeStatQuery(new IMapItemMessageCallback<String, String>() {
                @Override
                public void err(Throwable cause) {
                    eventFirerer.fireGetStatError(cause);
                    //TODO add log line
                }

                @Override
                public void ref(Throwable cause) {
                    eventFirerer.fireGetStatRefused(cause);
                    //TODO add log line
                }

                @Override
                public void ack(Map<String, String> stats) {
                    replaceStats(stats);
                    eventFirerer.fireGetStatSuccess();
                    //TODO add log line
                }
            });
        }

        public Date getLastRefreshTimestamp(){
            return lastRefreshTimestamp;
        }

        private synchronized void replaceStats(Map<String, String> stats) {
            this.stats = new HashMap<>(stats);
            this.lastRefreshTimestamp = new Date();
        }

    public void removeListener(IStatListener listener) {
        eventFirerer.removeListener(listener);
    }

    public void addListener(IStatListener listener) {
        eventFirerer.addListener(listener);
    }
}
