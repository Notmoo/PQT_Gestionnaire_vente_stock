package com.pqt.server.module.state;

import com.pqt.core.entities.members.DataServer;

import java.util.Date;

//TODO écrire Javadoc
//TODO Ajouter logs
public class ServerStateService {

	private ServerState serverState;
	private DataServer server;

	public ServerStateService() {
	    this.server = new DataServer();
	    this.serverState = new ServerState();

	    //TODO config adresse IP
        //this.server.setAddress(...);

        this.server.setLastUpdate(new Date());
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

	public ServerState getServerStateCopy() {
		return serverState.copy();
	}

}
