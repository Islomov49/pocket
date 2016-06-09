package com.jim.pocketaccounter.finance;

import android.content.Context;
import android.provider.DocumentsContract;
import android.util.Log;

import com.jim.pocketaccounter.helper.PocketAccounterDatabase;
import com.jim.pocketaccounter.helper.PocketAccounterGeneral;

import java.util.ArrayList;

public class FinanceManager {
	private Context context;
	private ArrayList<Currency> currencies;
	private ArrayList<RootCategory> categories, expanses, incomes;
	private ArrayList<Account> accounts;
	private ArrayList<FinanceRecord> records;
	private PocketAccounterDatabase db;
	public FinanceManager(Context context) {
		this.context = context;
		db = new PocketAccounterDatabase(context);
		currencies = loadCurrencies();
		categories = loadCategories();
		expanses = loadExpanses();
		incomes = loadIncomes();
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
	private ArrayList<RootCategory> loadExpanses() {return db.loadExpanses();}
	private ArrayList<RootCategory> loadIncomes() {return db.loadIncomes();}
	public void saveExpanses() {db.saveExpanses(expanses);}
	public void saveIncomes() {db.saveIncomes(incomes);}
	public ArrayList<RootCategory> getExpanses() {return expanses;}
	public ArrayList<RootCategory> getIncomes() {return incomes;}
}