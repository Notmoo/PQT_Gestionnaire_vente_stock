package com.pqt.client.module.query.query_callback;

import com.pqt.core.entities.product.Product;

import java.util.List;

public interface IStockQueryCallback {
	public void ack(List<Product> products);
	public void err(Throwable cause);
	public void ref(Throwable cause);
}
