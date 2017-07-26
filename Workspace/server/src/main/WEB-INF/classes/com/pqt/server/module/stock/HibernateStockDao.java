package com.pqt.server.module.stock;

import com.pqt.core.entities.product.Product;

import java.util.List;

//TODO écrire contenu méthodes
public class HibernateStockDao implements IStockDao {


	/**
	 * @see com.pqt.server.module.stock.IStockDao#getProductList()
	 */
	public List<Product> getProductList() {
		return null;
	}


	/**
	 * @see com.pqt.server.module.stock.IStockDao#getProduct(long)
	 */
	public Product getProduct(long id) {
		return null;
	}


	/**
	 * @see com.pqt.server.module.stock.IStockDao#addProduct(com.pqt.core.entities.product.Product)
	 */
	public void addProduct(Product product) {

	}


	/**
	 * @see com.pqt.server.module.stock.IStockDao#removeProduct(long)
	 */
	public void removeProduct(long id) {

	}


	/**
	 * @see com.pqt.server.module.stock.IStockDao#modifyProduct(long, com.pqt.core.entities.product.Product)
	 */
	public void modifyProduct(long id, Product product) {

	}

}
