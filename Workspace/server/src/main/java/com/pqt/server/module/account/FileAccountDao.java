package com.pqt.server.module.account;

import com.pqt.core.entities.user_account.Account;
import com.pqt.core.entities.user_account.AccountLevel;
import com.pqt.server.tools.io.ISerialFileManager;
import com.pqt.server.tools.io.SimpleSerialFileManagerFactory;
import com.pqt.server.tools.security.IHashTool;
import com.pqt.server.tools.security.RandomString;
import com.pqt.server.tools.security.SHA256HashTool;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

//TODO Issue #6 : ajouter logs

/**
 * Implémentation de l'interface {@link IAccountDao} utilisant un fichier contenant des objets sérialisés comme
 * source de persistance de données.
 * <p/>
 * Cette classe n'est pas faite pour gérer les accès concurentiels au fichier assurant la persistance, et n'est donc pas
 * thread-safe. Elle est conçue pour que tous les accès soient effectués depuis un même thread et depuis un unique objet.
 * <p/>
 * Cette classe manipule les mot de passe sous forme chiffrée via un système de hash (SHA-256) + salt, et ne fait pas
 * persister les mots de passes non-chiffrées. Les noms d'utilisateurs sont stockés sans chiffrage.
 */
public class FileAccountDao implements IAccountDao {

    private static final String ACCOUNT_FILE_NAME = "acc.pqt";
    private final String ACCOUNT_FILE_FOLDER_PATH;

    private Set<AccountEntry> accountEntries;
    private Set<AccountEntry> connectedAccount;
    private IHashTool hashTool;
    private RandomString randomString;
    private ISerialFileManager<AccountEntry> fileManager;

    public FileAccountDao(String ressourceFolderPathStr) {
        ACCOUNT_FILE_FOLDER_PATH = ressourceFolderPathStr;
        accountEntries = new HashSet<>();
        connectedAccount = new HashSet<>();
        hashTool = new SHA256HashTool();
        randomString = new RandomString(10);
        fileManager = SimpleSerialFileManagerFactory.getFileManager(AccountEntry.class, getAccountFilePathStr());
        loadFromFile();
    }

    private String getAccountFilePathStr(){
        return ACCOUNT_FILE_FOLDER_PATH + File.separator + ACCOUNT_FILE_NAME;
    }

    /**
     * Recherche une correspondance entre un objet {@link Account} et les objets {@link AccountEntry} contenu dans
     * la collection {@code entries}. La correspondance se base sur la valeur renvoyée par {@link Account#getUsername()}
     * et sur {@link AccountEntry#getUsername()}.
     * @param account données à utiliser pour rechercher la correspondance.
     * @param entries collection de données à utiliser pour rechercher la correspondance
     * @return La première correspondance trouvée, ou {@code null} si aucune correspondance n'a pu être faite.
     */
    private AccountEntry lookupMatchingEntry(Account account, Collection<AccountEntry> entries){
        return entries.stream()
                .filter(accountEntry ->
                    accountEntry != null
                            && account != null
                            && accountEntry.getUsername().equals(account.getUsername()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public synchronized boolean isAccountConnected(Account account) {
        return lookupMatchingEntry(account, connectedAccount)!=null;
    }

    @Override
    public synchronized boolean submitAccountCredentials(Account account, boolean desiredState) {
        if(isAccountRegistered(account)){
            if(desiredState!=isAccountConnected(account)){
                if(desiredState)
                    return connect(account);
                else
                    return disconnect(account);
            }
        }

        return false;
    }

    /**
     * Passe un compte déconnecté dans l'état connecté. N'effecctue le changement que si un compte déconnecté correspond
     * aux données fournies et que le mot de passe fournit par {@code account.getPassword()} corresspond à celui du
     * compte correspondant.
     *
     * @param account données à utiliser pour effectuer la correspondance et l'identification
     * @return {@code true} si le changement d'état a eu lieu, {@code false sinon}
     */
    private boolean connect(Account account){
        AccountEntry entry = lookupMatchingEntry(account, accountEntries);
        if(entry==null)
            return false;
        else{
            String expectedUsername = entry.getUsername();
            String expectedPasswordHash = entry.getPasswordHash();
            String salt = entry.getSalt();

            if(expectedUsername.equals(account.getUsername()) && hashTool.hashAndSalt(account.getPassword(), salt).equals(expectedPasswordHash)){
                connectedAccount.add(entry);
                return true;
            }else
                return false;
        }
    }

    /**
     * Passe un compte connecté dans l'état déconnecté. N'effectue le changement que si un compte connecté correspond
     * aux données fournies.
     * @param account données à utiliser pour efffectuer la correspondance avec un compte
     * @return {@code true} si le changement d'état a eu lieu, {@code false sinon}
     */
    private boolean disconnect(Account account){
       AccountEntry entry = lookupMatchingEntry(account, accountEntries);
        if(entry!=null && connectedAccount.contains(entry)){
            connectedAccount.remove(entry);
            return true;
        }
        return false;
    }

    @Override
    public synchronized boolean isAccountRegistered(Account account) {
        return lookupMatchingEntry(account, accountEntries)!=null;
    }

    @Override
    public synchronized AccountLevel getAccountPermissionLevel(Account account) {
        if(isAccountRegistered(account))
            return lookupMatchingEntry(account, accountEntries).getLevel();
        return null;
    }

    @Override
    public synchronized List<Account> getAccountList() {
        return accountEntries.stream().map(accountEntry -> new Account(accountEntry.getUsername(), null, accountEntry.getLevel())).collect(Collectors.toList());
    }

    /**
     * Sauvegarde les données des comptes dans le fichier de sauvegarde.
     */
    private void saveToFile(){
        fileManager.saveSetToFile(accountEntries);
    }

    public boolean addAccount(Account account){
        if(accountEntries.stream().filter(accountEntry -> accountEntry.getUsername().equals(account.getUsername())).count()==0) {
            String salt = randomString.nextString();
            String passwordHash = hashTool.hashAndSalt(account.getPassword(), salt);
            accountEntries.add(new AccountEntry(account.getUsername(), passwordHash, salt, account.getPermissionLevel()));
            saveToFile();
            return true;
        }else{
            return false;
        }
    }

    /**
     * Charge les données des comptes depuis le fichier de sauvegarde.
     * <p/>
     * <b>Attention : pour des raisons de cohérence des données, tous les comptes connectés sont repassés dans l'état
     * déconnectés une fois le chargement fait.</b>
     */
    private void loadFromFile(){
        this.accountEntries = new HashSet<>(fileManager.loadSetFromFile());
        //TODO faire check des comptes au lieu de tout déconnecter?
        this.connectedAccount.clear();
    }
}
