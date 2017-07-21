package com.pqt.core.entities.query;

public class SimpleQuery implements IQuery {

    private QueryType type;

    /**
     *
     * @param type
     * @throws NullPointerException if type is null
     */
    public SimpleQuery(QueryType type) {
        if(type==null) throw new NullPointerException("null value not allowed as query type");

        this.type = type;
    }

    public QueryType getType() {
        return type;
    }
}
