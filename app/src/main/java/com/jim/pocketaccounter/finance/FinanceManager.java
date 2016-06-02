package com.jim.pocketaccounter.finance;

import java.util.ArrayList;

import android.content.Context;

public class FinanceManager {
	private Context context;
	private ArrayList<Currency> currencies;
	private ArrayList<RootCategory> categories;
	private ArrayList<Account> accounts;
	public FinanceManager(Context context) {
		this.context = context;
		currencies = new ArrayList<Currency>();
		categories = new ArrayList<RootCategory>();
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
}
