package com.jim.pocketaccounter.finance;

import android.content.Context;

import java.util.ArrayList;

public class FinanceManager {
	private Context context;
	private ArrayList<Currency> currencies;
	private ArrayList<RootCategory> categories;
	private ArrayList<Account> accounts;
	private ArrayList<FinanceRecord> records;
	public FinanceManager(Context context) {
		this.context = context;
		currencies = new ArrayList<Currency>();
		categories = new ArrayList<RootCategory>();
		records = new ArrayList<FinanceRecord>();
		setAccounts(new ArrayList<Account>());
	}
	public ArrayList<Currency> getCurrencies() {
		return currencies;
	}
	public void setCurrencies(ArrayList<Currency> currencies) {
		this.currencies = currencies;
	}
	private ArrayList<Currency> loadCurrencies() {
		return null;
	}
	public Currency getMainCurrency() {
		for (int i=0; i<currencies.size(); i++) {
			if (currencies.get(i).getMain())
				return currencies.get(i);
		}
		return null;
	}
	public ArrayList<RootCategory> getCategories() {
		return categories;
	}
	public void setCategories(ArrayList<RootCategory> categories) {
		this.categories = categories;
	}
	private ArrayList<Category> loadCategories() {
		return null;
	}
	public ArrayList<Account> getAccounts() {
		return accounts;
	}
	public void setAccounts(ArrayList<Account> accounts) {
		this.accounts = accounts;
	}
	public ArrayList<FinanceRecord> getRecords() {return records; }
	public void setRecords(ArrayList<FinanceRecord> records) {this.records = records;}
}