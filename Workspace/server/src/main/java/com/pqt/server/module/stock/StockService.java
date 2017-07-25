package com.pqt.server.module.stock;

import com.pqt.core.entities.product.Product;

import java.util.List;

//TODO écrire Javadoc
//TODO ajouter logs
public class StockService {

    private IStockDao dao;

    public StockService() {
        dao = new HibernateStockDao();
    }

    public List<Product> getProductList() {
		return dao.getProductList();
	}

	public Product getProduct(long id) {
		return dao.getProduct(id);
	}

	public void addProduct(Product product) {
        dao.addProduct(product);
	}

	public void removeProduct(long id) {
        dao.removeProduct(id);
	}

	public void modifyProduct(long id, Product product) {
        dao.modifyProduct(id, product);
	}
}
