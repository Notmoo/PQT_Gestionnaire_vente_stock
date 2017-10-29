package com.pqt.server.module.account;

import com.pqt.core.entities.user_account.Account;
import com.pqt.core.entities.user_account.AccountLevel;

import java.util.List;

//TODO ajouter logs
/**
 * Cette classe correspond au service de gestion des comptes utilisateurs. Il permet la vérification de l'existance
 * d'un compte, de son état (connecté/déconnecté), de changer son état ainsi que de récupérer son niveau d'accréditation.
 *
 * @author Guillaume "Cess" Prost
 * @see AccountLevel
 * @see Account
 */
public class AccountService {

	private IAccountDao dao;

    public AccountService() {
		dao = new FileAccountDao();
    }

    /**
     * Vérifie si un compte utilisateur donné est actuellement connecté. Le compte utilisateur doit être existant
     * pour être connecté.
     * <p/>
     * Les informations contenues dans l'objet {@code account} passé en paramètre ne sont pas directements utilisées,
     * elles servent juste à déterminer le compte utilisateur réel correspondant. <b>Il est nécessaire de s'assurer
     * que les données passées en paramètres soient justes avant de faire appel à cette méthode.</b>
     * <p/>
     * Pour cette méthode, seul le nom d'utilisateur est pris en compte pour établir une correspondance.
     * <p/>
     * Dans le cas où aucune correspondance ne peut être faite entre les informations fournies et les données
     * enregistrées, la valeur {@code false} sera renvoyée.
     *
     * @param account Objet {@link Account} dont les informations seront utilisées pour déterminer le compte concerné.
     *
     * @return {@code true} si une correspondance a pû être établie et que le compte correspondant est connecté,
     *      {@code false} sinon.
     */
    public boolean isAccountConnected(Account account) {
		return dao.isAccountConnected(account);
	}

    /**
     * Soumet une demande de changement d'état pour un compte utilisateur. Les états possibles sont "connecté" ({@code true})
     * et "déconnecté" ({@code false}).
     * <p/>
     * Les informations contenues dans l'objet {@code account} passé en paramètre ne sont pas directements utilisées,
     * elles servent juste à déterminer le compte utilisateur réel correspondant. <b>Il est nécessaire de s'assurer
     * que les données passées en paramètres soient justes avant de faire appel à cette méthode.</b>
     * <p/>
     * Pour cette méthode, seul le nom d'utilisateur est pris en compte pour établir une correspondance. Le mot de passe
     * est uniquement requis pour une connexion, et pas pour une déconnexion.
     * <p/>
     * Une fois la correspondance effectuée, une tentative de changement d'état sera faite pour le compte correspondant.
     * Cette tentative peut échouer si :
     * <ul>
     *     <li>Le compte est déjà dans l'état désiré</li>
     *     <li>Le mot de passe ne correspond pas (uniquement pour une connexion)</li>
     * </ul>
     * Dans le cas d'un échec, l'état du compte reste inchangé et la valeur booléenne {@code false} est renvoyée,
     * sans plus de détails. Si le changement d'état a eu lieu, la valeur booléenne {@code true} est renvoyée.
     *
     * @param account Objet {@link Account} dont les informations seront utilisées pour déterminer le compte concerné.
     * @param desiredState L'état dans lequel le compte doit se trouver une fois le changement fait.
     * @return {@code true} si le changement d'état a eu lieu, {@code false} sinon.
     */
	public boolean submitAccountCredentials(Account account, boolean desiredState) {
		return dao.submitAccountCredentials(account, desiredState);
	}

    /**
     * Vérifie si un compte utilisateur donné existe dans la base de donnée du serveur.
     * <p/>
     * Les informations contenues dans l'objet {@code account} passé en paramètre ne sont pas directements utilisées,
     * elles servent juste à déterminer le compte utilisateur réel correspondant. <b>Il est nécessaire de s'assurer
     * que les données passées en paramètres soient justes avant de faire appel à cette méthode.</b>
     * <p/>
     * Pour cette méthode, seul le nom d'utilisateur est pris en compte pour établir une correspondance.
     *
     * @param account Objet {@link Account} dont les informations seront utilisées pour déterminer le compte concerné.
     * @return {@code true} si une correspondance a pû être établie entre les données fournies et un compte dans la base
     *      de données, {@code false} sinon.
     */
	public boolean isAccountRegistered(Account account){
		return dao.isAccountRegistered(account);
	}

    /**
     * Récupère le niveau de permission du compte utilisateur correspondant aux informations fournies en paramètre.
     * <p/>
     * Les informations contenues dans l'objet {@code account} passé en paramètre ne sont pas directements utilisées,
     * elles servent juste à déterminer le compte utilisateur réel correspondant. <b>Il est nécessaire de s'assurer
     * que les données passées en paramètres soient justes avant de faire appel à cette méthode.</b>
     * <p/>
     * Pour cette méthode, seul le nom d'utilisateur est pris en compte pour établir une correspondance.
     *
     * @param account Objet {@link Account} dont les informations seront utilisées pour déterminer le compte concerné.
     * @return Le niveau de permission {@link AccountLevel} du compte correspondant aux informations si une correspondance
     * a pû être établie entre {@code account} et un compte utilisateur de la base de donnée, et {@code null} si aucune
     * correspondance n'a pû être faite.
     */
	public AccountLevel getAccountPermissionLevel(Account account){
		return dao.getAccountPermissionLevel(account);
	}

    /**
     * Renvoie la liste des comptes utilisateurs contenus dans la base de données sous forme d'une liste d'objets
     * {@link Account}. <b>Seuls les noms d'utilisateurs ainsi que les niveaux de permissions sont récupérés, les
     * autres champs étant volontairement initialisés avec une valeur {@code null}.</b>
     * @return Une liste d'objet {@link Account} représentant les différents comptes utilisateurs existant dans la base
     * de données.
     */
    public List<Account> getAccountList(){
	    return dao.getAccountList();
    }
}
