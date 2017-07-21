package com.pqt.core.entities.query;

public class ConnectQuery extends SimpleQuery {

	private String serverAddress;

	public ConnectQuery(String serverAddress) {
		super(QueryType.CONNECT);
		this.serverAddress = serverAddress;
	}

	public String getServerAddress() {
		return serverAddress;
	}

	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}
}
