package com.pqt.core.entities.client;

import com.pqt.core.entities.log.ILoggable;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Notmoo on 18/07/2017.
 */
public class Client implements ILoggable, Serializable {

    private int id;
    private String address;
    private Date lastUpdate;

    public Client() {
    }

    public Client(int id, String address, Date lastUpdate) {
        this.id = id;
        this.address = address;
        this.lastUpdate = lastUpdate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
