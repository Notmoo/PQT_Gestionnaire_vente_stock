package com.pqt.core.entities.members;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Notmoo on 18/07/2017.
 */
public class Client extends PqtMember{

    private String address;
    private Date lastUpdate;

    public Client() {
        super(-1, PqtMemberType.CLIENT);
    }

    public Client(int id, String address) {
        super(id, PqtMemberType.CLIENT);
        this.address = address;
        this.lastUpdate = new Date();
    }

    public Client(int id, String address, Date lastUpdate) {
        super(id, PqtMemberType.CLIENT);
        this.address = address;
        this.lastUpdate = lastUpdate;
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
