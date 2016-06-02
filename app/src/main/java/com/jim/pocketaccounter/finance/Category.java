package com.jim.pocketaccounter.finance;

public abstract class Category {
	private String name, id;
	public static final int INCOME=0, EXPANCE=1, BOTH=2;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}