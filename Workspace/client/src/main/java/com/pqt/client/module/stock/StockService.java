package com.pqt.client.module.stock;

import com.pqt.client.module.query.QueryExecutor;
import com.pqt.core.entities.product.Product;
import com.pqt.client.module.stock.Listeners.IStockListener;

import java.util.Date;
import java.util.List;

//TODO Issue #6 : Add log lines
public class StockService {

	private StockDao dao;

	public StockService(QueryExecutor executor) {
		dao = new StockDao(executor);
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
     * La liste peut être obsolète, voir {@link #getLastRefreshTimestamp()} pour la date du dernier refresh et
	 * {@link #refreshProductList()} pour la mettre à jour.
     *
     * @return Liste des produits en vente.
     */
	public List<Product> getProducts() {
		return dao.getProducts();
	}

	/**
	 *	Récupère la liste des produits n'étant pas {@code product} et n'étant pas composé de {@code product}.
	 *  Les composants sont récursivements vérifiés pour que ces derniers valident aussi ces deux conditions.
	 *  <p/>
	 * La liste peut être obsolète, voir {@link #getLastRefreshTimestamp()} pour la date du dernier refresh et
	 * {@link #refreshProductList()} pour la mettre à jour.
	 *
	 * @param product produit à exclure des résultats.
	 * @return Liste de produit n'étant pas et ne contenant pas {@code product}.
	 */
	public List<Product> getProductsExcluding(Product product) {
		return dao.getProducts();
	}

	private boolean contains(Product container, Product contained){
	    if(container==null || contained==null)
	        return false;
	    if(container.equals(contained))
	        return true;

	    if(container.getComponents()!=null)
	        return container.getComponents()
                    .stream()
                    .filter(component->contains(component, contained))
                    .count()>0;

	    return false;
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
     *  Un objet {@link StockUpdateBuilder} permet de générer facilement une update. Pour valider l'update, voir {@link #commitUpdate(StockUpdateBuilder)}.
     *
     * @return nouvelle instance de la classe {@link StockUpdateBuilder}.
     */
	public StockUpdateBuilder getNewUpdateBuilder() {
		return new StockUpdateBuilder();
	}

    /**
     * Propose une modification de stock et lance la procédure de validation. Retourne l'identifiant de la modification, qui sera utilisé dans les notifications pour discriminer les événements.
     *  <p/>
     *  L'exécution de cette méthode ne constitue pas la modification, elle ne fait que démarrer la procédure.
     *  Les événements liées à la valiation des updates du stock peuvent être écoutés via la classe de listener {@link IStockListener}.
	 *  <p/>
	 *  Une procédure de mise à jour du stock sera automatiquement lancée si la modification est validée (comme si la méthode {@link #refreshProductList()} avait été appelée).
     * @param stockUpdateBuilder
     * @return l'identifiant de l'update soumise.
     */
	public long commitUpdate(StockUpdateBuilder stockUpdateBuilder) {
		return dao.commitUpdate(stockUpdateBuilder.build());
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

    public void shutdown() {
		//Nothing to do
    }
}
