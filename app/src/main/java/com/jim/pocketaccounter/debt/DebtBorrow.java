package com.jim.pocketaccounter.debt;

import android.support.v4.util.ArrayMap;

import com.jim.pocketaccounter.finance.Account;
import com.jim.pocketaccounter.finance.Currency;

import java.util.Calendar;
import java.util.Map;

public class DebtBorrow {
    private Person person;
    private Calendar takenDate, returnDate;
    private int type;
    private String account;
    private String currency;
    private double amount;
    private Map<Calendar, Double> recking;
    public static final int DEBT = 0, BORROW = 1;
    private String id; //"debt_"+UUID.randowUUID().toString();

    public DebtBorrow(Person person, Calendar takenDate, Calendar returnDate, String id, String account, String currency, double amount, int type) {
        this.person = person;
        this.takenDate = takenDate;
        this.returnDate = returnDate;
        this.account = account;
        this.currency = currency;
        this.amount = amount;
        recking = new ArrayMap<>();
        this.type = type;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DebtBorrow() {}
    public Person getPerson() {return person;}
    public void setPerson(Person person) {this.person = person;}
    public Calendar getTakenDate() {return takenDate;}
    public void setTakenDate(Calendar takenDate) {this.takenDate = (Calendar)takenDate.clone();}
    public Calendar getReturnDate() {return returnDate;}
    public void setReturnDate(Calendar returnDate) {this.returnDate = (Calendar)returnDate.clone();}
    public String getAccount() {return account;}
    public void setAccount(String account) {this.account = account;}
    public String getCurrency() {return currency;}
    public void setCurrency(String currency) {this.currency = currency;}
    public double getAmount() {return amount;}
    public void setAmount(double amount) {this.amount = amount;}
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public void addRecking(Calendar calendar, double summ) {recking.put(calendar, summ);}
    public Map<Calendar, Double> getRecking() {return recking;}
    public void setRecking(ArrayMap<Calendar, Double> recking) {
        this.recking = recking;
    }

}