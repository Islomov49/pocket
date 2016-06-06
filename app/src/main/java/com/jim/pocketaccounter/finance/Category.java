package com.jim.pocketaccounter.finance;

public abstract class Category {
	private String name, id;
	private int icon;
	public String getName() {return name;}
	public int getIcon() {return icon;}
	public void setIcon(int icon) {this.icon = icon;}
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