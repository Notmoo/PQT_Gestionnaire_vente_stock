package com.pqt.server.module.client;

import com.pqt.core.entities.members.Client;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

//TODO Issue #6 : ajouter logs

/**
 * Cette classe correspond au service de gestion des clients.
 * <p/>
 * Un client est une instance du logiciel de composition des commandes, ce dernier étant la principale entitée capable
 * d'envoyer des requêtes aux serveurs de données.
 * <p/>
 * Ce service est censé permettre la tracabilité des clients se connectant au serveur, en gardant en cache tous les
 * clients avec des horodateurs représentant la date et l'heure de la dernière requête reçue de chaque client.
 */
public class ClientService {

    private Set<ClientEntry> clientCache;

    public ClientService(){
        clientCache = new HashSet<>();
    }

    private ClientEntry lookupClientEntry(Client client){
        return clientCache.stream().filter(clientEntry -> clientEntry.check(client)).findFirst().orElse(null);
    }
    /**
     * Vérifie si le client donné est connu.
     * @param client client à vérifier
     * @return {@code true} si le client donné correspond à une entrée du cache, {@code false} sinon.
     */
	public boolean isClientRegistered(Client client) {
		return clientCache.contains(client);
	}

    /**
     * Enregistre un client dans le cache du service. Si le client existe déjà dans la base, rafraichit l'horodateur
     * associé.
     * @param client client à enregistrer
     */
	public void registerClient(Client client) {
        if(lookupClientEntry(client)==null){
            clientCache.add(new ClientEntry(client));
        }else{
            refreshClientTimestamp(client);
        }
	}

    /**
     * Etabit une correspondance entre {@code client} et une entrée du cache du service, et renvoie l'horodateur associé
     * à la correspondance. Renvoie {@code null} si aucune correspondance n'a pû être faite.
     * @param client données à utiliser pour établir la correspondance
     * @return l'horodateur associé à la correspondance, ou {@code null} si aucune correspondance ne peut être faite.
     */
	public Date getClientTimestamp(Client client) {
        ClientEntry entry = lookupClientEntry(client);
        return entry!=null? entry.getTimestamp() : null;
	}

    /**
     * Récupère la liste des clients actuellement dans le cache du service
     * @return Liste des clients dans le cache
     */
	public List<Client> getClientList(){
	    return clientCache.stream().map(ClientEntry::getClient).collect(Collectors.toList());
    }

    /**
     * Vide le cache du service.
     */
    public void clear(){
	    clientCache.clear();
    }

    /**
     * Met à jour l'horodateur associé au client donné.
     * @param client données à utiliser pour établir la correspondance
     */
	private void refreshClientTimestamp(Client client) {
	    ClientEntry entry = lookupClientEntry(client);
        if(entry!=null){
            entry.setTimestamp(new Date());
        }
	}
}
