package com.pqt.core.entities.members;

import java.util.Date;

/**
 * Created by Notmoo on 18/07/2017.
 */
public class Client extends PqtMember{

    private String address;

    public Client() {
        super(-1, PqtMemberType.CLIENT);
    }

    public Client(long id, String address) {
        super(id, PqtMemberType.CLIENT);
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Client client = (Client) o;

        return address.equals(client.address) && id==client.id && type.equals(client.type);
    }

    @Override
    public int hashCode() {
        return address.hashCode() + type.hashCode() + Integer.class.cast(id);
    }
}
