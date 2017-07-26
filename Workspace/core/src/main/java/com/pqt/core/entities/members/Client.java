package com.pqt.core.entities.members;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

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

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), address);
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;

        if(!this.getClass().isInstance(obj))
            return false;

        Client other = Client.class.cast(obj);
        return super.equals(obj)
                && Objects.equals(this.address, other.address);
    }
}
