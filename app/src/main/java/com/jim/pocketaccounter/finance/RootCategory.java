package com.jim.pocketaccounter.finance;

import java.util.ArrayList;

public class RootCategory extends Category{
	private int icon, type;
	private ArrayList<SubCategory> subCategories;
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getIcon() {
		return icon;
	}
	public void setIcon(int icon) {
		this.icon = icon;
	}
	public ArrayList<SubCategory> getSubCategories() {
		return subCategories;
	}
	public void setSubCategories(ArrayList<SubCategory> subCategories) {
		this.subCategories = subCategories;
	}
}