package com.jim.pocketaccounter.report;

import com.jim.pocketaccounter.finance.Category;

import java.util.ArrayList;

public class CategoryDataRow {
    private Category category;
    private double totalAmount;
    private ArrayList<SubCategoryWitAmount> subCats = new ArrayList<SubCategoryWitAmount>();
    public Category getCategory() {return category;}
    public void setCategory(Category category) {this.category = category;}
    public double getTotalAmount() {return totalAmount;}
    public void setTotalAmount(double totalAmount) {this.totalAmount = totalAmount;}
    public ArrayList<SubCategoryWitAmount> getSubCats() {return subCats;}
    public void setSubCats(ArrayList<SubCategoryWitAmount> subCats) {this.subCats = subCats;}
}