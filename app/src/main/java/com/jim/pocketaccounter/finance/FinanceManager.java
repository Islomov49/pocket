package com.jim.pocketaccounter.finance;

import android.content.Context;

import com.jim.pocketaccounter.debt.DebtBorrow;
import com.jim.pocketaccounter.helper.PocketAccounterDatabase;

import java.util.ArrayList;

public class FinanceManager {
	private Context context;
	private ArrayList<Currency> currencies;
	private ArrayList<RootCategory> categories, expanses, incomes;
	private ArrayList<Account> accounts;
	private ArrayList<FinanceRecord> records;
	private ArrayList<DebtBorrow> debtBorrows;
	private PocketAccounterDatabase db;

	public FinanceManager(Context context) {
		this.context = context;
		db = new PocketAccounterDatabase(context);
		currencies = loadCurrencies();
		categories = loadCategories();
		accounts = loadAccounts();
		records = loadRecords();
		debtBorrows = loadDebtBorrows();
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
	public void setDebtBorrows(ArrayList<DebtBorrow> debtBorrows) {
		this.debtBorrows = debtBorrows;
	}
	public Currency getMainCurrency() {
		for (int i=0; i<currencies.size(); i++) {
			if (currencies.get(i).getMain())
				return currencies.get(i);
		}
		return null;
	}
	public ArrayList<DebtBorrow> getDebtBorrows() {return debtBorrows;}
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
	private ArrayList<RootCategory> loadExpanses() {return null;}
	private ArrayList<RootCategory> loadIncomes() {return null;}
	public void saveExpanses() {}
	public void saveIncomes() {}
	public ArrayList<DebtBorrow> loadDebtBorrows () {return db.loadDebtBorrows();}
	public void saveDebtBorrows () {db.saveDebtBorrowsToTable(debtBorrows);}
}