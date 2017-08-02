package com.pqt.core.entities.members;

import java.util.Date;

public class DataServer extends PqtMember{

	private String address;

	public DataServer() {
        super(-1, PqtMemberType.DATA_SERVER);
	}

    public DataServer(long id, String address) {
        super(id, PqtMemberType.DATA_SERVER);
        this.address = address;
    }

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
