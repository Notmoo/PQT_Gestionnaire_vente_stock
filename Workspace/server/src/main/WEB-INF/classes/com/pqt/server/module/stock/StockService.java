package com.pqt.server.module.stock;

import com.pqt.core.entities.product.Product;
import com.pqt.core.entities.product.ProductUpdate;
import com.pqt.server.exception.ServerQueryException;
import com.pqt.server.tools.entities.SaleContent;

import java.util.List;
import java.util.Map;

//TODO ajouter logs

/**
 *  Cette classe correspond au service de gestion du stock de produits. Il est en charge de la persistance des
 *  données liées aux produits, de founir un accès centralisé à ces données et se charge également d'appliquer les
 *  mises à jour de stock (ajout, modif ou suppr de produits) et les ventes de produits issues des commandes
 *  (modification des quantités).
 *  <p/>
 *  <b>Attention : ce service ne se charge pas de valider les commandes, il ne fait que modifier les quantités comme si
 *  la commande avait été validé</b>
 *
 * @see Product
 * @see ProductUpdate
 * @see SaleContent
 * @author Guillaume "Cess" Prost
 */
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

	public void applySale(SaleContent saleContent) {
		dao.applySale(saleContent);
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
				throw new ServerQueryException("Object ProductUpdate invalide : old et new vallent tous les deux null");
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
