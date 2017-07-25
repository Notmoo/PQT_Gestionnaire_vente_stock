package com.pqt.server.module.client;

import com.pqt.core.entities.members.Client;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

//TODO écrire javadoc
//TODO ajouter logs
public class ClientService {

    private Set<Client> clientCache;

    public ClientService(){
        clientCache = new HashSet<>();
    }

	public boolean isClientRegistered(Client client) {
		return clientCache.contains(client);
	}

	public void registerClient(Client client) {
        if(clientCache.contains(client)){
            refreshClientTimestamp(client);
            clientCache.add(client);
        }
	}

	public Date getLastClientAction(Client client) {
		return client.getLastUpdate();
	}

	public void refreshClientTimestamp(Client client) {
        client.setLastUpdate(new Date());
	}
}
