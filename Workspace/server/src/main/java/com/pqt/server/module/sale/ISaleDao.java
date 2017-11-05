package com.pqt.server.module.sale;

import com.pqt.core.entities.sale.LightweightSale;
import com.pqt.core.entities.sale.Sale;

/**
 * Interface définissant les méthodes requises pour tout DAO du service de gestion des commandes {@link SaleService}.
 * <p/>
 * Les implémentations de cette interface doivent pouvoir valider des commandes, agir sur le stock et générer les
 * identifiants de commandes validées.
 * <p/>
 * Les implémentations peuvent (optionnel) assurer une persistance des données relatives aux commandes validées, et
 * peuvent donc assurer le revert des commandes {@link #submitSaleRevert(long)}. <b>Le support de cette fonctionnalité
 * est optionnel</b>.
 *
 * @see SaleService pour de plus amples détails sur le fonctionnement attendu des méthodes
 */
public interface ISaleDao {

	long submitSale(Sale sale);

	Sale convert(LightweightSale lwSale);

	boolean isSaleRevertSupported();
	boolean submitSaleRevert(long id);
}
