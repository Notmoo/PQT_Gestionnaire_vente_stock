package com.pqt.server.module.state;

import com.pqt.core.entities.members.DataServer;

import java.util.Date;

/**
 * Cette classe correspond au service interne du serveur, chargé de conserver les données propres au serveur, comme
 * son adresse IP ou encore les différents aspects de la configuration actuelle. Il permet également de récupérer un
 * objet {@link DataServer}, implémentation de {@link com.pqt.core.entities.members.PqtMember}, qui sert à représenter
 * ce serveur dans les messages, soit comme émetteur, soit comme destinataire.
 *
 * @see com.pqt.core.entities.messages.Message
 */
public class ServerStateService {

	private ServerState serverState;
	private DataServer server;

	public ServerStateService() {
	    this.server = new DataServer();
	    this.serverState = new ServerState();

	    //TODO config adresse IP
        //this.com.pqt.server.setAddress(...);
	}

	public void startServer() {
        serverState.setServerState(true);
	}

	public void stopServer() {
        serverState.setServerState(false);
	}

	public void changeConnectionPort(int port) {
        serverState.setPort(port);
	}

	public DataServer getServer() {
		return server;
	}

	public void setServer(DataServer server) {
		this.server = server;
	}

	public ServerState getServerStateCopy() {
		return serverState.copy();
	}

}
