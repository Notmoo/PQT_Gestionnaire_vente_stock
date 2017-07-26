package com.pqt.core.entities.members;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

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

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), address);
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;

		if(!this.getClass().isInstance(obj))
			return false;

		DataServer other = DataServer.class.cast(obj);
		return super.equals(obj)
				&& Objects.equals(this.address, other.address);
	}
}
