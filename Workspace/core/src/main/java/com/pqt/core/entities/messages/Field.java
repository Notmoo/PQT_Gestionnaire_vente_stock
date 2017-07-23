package com.pqt.core.entities.messages;

import java.io.Serializable;

public class Field implements Serializable{

	private String header;
	private String data;

	public Field(String header, String data) {
		this.header = header;
		this.data = data;
	}

	public String getHeader() {
		return header;
	}

	public String getData() {
		return data;
	}
}
