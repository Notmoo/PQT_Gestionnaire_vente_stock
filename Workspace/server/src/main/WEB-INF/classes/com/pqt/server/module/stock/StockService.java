package com.pqt.server.module.stock;

import com.pqt.core.entities.product.Product;
import com.pqt.core.entities.product.ProductUpdate;
import com.pqt.server.exception.ServerQueryException;

import java.util.List;
import java.util.Map;

//TODO écrire Javadoc
//TODO ajouter logs
public class StockService {

    private IStockDao dao;

    public StockService() {
        dao = new FileStockDao();
    }

    public List<Product> getProductList() {
		return dao.getProductList();
	}

	public Product getProduct(long id) {
		return dao.getProduct(id);
	}

	public void applySale(Map<Product, Integer> productAmounts) {
		dao.applySale(productAmounts);
	}

	public void applyUpdateList(List<ProductUpdate> updates) throws ServerQueryException{
    	for(ProductUpdate upd : updates){
    		if(upd.getOldVersion()==null){
    			addProduct(upd.getNewVersion());
			}else if(upd.getNewVersion()==null){
    			removeProduct(upd.getOldVersion().getId());
			}else if(upd.getOldVersion()!=null && upd.getNewVersion()!=null){
				modifyProduct(upd.getOldVersion().getId(), upd.getNewVersion());
			}else{
			    //TODO écrit le throw d'une ServerQueryException
            }
		}
	}

	private void addProduct(Product product) {
        dao.addProduct(product);
	}

	private void removeProduct(long id) {
        dao.removeProduct(id);
	}

	private void modifyProduct(long id, Product product) {
        dao.modifyProduct(id, product);
	}
}
