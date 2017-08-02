package com.pqt.core.entities.members;

import com.pqt.core.entities.log.ILoggable;

import java.io.Serializable;

public class PqtMember implements ILoggable, Serializable {

	protected long id;
	protected PqtMemberType type;

	public PqtMember() {
	}

	public PqtMember(long id, PqtMemberType type) {
		this.id = id;
		this.type = type;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public PqtMemberType getType() {
		return type;
	}

	public void setType(PqtMemberType type) {
		this.type = type;
	}
}
