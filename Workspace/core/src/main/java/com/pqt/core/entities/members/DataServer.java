package com.pqt.core.entities.members;

import java.io.Serializable;
import java.util.Date;

public class DataServer extends PqtMember{

	private String address;
	private Date lastUpdate;

	public DataServer() {
        super(-1, PqtMemberType.DATA_SERVER);
	}

    public DataServer(long id, String address) {
        super(id, PqtMemberType.DATA_SERVER);
        this.address = address;
        this.lastUpdate = new Date();
    }

    public DataServer(long id, String address, Date lastUpdate) {
        super(id, PqtMemberType.DATA_SERVER);
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
}
