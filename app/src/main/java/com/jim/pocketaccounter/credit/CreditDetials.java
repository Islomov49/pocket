package com.jim.pocketaccounter.credit;

import com.jim.pocketaccounter.finance.Currency;

/**
 * Created by developer on 02.06.2016.
 */

public class CreditDetials {
    private String credit_name;
    private int icon_ID;

    public int getIcon_ID() {
        return icon_ID;
    }

    public void setIcon_ID(int icon_ID) {
        this.icon_ID = icon_ID;
    }

    private long take_time;
    private double procent;
    private double procent_interval;
    //month , yeat, or day all
    private long period_time;
    private double value_of_credit;
    private Currency valyute_currency;

    public CreditDetials(){

    }
    public CreditDetials(int icon_ID,String credit_name, long take_time, double procent, double procent_interval, long period_time, double value_of_credit, Currency valyute_currency) {
        this.icon_ID=icon_ID;
        this.credit_name = credit_name;
        this.take_time = take_time;
        this.procent = procent;
        this.procent_interval = procent_interval;
        this.period_time = period_time;
        this.value_of_credit = value_of_credit;
        this.valyute_currency = valyute_currency;
    }

    public String getCredit_name() {

        return credit_name;
    }

    public void setCredit_name(String credit_name) {
        this.credit_name = credit_name;
    }

    public long getTake_time() {
        return take_time;
    }

    public void setTake_time(long take_time) {
        this.take_time = take_time;
    }

    public double getProcent() {
        return procent;
    }

    public void setProcent(double procent) {
        this.procent = procent;
    }

    public double getProcent_interval() {
        return procent_interval;
    }

    public void setProcent_interval(double procent_interval) {
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
