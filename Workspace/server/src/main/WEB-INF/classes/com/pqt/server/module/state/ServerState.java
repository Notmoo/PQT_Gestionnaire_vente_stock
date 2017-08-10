package com.pqt.server.module.state;

public class ServerState {

	private int port;

	private boolean serverState;

	public ServerState() {
		port = -1;
		serverState = false;
	}

	public ServerState(int port) {
		this.port = port;
		serverState = false;
	}

	public ServerState(ServerState clone){
	    this.serverState = clone.serverState;
	    this.port = clone.port;
    }

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public boolean isServerState() {
		return serverState;
	}

	public void setServerState(boolean serverState) {
		this.serverState = serverState;
	}

    public ServerState copy() {
        return new ServerState(this);
    }
}
