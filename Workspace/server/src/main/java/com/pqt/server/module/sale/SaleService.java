package com.pqt.server.module.sale;

import com.pqt.core.entities.sale.LightweightSale;
import com.pqt.core.entities.sale.Sale;
import com.pqt.core.entities.sale.SaleEdit;
import com.pqt.server.module.sale.listeners.ISaleFirerer;
import com.pqt.server.module.sale.listeners.ISaleListener;
import com.pqt.server.module.sale.listeners.SimpleSaleFirerer;
import com.pqt.server.module.serving.IServingDao;
import com.pqt.server.module.serving.ServingDao;
import com.pqt.server.module.stock.StockService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * Cette classe correspond au service de validation des commandes de produits.
 * <p/>
 * Ce service est censé pouvoir déterminer si une commmande (classe {@link Sale}) est valide ou non, et doit le cas
 * échéant effectuer les retraits de produits du stock. A chaque commande validée doit correspondre un identifiant
 * unique, qui doit être renvoyé en réponse de la validation. Cet identifiant doit permettre de pouvoir annuler
 * ultérieurement la commande correspondante via la méthode {@link #submitSaleRevert(long)}.
 * <p/>
 * Une commande est considérée comme valide si tous les produits composants la commande existent dans le stock et que
 * les quantités demandées dans la commande sont disponibles en stock.
 * <p/>
 * Ce service met également à disposition la possibilité d'enregistrer des observateurs, qui seront utilisés pour
 * exécuter des méthodes lors de certains événements, comme la validation d'une commande.
 *
 * @see ISaleListener
 */
public class SaleService {

    private static Logger LOGGER = LogManager.getLogger(SaleService.class);

    private ISaleDao dao;
    private ISaleFirerer eventFirerer;
    private IServingDao servingDao;

    public SaleService(StockService stockService, String ressourceFolderPathStr) {
        dao = new NoRevertFileSaleDao(stockService, ressourceFolderPathStr);
        eventFirerer = new SimpleSaleFirerer();
        servingDao = new ServingDao();
        dao.addServingDao(servingDao);
    }

    /**
     * Récupère la liste de des commandes à servir
     * @return liste de commandes
     */
    public List<LightweightSale> getServingSaleList(){
        return servingDao.getSaleList();
    }

    /**
     * Permet de compléter une commande
     * @param saleEdit objet de communication
     */
    public void completeCommand(SaleEdit saleEdit){
        servingDao.completeCommand(saleEdit);
    }

    /**
     * Soumet une commande au service pour validation. Si la commande est validée, les stocks seront débités et
     * l'identifiant de la commande sera renvoyé. Si la commande n'est pas validée, la valeur {@value -1} sera renvoyée
     * et les stocks resterons inchangés.
     * @param sale commande à valider
     * @return l'identifiant positif non-nul attribué à la commande si elle est validée, {@value -1} sinon.
     */
    public long submitSale(Sale sale) {
        long id = dao.submitSale(sale);
        if(id!=-1){
            LOGGER.info("Nouvelle commande : #{}, faite par '{}'(permission {}), de type '{}' et valant {}€",
                    id,
                    sale.getOrderedBy().getUsername(),
                    sale.getOrderedBy().getPermissionLevel().name(),
                    sale.getType().name(),
                    sale.getTotalPrice());
            eventFirerer.fireSaleValidatedEvent(sale);
        }else{
            LOGGER.info("Refus d'une commande : faite par '{}'(permission {}), de type '{}' et valant {}€",
                    sale.getOrderedBy().getUsername(),
                    sale.getOrderedBy().getPermissionLevel().name(),
                    sale.getType().name(),
                    sale.getTotalPrice());
        }
		return id;
	}

    /**
     * Soumet une commande au service pour validation. Si la commande est validée, les stocks seront débités et
     * l'identifiant de la commande sera renvoyé. Si la commande n'est pas validée, la valeur {@value -1} sera renvoyée
     * et les stocks resterons inchangés.
     * <p/>
     * Les produits de la commande seront récupéré automatiquement via le service de stocks en se basant sur les id
     * présent dans {@code sale}. Si un id ne correspond pas à un produit, la totalité de la commande sera refusée.
     *
     * @param lwSale commande à valider
     * @return l'identifiant positif non-nul attribué à la commande si elle est validée, {@value -1} sinon.
     */
    public long submitSale(LightweightSale lwSale) {
        Sale sale = dao.convert(lwSale);
        if(sale!=null)
            return submitSale(sale);
        else
            LOGGER.info("Refus d'une lightweight commande : impossible de convertir en commande détaillée\n" +
                    "Faite par '{}'(premission {}), de type '{}' et valant {}€",
                    lwSale.getOrderedBy().getUsername(),
                    lwSale.getOrderedBy().getPermissionLevel().name(),
                    lwSale.getType().name(),
                    lwSale.getPrice());
        return -1;
    }

    /**
     * Détermine si le rollback de commande est supporté par la configuration actuelle du serveur ou non.
     * <p/>
     * Tenter d'effectuer un rollback de commande alors que ce dernier n'est pas supporté lèvera une
     * {@link UnsupportedOperationException}.
     *
     * @return {@code true} si le rollback de commande est supporté, {@code false} sinon.
     */
	public boolean isSaleRevertSupported(){
        return dao.isSaleRevertSupported();
    }

    /**
     * Demande le rollback d'une commande en se basant sur l'identifiant.
     * @param id identifiant de la commande à annuler
     * @return {@code true} si la commande a bel et bien été annulée, {@code false} si aucun changement n'a été fait.
     */
	public boolean submitSaleRevert(long id) {
	    if(isSaleRevertSupported()){
	        boolean status = dao.submitSaleRevert(id);
	        if(status)
                LOGGER.info("Revert de la commande #{}", id);
            else
                LOGGER.info("Echec du revert de la commande #{}", id);
            return status;
        }else
	        throw new UnsupportedOperationException("Cette opération ('sale revert') n'est pas supportée par la configuration actuelle du serveur");
	}

    /**
     * Ajout un observateur au service, qui sera notifié lorsque certains événements auront lieu.
     * @param l observateur à ajouter.
     * @see ISaleListener
     */
    public void addListener(ISaleListener l) {
        eventFirerer.addListener(l);
    }

    /**
     * Retire un observateur du service.
     * @param l observateur à retirer.
     * @see ISaleListener
     */
    public void removeListener(ISaleListener l){
        eventFirerer.addListener(l);
    }

    public String getServingVersion() {
        return String.valueOf(servingDao.getVersion());
    }
}
