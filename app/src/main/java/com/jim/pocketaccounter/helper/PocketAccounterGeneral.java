package com.jim.pocketaccounter.helper;

import com.jim.pocketaccounter.PocketAccounter;
import com.jim.pocketaccounter.finance.Currency;
import com.jim.pocketaccounter.finance.FinanceRecord;
import com.jim.pocketaccounter.finance.RootCategory;

import java.util.ArrayList;
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
	public static double calculateAction(RootCategory category, Calendar date) {
		double result = 0.0;
		if (category == null) return 0.0;
		Calendar begin = (Calendar) date.clone();
		begin.set(Calendar.HOUR_OF_DAY, 0);
		begin.set(Calendar.MINUTE, 0);
		begin.set(Calendar.SECOND, 0);
		begin.set(Calendar.MILLISECOND, 0);
		Calendar end = (Calendar) date.clone();
		end.set(Calendar.HOUR_OF_DAY, 23);
		end.set(Calendar.MINUTE, 59);
		end.set(Calendar.SECOND, 59);
		end.set(Calendar.MILLISECOND, 59);
		for (int i=0; i<PocketAccounter.financeManager.getRecords().size(); i++) {
			FinanceRecord record = PocketAccounter.financeManager.getRecords().get(i);
			if (record.getDate().compareTo(begin) >= 0 && record.getDate().compareTo(end) <= 0 &&
					record.getCategory().getId().matches(category.getId()))
				result = result + getCost(record);
		}
		double totalAmount = 0.0;
		for (int i=0; i<PocketAccounter.financeManager.getRecords().size(); i++) {
			FinanceRecord record = PocketAccounter.financeManager.getRecords().get(i);
			if (record.getDate().compareTo(begin) >= 0 && record.getDate().compareTo(end) <= 0
					&& record.getCategory().getType() == category.getType())
				totalAmount = totalAmount + getCost(record);
		}
		if (totalAmount == 0.0) return 0.0;
		result = 100*result/totalAmount;
		return result;
	}
}
