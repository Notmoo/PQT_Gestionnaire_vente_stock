package com.pqt.core.entities.user_account;

import com.pqt.core.entities.log.ILoggable;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Notmoo on 18/07/2017.
 */
public class Account implements ILoggable, Serializable {
    private String username;
    private String password;
    private AccountLevel permissionLevel;

    public Account() {
    }

    public Account(String username, String password, AccountLevel permissionLevel) {
        this.username = username;
        this.password = password;
        this.permissionLevel = permissionLevel;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public AccountLevel getPermissionLevel() {
        return permissionLevel;
    }

    public void setPermissionLevel(AccountLevel permissionLevel) {
        this.permissionLevel = permissionLevel;
    }
}
