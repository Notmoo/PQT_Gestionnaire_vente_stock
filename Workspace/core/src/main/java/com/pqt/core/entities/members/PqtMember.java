package com.pqt.core.entities.members;

import com.pqt.core.entities.log.ILoggable;

import java.io.Serializable;
import java.util.Objects;

public class PqtMember implements ILoggable, Serializable {

	private long id;
	private PqtMemberType type;

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

	@Override
	public int hashCode() {
		return Objects.hash(id, type);
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;

		if(!this.getClass().isInstance(obj))
			return false;

		PqtMember other = PqtMember.class.cast(obj);
		return this.id == other.id
				&& Objects.equals(this.type, other.type);
	}
}
