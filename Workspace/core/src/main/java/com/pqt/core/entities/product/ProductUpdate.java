package com.pqt.core.entities.product;

import com.pqt.core.entities.log.ILoggable;
import sun.misc.Version;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class ProductUpdate implements ILoggable, Serializable {

	private Product oldVersion;
	private Product newVersion;

    public ProductUpdate() {
    }

    public ProductUpdate(Product oldVersion, Product newVersion) {
        this.oldVersion = oldVersion;
        this.newVersion = newVersion;
    }

    public ProductUpdate(Date createdAt, Product oldVersion, Product newVersion) {
        this.oldVersion = oldVersion;
        this.newVersion = newVersion;
    }

    public Product getOldVersion() {
        return oldVersion;
    }

    public void setOldVersion(Product oldVersion) {
        this.oldVersion = oldVersion;
    }

    public Product getNewVersion() {
        return newVersion;
    }

    public void setNewVersion(Product newVersion) {
        this.newVersion = newVersion;
    }

    @Override
    public int hashCode() {
        return Objects.hash(oldVersion, newVersion);
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;

        if(!this.getClass().isInstance(obj))
            return false;

        ProductUpdate other = ProductUpdate.class.cast(obj);
        return Objects.equals(this.oldVersion, other.oldVersion)
                && Objects.equals(this.newVersion, other.newVersion);
    }
}
