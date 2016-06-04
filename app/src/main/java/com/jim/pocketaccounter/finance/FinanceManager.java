package com.jim.pocketaccounter.finance;

import android.content.Context;

import com.jim.pocketaccounter.helper.PocketAccounterDatabase;

import java.util.ArrayList;

public class FinanceManager {
	private Context context;
	private ArrayList<Currency> currencies;
	private ArrayList<RootCategory> categories;
	private ArrayList<Account> accounts;
	private ArrayList<FinanceRecord> records;
	private PocketAccounterDatabase db;
	public FinanceManager(Context context) {
		this.context = context;
		db = new PocketAccounterDatabase(context);
		currencies = loadCurrencies();
		categories = loadCategories();
		accounts = loadAccounts();
		records = loadRecords();
	}
	public ArrayList<Currency> getCurrencies() {
		return currencies;
	}
	public void setCurrencies(ArrayList<Currency> currencies) {
		this.currencies = currencies;
	}
	private ArrayList<Currency> loadCurrencies() {
		return db.loadCurrencies();
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
	private ArrayList<RootCategory> loadCategories() {return db.loadCategories();}
	public ArrayList<Account> getAccounts() {
		return accounts;
	}
	private ArrayList<Account> loadAccounts() {return db.loadAccounts();}
	public ArrayList<FinanceRecord> getRecords() {return records; }
	private ArrayList<FinanceRecord> loadRecords() {return db.loadDailyRecords();}
	public void saveCurrencies() {db.saveDatasToCurrencyTable(currencies);}
	public void saveAccounts() {db.saveDatasToAccountTable(accounts);}
	public void saveCategories() {db.saveDatasToCategoryTable(categories);}
	public void saveRecords() {db.saveDatasToDailyRecordTable(records);}
}