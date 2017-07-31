package com.pqt.server.module.account;

import com.pqt.core.entities.user_account.Account;
import com.pqt.server.tools.io.ISerialFileManager;
import com.pqt.server.tools.io.SimpleSerialFileManagerFactory;
import com.pqt.server.tools.security.IHashTool;
import com.pqt.server.tools.security.MD5HashTool;

import java.util.*;

//TODO écrire Javadoc
//TODO ajouter logs
public class FileAccountDao implements IAccountDao {

    private static final String ACCOUNT_FILE_NAME = "acc.pqt";

    private Set<AccountEntry> accountEntries;
    private Set<AccountEntry> connectedAccount;
    private IHashTool hashTool;
    private ISerialFileManager<AccountEntry> fileManager;

    public FileAccountDao() {
        accountEntries = new HashSet<>();
        connectedAccount = new HashSet<>();
        hashTool = new MD5HashTool();
        fileManager = SimpleSerialFileManagerFactory.getFileManager(AccountEntry.class, ACCOUNT_FILE_NAME);
        loadFromFile();
    }

    private AccountEntry lookupMatchingEntry(Account account, Collection<AccountEntry> entries){
        return entries.stream().filter(accountEntry -> accountEntry.getUsername().equals(account.getUsername())).findFirst().orElse(null);
    }

    @Override
    public boolean isAccountConnected(Account account) {
        return lookupMatchingEntry(account, connectedAccount)!=null;
    }

    @Override
    public boolean submitAccountCredentials(Account acc, boolean desiredState) {
        if(isAccountRegistered(acc)){
            if(desiredState!=isAccountConnected(acc)){
                if(desiredState)
                    return connect(acc);
                else
                    return disconnect(acc);
            }
        }

        return false;
    }

    private boolean connect(Account account){
        Optional<AccountEntry> entry = accountEntries.stream().filter(accountEntry -> accountEntry.getUsername().equals(account.getUsername())).findFirst();
        if(!entry.isPresent())
            return false;
        else{
            String expectedUsername = entry.get().getUsername();
            String expectedPasswordHash = entry.get().getPasswordHash();
            String salt = entry.get().getSalt();

            if(expectedUsername.equals(account.getUsername()) && hashTool.hashAndSalt(account.getPassword(), salt).equals(expectedPasswordHash)){
                connectedAccount.add(entry.get());
                return true;
            }else
                return false;
        }
    }

    private boolean disconnect(Account account){
        Optional<AccountEntry> entry = accountEntries.stream().filter(accountEntry -> accountEntry.getUsername().equals(account.getUsername())).findFirst();
        if(entry.isPresent() && connectedAccount.contains(entry.get())){
            connectedAccount.remove(entry.get());
            return true;
        }
        return false;
    }

    @Override
    public boolean isAccountRegistered(Account account) {
        return lookupMatchingEntry(account, accountEntries)!=null;
    }

    private void saveToFile(){
        fileManager.saveSetToFile(accountEntries);
    }

    private void loadFromFile(){
        this.accountEntries = new HashSet<>(fileManager.loadSetFromFile());
        //TODO faire check des comptes au lieu de tout déconnecter?
        this.connectedAccount.clear();
    }
}
