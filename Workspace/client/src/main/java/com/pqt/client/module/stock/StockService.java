package com.pqt.client.module.stock;

import com.pqt.client.module.query.QueryExecutor;
import com.pqt.client.module.query.QueryFactory;
import com.pqt.client.module.query.query_callback.IUpdateQueryCallback;
import com.pqt.client.module.stock.Listeners.IStockFirerer;
import com.pqt.client.module.stock.Listeners.SimpleStockFirerer;
import com.pqt.core.entities.product.Product;
import com.pqt.client.module.stock.Listeners.IStockListener;

import java.util.Date;
import java.util.List;

//TODO Add log lines
public class StockService {

	public static final StockService INSTANCE = new StockService();

	private StockDao dao;

	private StockService() {
		dao = new StockDao();
	}

    /**
     * Accesseur renvoyant un objet {@link Date} correspondant à la dernière heure où les stocks ont été mis à jour
     *  <p/>
     * @return {@link Date} représentant l'instant de la dernière mise à jour des stocks.
     */
	public Date getLastRefreshTimestamp(){
	    return dao.getLastRefreshTimestamp();
    }

    /**
     * Démarre la procédure de mise à jour des stocks.
     *  <p/>
     * L'exécution de cette méthode ne constitue pas la mise à jour, elle ne fait que démarrer la procédure.
     * Les événements liées à la mise à jour du stock peuvent être écoutés via la classe de listener {@link IStockListener}.
     */
	public void refreshProductList() {
	    dao.refreshProductList();
	}

    /**
     * Accesseur de la liste des produits actuellement en vente.
     *  <p/>
     * La liste peut être obsolète, voir {@link #getLastRefreshTimestamp()} pour la date du dernier refresh et {@link #refreshProductList()} pour la mettre à jour.
     *
     * @return Liste des produits en vente.
     */
	public List<Product> getProducts() {
		return dao.getProducts();
	}

    /**
     * Accesseur récupérant un unique produit présent dans les stocks en se basant sur son id.
     *
     * @param id identifiant du produit voulu
     * @return produit voulu s'il est présent dans le stock, ou null si l'identifiant {@code id} ne correspond à aucun produit présent dans le stock.
     */
	public Product getProduct(int id) {
		return dao.getProduct(id);
	}

    /**
     * Renvoie un nouveau builder d'update de stock.
     *  <p/>
     *  Un update de stock correspond à un ticket de demande de modification du stock, composé d'ajouts, de suppressions et de modifications de produits.
     *  Un objet {@link UpdateBuilder} permet de générer facilement une update. Pour valider l'update, voir {@link #commitUpdate(UpdateBuilder)}.
     *
     * @return nouvelle instance de la classe {@link UpdateBuilder}.
     */
	public UpdateBuilder getNewUpdateBuilder() {
		return new UpdateBuilder();
	}

    /**
     * Propose une modification de stock et lance la procédure de validation. Retourne l'identifiant de la modification, qui sera utilisé dans les notifications pour discriminer les événements.
     *  <p/>
     *  L'exécution de cette méthode ne constitue pas la modification, elle ne fait que démarrer la procédure.
     *  Les événements liées à la valiation des updates du stock peuvent être écoutés via la classe de listener {@link IStockListener}.
	 *  <p/>
	 *  Une procédure de mise à jour du stock sera automatiquement lancée si la modification est validée (comme si la méthode {@link #refreshProductList()} avait été appelée).
     * @param updateBuilder
     * @return l'identifiant de l'update soumise.
     */
	public long commitUpdate(UpdateBuilder updateBuilder) {
		return dao.commitUpdate(updateBuilder.build());
	}

    /**
     * Ajoute un listener à la liste de diffusion des événements pouvant être levé par le service.
     * @param listener
     * @see {@link IStockListener}
     */
	public void addListener(IStockListener listener) {
		dao.addListener(listener);
	}

    /**
     * Retire un listener d'événement donné de la liste de diffusion des événements.
     * @param listener
     * @see {@link IStockListener}
     */
	public void removeListener(IStockListener listener) {
		dao.removeListener(listener);
	}

}
