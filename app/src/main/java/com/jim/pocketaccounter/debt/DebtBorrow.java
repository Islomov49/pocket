package com.jim.pocketaccounter.debt;

import android.support.v4.util.ArrayMap;

import com.jim.pocketaccounter.finance.Account;
import com.jim.pocketaccounter.finance.Currency;

import java.util.Calendar;
import java.util.Map;

public class DebtBorrow {
    private Person person;
    private Calendar takenDate, returnDate;
    private boolean remind;
    private boolean type;
    private Account account;
    private Currency currency;
    private double amount;
    private Map<Calendar, Double> recking;

    public DebtBorrow(Person person, Calendar takenDate, Calendar returnDate, boolean remind, Account account, Currency currency, double amount, boolean type) {
        this.person = person;
        this.takenDate = takenDate;
        this.returnDate = returnDate;
        this.remind = remind;
        this.account = account;
        this.currency = currency;
        this.amount = amount;
        recking = new ArrayMap<>();
        this.type = type;
    }

    public DebtBorrow() {}
    public Person getPerson() {return person;}
    public void setPerson(Person person) {this.person = person;}
    public Calendar getTakenDate() {return takenDate;}
    public void setTakenDate(Calendar takenDate) {this.takenDate = (Calendar)takenDate.clone();}
    public Calendar getReturnDate() {return returnDate;}
    public void setReturnDate(Calendar returnDate) {this.returnDate = (Calendar)returnDate.clone();}
    public boolean isRemind() {return remind;}
    public void setRemind(boolean remind) {this.remind = remind;}
    public Account getAccount() {return account;}
    public void setAccount(Account account) {this.account = account;}
    public Currency getCurrency() {return currency;}
    public void setCurrency(Currency currency) {this.currency = currency;}
    public double getAmount() {return amount;}
    public void setAmount(double amount) {this.amount = amount;}
    public boolean isType() {
        return type;
    }
    public void setType(boolean type) {
        this.type = type;
    }
    public void addRecking(Calendar calendar, double summ) {recking.put(calendar, summ);}
    public Map<Calendar, Double> getRecking() {return recking;}
}