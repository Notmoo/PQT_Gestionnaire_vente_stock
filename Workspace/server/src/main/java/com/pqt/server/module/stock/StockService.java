package com.pqt.server.module.stock;

import com.pqt.core.entities.product.Product;
import com.pqt.core.entities.product.ProductUpdate;
import com.pqt.server.exception.ServerQueryException;
import com.pqt.server.tools.entities.SaleContent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

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

	private static Logger LOGGER = LogManager.getLogger(StockService.class);

    private IStockDao dao;

    public StockService(String ressourceFolderPathStr) {
        dao = new FileStockDao(ressourceFolderPathStr);
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
    			removeProduct(upd.getOldVersion());
			}else if(upd.getOldVersion()!=null && upd.getNewVersion()!=null){
				modifyProduct(upd.getOldVersion(), upd.getNewVersion());
			}else{
				throw new ServerQueryException("Object ProductUpdate invalide : old et new valent tous les deux null");
            }
		}
	}

	private void addProduct(Product product) {
        LOGGER.info("Ajout du produit '{}' --> id #{}", product.getName(), dao.addProduct(product));
	}

	private void removeProduct(Product product) {
		LOGGER.info("Suppression du produit #{} --> '{}'", product.getId(), product.getName());
        dao.removeProduct(product.getId());
	}

	private void modifyProduct(Product oldVersion, Product newVersion) {
		LOGGER.info("Modification du produit #{} --> '{}'", oldVersion.getId());
        dao.modifyProduct(oldVersion.getId(), newVersion);
	}
}
