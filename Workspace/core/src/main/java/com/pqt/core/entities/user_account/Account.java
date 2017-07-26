package com.pqt.core.entities.user_account;

import com.pqt.core.entities.log.ILoggable;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * Created by Notmoo on 18/07/2017.
 */
public class Account implements ILoggable, Serializable {
    private int id;
    private String username;
    private String passwordHash;
    private Date creationDate;
    private AccountLevel permissionLevel;

    public Account() {
    }

    public Account(int id, String username, String passwordHash, Date creationDate, AccountLevel permissionLevel) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.creationDate = creationDate;
        this.permissionLevel = permissionLevel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public AccountLevel getPermissionLevel() {
        return permissionLevel;
    }

    public void setPermissionLevel(AccountLevel permissionLevel) {
        this.permissionLevel = permissionLevel;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, passwordHash, permissionLevel);
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;

        if(!this.getClass().isInstance(obj))
            return false;

        Account acc = Account.class.cast(obj);
        return this.id == acc.id
                && Objects.equals(this.username, acc.username)
                && Objects.equals(this.passwordHash, acc.passwordHash)
                && Objects.equals(this.permissionLevel, acc.permissionLevel);
    }
}
