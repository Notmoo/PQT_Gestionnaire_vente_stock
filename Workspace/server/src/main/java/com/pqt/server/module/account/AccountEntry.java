package com.pqt.server.module.account;

import com.pqt.core.entities.user_account.AccountLevel;

import java.io.Serializable;

public class AccountEntry implements Serializable{
    private String username, passwordHash, salt;
    private AccountLevel level;

    public AccountEntry() {
    }

    public AccountEntry(String username, String passwordHash, String salt, AccountLevel level) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.salt = salt;
        this.level = level;
    }

    String getUsername() {
        return username;
    }

    String getPasswordHash() {
        return passwordHash;
    }

    String getSalt() {
        return salt;
    }

    AccountLevel getLevel() {
        return level;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AccountEntry that = (AccountEntry) o;

        if (!username.equals(that.username)) return false;
        if (!passwordHash.equals(that.passwordHash)) return false;
        if (!salt.equals(that.salt)) return false;
        return level == that.level;
    }

    @Override
    public int hashCode() {
        int result = username.hashCode();
        result = 31 * result + passwordHash.hashCode();
        result = 31 * result + salt.hashCode();
        result = 31 * result + level.hashCode();
        return result;
    }
}
