package com.jim.pocketaccounter.credit;

import com.jim.pocketaccounter.finance.Currency;

/**
 * Created by developer on 02.06.2016.
 */

public class CreditComputeDate {
    private  int ID;
    private double procent_100_system;
    private double total_value;
    private double total_paid;
    private long date_start;
    private long interval;
    private Currency valyuta;


    public long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public CreditComputeDate(){
        ID=0;
        procent_100_system=0;
        total_value=0;
        total_paid=0;
        date_start=0;

    }
    public CreditComputeDate(int ID, double procent_100_system, double total_value, double total_paid, long date_start,long interval, Currency valyuta) {
        this.ID = ID;
        this.procent_100_system = procent_100_system;
        this.total_value = total_value;
        this.total_paid = total_paid;
        this.date_start = date_start;
        this.interval = interval;

        this.valyuta=valyuta;
    }

    public Currency getValyuta() {
        return valyuta;
    }

    public void setValyuta(Currency valyuta) {
        this.valyuta = valyuta;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public double getProcent_100_system() {
        return procent_100_system;
    }

    public void setProcent_100_system(double procent_100_system) {
        this.procent_100_system = procent_100_system;
    }

    public double getTotal_value() {
        return total_value;
    }

    public void setTotal_value(double total_value) {
        this.total_value = total_value;
    }

    public double getTotal_paid() {
        return total_paid;
    }

    public void setTotal_paid(double total_paid) {
        this.total_paid = total_paid;
    }

    public long getDate_start() {
        return date_start;
    }

    public void setDate_start(long date_end) {
        this.date_start = date_end;
    }


}
