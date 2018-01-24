package com.pqt.core.entities.user_account;

import java.util.Objects;

public class AccountUpdate {

    private Account oldVersion, newVersion;

    public AccountUpdate(Account oldVersion, Account newVersion) {
        this.oldVersion = oldVersion;
        this.newVersion = newVersion;
    }

    public Account getOldVersion() {
        return oldVersion;
    }

    public void setOldVersion(Account oldVersion) {
        this.oldVersion = oldVersion;
    }

    public Account getNewVersion() {
        return newVersion;
    }

    public void setNewVersion(Account newVersion) {
        this.newVersion = newVersion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountUpdate that = (AccountUpdate) o;
        return Objects.equals(oldVersion, that.oldVersion) &&
                Objects.equals(newVersion, that.newVersion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(oldVersion, newVersion);
    }
}
