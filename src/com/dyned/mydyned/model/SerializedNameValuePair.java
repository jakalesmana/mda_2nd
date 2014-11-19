package com.dyned.mydyned.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class SerializedNameValuePair implements Serializable {

	private String name;
	private String value;
	
	public SerializedNameValuePair(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}
}
