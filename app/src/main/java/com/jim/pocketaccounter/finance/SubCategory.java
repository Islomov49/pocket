package com.jim.pocketaccounter.finance;

public class SubCategory extends Category {
	private Category parent;
	public Category getParent() {
		return parent;
	}
	public void setParent(Category parent) {
		this.parent = parent;
	}
}
