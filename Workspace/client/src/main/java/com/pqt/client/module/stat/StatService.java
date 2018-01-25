package com.pqt.client.module.stat;

import com.pqt.client.module.query.QueryExecutor;
import com.pqt.client.module.stat.listeners.IStatListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

//TODO Issue #5 : écrire javadoc
public class StatService {

    private static Logger LOGGER = LogManager.getLogger(StatService.class);

	private StatDao dao;

    public StatService(QueryExecutor executor) {
        LOGGER.info("Initialisation du service 'Stat'");
        dao = new StatDao(executor);
        LOGGER.info("Service 'Stat' initialisé");
    }

    public Map<String,String> getStats() {
		return dao.getStats();
	}

	public void refreshStats() {
        dao.refreshStats();
	}

	public void addListener(IStatListener listener) {
        dao.addListener(listener);
	}

	public void removeListener(IStatListener listener) {
        dao.removeListener(listener);
	}

    public void shutdown() {
        LOGGER.info("Fermeture du service 'Stat'");
        //Nothing to do
    }
}
