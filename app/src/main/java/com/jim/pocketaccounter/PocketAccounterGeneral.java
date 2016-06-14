package com.jim.pocketaccounter;

import com.jim.pocketaccounter.PocketAccounter;
import com.jim.pocketaccounter.debt.DebtBorrow;
import com.jim.pocketaccounter.debt.Recking;
import com.jim.pocketaccounter.finance.Currency;
import com.jim.pocketaccounter.finance.FinanceRecord;

public class PocketAccounterGeneral {
	public static final int NORMAL_MODE = 0;
	public static final int EDIT_MODE = 1;
	public static final int INCOME=0;
	public static final int EXPANCE=1;
	public static final int BOTH=2;
	public static final int EXPANCE_BUTTONS_COUNT = 16;
	public static final int INCOME_BUTTONS_COUNT = 4;

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
}
