package com.pqt.client.module.stat;

import com.pqt.client.module.query.QueryExecutor;
import com.pqt.client.module.stat.listeners.IStatListener;

import java.util.Map;

//TODO écrire javadoc
//TODO add log lines
public class StatService {

	private StatDao dao;

    public StatService(QueryExecutor executor) {
        dao = new StatDao(executor);
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

}
