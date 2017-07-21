package com.pqt.core.entities.query;

import com.pqt.core.entities.product.ProductUpdate;

import java.util.List;

public class UpdateQuery extends SimpleQuery {

	private List<ProductUpdate> updates;

    public UpdateQuery(List<ProductUpdate> updates) {
        super(QueryType.UPDATE);
        this.updates = updates;
    }

    public List<ProductUpdate> getUpdates() {
        return updates;
    }

    public void setUpdates(List<ProductUpdate> updates) {
        this.updates = updates;
    }
}
