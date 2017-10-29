package com.pqt.server.module.account;

import com.pqt.core.entities.user_account.Account;
import com.pqt.core.entities.user_account.AccountLevel;

import java.util.List;

/**
 * Cette interface définit les méthodes de base que doivent avoir les classes de DAO du service {@link AccountService}.
 * <p/>
 * Les implémentations de cette interface sont censé assurer la persistance des donnéess de connexions des comptes
 * utilisateurs et doit également garder une trace niveau runtime de l'état des comptes (état connecté ou état
 * déconnecté). Enfin, les implémentations doivent pouvoir déterminer une correspondance entre un nom d'utilisateur et
 * un compte, et doit pouvoir effectuer les changements d'état sur la base d'un mot de passe non-chiffré fournit grâce
 * à une instance {@link Account} (voir {@link #submitAccountCredentials(Account, boolean)}).
 *
 * @author Guillaume "Cess" Prost
 */
interface IAccountDao {

    /**
     * @see AccountService#isAccountConnected(Account)
     *
     * @param account données à utiliser
     * @return {@code true} si les données correspondent à un compte et que ce dernier est connecté, {@code false}
     * sinon.
     */
	boolean isAccountConnected(Account account);

    /**
     * @see AccountService#submitAccountCredentials(Account, boolean)
     *
     * @param account données à utiliser
     * @param desiredState état désiré pour le compte
     * @return {@code true} si les données correspondent à un compte et que le changement d'état a eu lieu,
     * {@code false} sinon.
      */
	boolean submitAccountCredentials(Account account, boolean desiredState);

    /**
     * @see AccountService#isAccountRegistered(Account)
     *
     * @param account données à utiliser
     * @return {@code true} si les données correspondent à un compte, {@code false} sinon.
     */
	boolean isAccountRegistered(Account account);

    /**
     * @see AccountService#getAccountPermissionLevel(Account)
     * @param account données à utiliser
     * @return Le niveau d'accréditation du compte utilisateur correspondant aux données, ou {@code null} si aucun
     * compte ne correspond.
     */
    AccountLevel getAccountPermissionLevel(Account account);

    /**
     * @see AccountService#getAccountList()
     * @return Une liste d'objet {@link Account} représentant les différents comptes utilisateurs existant dans la base
     * de données.
     */
	List<Account> getAccountList();
}
