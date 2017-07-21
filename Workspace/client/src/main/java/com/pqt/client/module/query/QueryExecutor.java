package com.pqt.client.module.query;

import com.pqt.client.module.query.query_callback.IUpdateQueryCallback;
import com.pqt.core.entities.query.IQuery;
import com.pqt.client.module.query.query_callback.ISimpleQueryCallback;
import com.pqt.client.module.query.query_callback.IStatQueryCallback;
import com.pqt.client.module.query.query_callback.IStockQueryCallback;

//TODO écrire contenu méthodes
//TODO écrire javadoc
public class QueryExecutor {

	public static final QueryExecutor INSTANCE = new QueryExecutor();

	private QueryExecutor(){

	}

	public long execute(IQuery query, ISimpleQueryCallback callback) {
        return 0;
	}

	public long execute(IQuery query, IStatQueryCallback callback) {
        return 0;
	}

	public long execute(IQuery query, IStockQueryCallback callback) {
        return 0;
	}

    public long execute(IQuery query, IUpdateQueryCallback callback) {
        return 0;
    }

}
