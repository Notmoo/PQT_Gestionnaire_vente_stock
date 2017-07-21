package com.pqt.core.entities.product;

import com.pqt.core.entities.log.ILoggable;
import sun.misc.Version;

import java.io.Serializable;
import java.util.Date;

public class ProductUpdate implements ILoggable, Serializable {

	private Date createdAt;
	private Product oldVersion;
	private Product newVersion;

    public ProductUpdate() {
    }

    public ProductUpdate(Product oldVersion, Product newVersion) {
        this.createdAt = new Date();
        this.oldVersion = oldVersion;
        this.newVersion = newVersion;
    }

    public ProductUpdate(Date createdAt, Product oldVersion, Product newVersion) {
        this.createdAt = createdAt;
        this.oldVersion = oldVersion;
        this.newVersion = newVersion;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
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
}
