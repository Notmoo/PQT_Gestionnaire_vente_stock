package com.pqt.server.module.client;

import com.pqt.core.entities.members.Client;

import java.util.Date;

public class ClientEntry {
    private Client client;
    private Date timestamp;

    public ClientEntry(Client client) {
        this.client = client;
        timestamp = new Date();
    }

    public ClientEntry(Client client, Date timestamp) {
        this.client = client;
        this.timestamp = timestamp;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public boolean check(Client client){
        return this.client.equals(client);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClientEntry that = (ClientEntry) o;

        if (client != null ? !client.equals(that.client) : that.client != null) return false;
        return timestamp != null ? timestamp.equals(that.timestamp) : that.timestamp == null;
    }

    @Override
    public int hashCode() {
        int result = client != null ? client.hashCode() : 0;
        result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
        return result;
    }
}
