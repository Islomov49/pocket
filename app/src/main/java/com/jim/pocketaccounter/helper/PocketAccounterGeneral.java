package com.jim.pocketaccounter.helper;

import com.jim.pocketaccounter.PocketAccounter;
import com.jim.pocketaccounter.finance.Currency;
import com.jim.pocketaccounter.finance.FinanceRecord;

import java.util.Calendar;

public class PocketAccounterGeneral {
	public static final int NORMAL_MODE = 0;
	public static final int EDIT_MODE = 1;
	public static final int INCOME=0;
	public static final int EXPANCE=1;
	public static final int EXPANCE_BUTTONS_COUNT = 16;
	public static final int INCOME_BUTTONS_COUNT = 4;
	public static final int NO_MODE = 0, EXPANSE_MODE = 1, INCOME_MODE = 2;
	public static final int MAIN = 0, DETAIL = 1;
	public static double getCost(FinanceRecord record) {
		double amount = 0.0;
		if (record.getCurrency().getMain())
			return record.getAmount();
		double koeff = 1.0;
		for (int i=0; i<record.getCurrency().getCosts().size(); i++) {
			if (record.getDate().compareTo(record.getCurrency().getCosts().get(i).getDay())<=0) {
				koeff = record.getCurrency().getCosts().get(i).getCost();
				break;
			}
			if (record.getDate().compareTo(record.getCurrency().getCosts().get(i).getDay())>=0)
				koeff = record.getCurrency().getCosts().get(i).getCost();
		}
		amount = record.getAmount()/koeff;
		return amount;
	}
	public static double getCost(Calendar date, Currency currency, double amount) {
		double result = 0.0;
		if (currency.getMain()) return amount;
		double koeff = 1.0;
		for (int i=0; i<currency.getCosts().size(); i++) {
			if (date.compareTo(currency.getCosts().get(i).getDay())<=0) {
				koeff = currency.getCosts().get(i).getCost();
				break;
			}
			if (date.compareTo(currency.getCosts().get(i).getDay())>=0)
				koeff = currency.getCosts().get(i).getCost();
		}
		result = amount/koeff;
		return result;
	}
}
