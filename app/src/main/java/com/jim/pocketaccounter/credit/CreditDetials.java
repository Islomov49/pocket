package com.jim.pocketaccounter.credit;

import com.jim.pocketaccounter.debt.Recking;
import com.jim.pocketaccounter.finance.Currency;

import java.util.ArrayList;
import java.util.Calendar;

public class CreditDetials {
    private String credit_name;
    private int icon_ID;
    private Calendar take_time;
    private double procent;
    private long procent_interval;
    private long period_time;
    private long myCredit_id;
    private double value_of_credit;
    private double value_of_credit_with_procent;
    private Currency valyute_currency;
    private ArrayList<ReckingCredit> reckings;


    public CreditDetials(){

    }
    public CreditDetials(int icon_ID, String credit_name, Calendar take_time,
                         double procent, long procent_interval, long period_time,
                         double value_of_credit, Currency valyute_currency,
                         double value_of_credit_with_procent, long myCredit_id) {
        this.icon_ID=icon_ID;
        this.credit_name = credit_name;
        this.take_time = take_time;
        this.procent = procent;
        this.procent_interval = procent_interval;
        this.period_time = period_time;
        this.value_of_credit = value_of_credit;
        this.valyute_currency = valyute_currency;
        this.value_of_credit_with_procent=value_of_credit_with_procent;
        this.myCredit_id=myCredit_id;
        reckings = new ArrayList<>();
    }

    public ArrayList<ReckingCredit> getReckings() {
        return reckings;
    }
    public void setReckings(ArrayList<ReckingCredit> reckings) {
        this.reckings = reckings;
    }

    public long getMyCredit_id() {
        return myCredit_id;
    }
    public void setMyCredit_id(long myCredit_id) {
        this.myCredit_id = myCredit_id;
    }
    public int getIcon_ID() {
        return icon_ID;
    }
    public void setIcon_ID(int icon_ID) {
        this.icon_ID = icon_ID;
    }
    public double getValue_of_credit_with_procent() {
        return value_of_credit_with_procent;
    }
    public void setValue_of_credit_with_procent(double value_of_credit_with_procent) {
        this.value_of_credit_with_procent = value_of_credit_with_procent;
    }
    public String getCredit_name() {
        return credit_name;
    }
    public void setCredit_name(String credit_name) {
        this.credit_name = credit_name;
    }
    public Calendar getTake_time() {
        return take_time;
    }
    public void setTake_time(Calendar take_time) {
        this.take_time = take_time;
    }
    public double getProcent() {
        return procent;
    }
    public void setProcent(double procent) {
        this.procent = procent;
    }
    public long getProcent_interval() {
        return procent_interval;
    }
    public void setProcent_interval(long procent_interval) {
        this.procent_interval = procent_interval;
    }
    public long getPeriod_time() {
        return period_time;
    }
    public void setPeriod_time(long period_time) {
        this.period_time = period_time;
    }
    public double getValue_of_credit() {
        return value_of_credit;
    }
    public void setValue_of_credit(double value_of_credit) {
        this.value_of_credit = value_of_credit;
    }
    public Currency getValyute_currency() {
        return valyute_currency;
    }
    public void setValyute_currency(Currency valyute_currency) {
        this.valyute_currency = valyute_currency;
    }
}
