package com.pqt.server.module.stock;

import com.pqt.core.entities.product.Product;
import com.pqt.server.tools.entities.SaleContent;

import java.util.List;

/**
 * Interface définissant les méthodes requises pour tout DAO du service de gestion des commandes {@link StockService}.
 * <p/>
 * Les implémentations doivent assurer une persistance des données relatives aux produits vendus, et doivent assurer
 * les modifications et les applications de ventes.
 *
 * @see StockService pour de plus amples détails sur le fonctionnement attendu des méthodes
 *
 * @author Guillaume "Cess" Prost
 */
public interface IStockDao {

    /**
     * Renvoie une copie de la liste des produits contenus dans la base de donnée.
     * @return copie de la liste des produits.
     */
	List<Product> getProductList();

    /**
     * Renvoie le produit correspondant à l'identifiant donné.
     * @param id identifiant du produit à récupérer
     * @return Produit correspondant, ou {@code null} si aucun produit ne correspond
     */
	Product getProduct(long id);

    /**
     * Ajoute un produit dans la base de donnée. Son identifiant sera éventuellement modifié pour éviter les conflits.
     * Dans tous les cas, l'identifiant final du produit est renvoyé une fois l'ajout effectué.
     * @param product produit à ajouter$
     * @return identifiant du produit ajouté.
     */
	long addProduct(Product product);

    /**
     * Supprime le produit correspondant à l'identifiant donné.
     * @param id identifiant du produit à supprimer.
     */
	void removeProduct(long id);


    /**
     * Modifie le produit correspondant à l'identifiant donné en le remplaçant par {@code product}. L'identifiant
     * reste inchangé.
     * <p/>
     * <b>Si {@code id} ne correspond à aucun produit, aucune modification n'est effectué. Cela signifie que
     * {@code product} n'est pas ajouté à la BDD.</b>
     * @param id identifiant du produit à modifier
     * @param product nouvelle version du produit
     */
	void modifyProduct(long id, Product product);

    /**
     * Applique les modifications de stocks liées à une commande validée, représenté par {@code saleContent}.
     * <p/>
     * @param saleContent détail des produits et quantités de la commande validée.
     * @throws IllegalArgumentException Exception levée si une erreur liée au contenu de {@code saleContent} survient.
     */
	void applySale(SaleContent saleContent) throws IllegalArgumentException;
}
