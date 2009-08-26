package com.doesntexist.milki.abstractModel;

import java.io.Serializable;

public class Account implements Serializable {
	private String id;
	private String name;
	private String remark;
	private double balance;
	
	public Account(String id, double balance) {
		this.id = id;
		this.balance = balance;
	}
	
	public String getId() {
		return id;
	}
}
