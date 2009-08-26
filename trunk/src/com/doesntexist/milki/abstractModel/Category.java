package com.doesntexist.milki.abstractModel;

import java.io.Serializable;

public class Category implements Serializable {
	private String id;
	private String name;
	
	public Category(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
}
