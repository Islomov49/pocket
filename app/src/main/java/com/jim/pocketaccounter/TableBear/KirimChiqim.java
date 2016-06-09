package com.jim.pocketaccounter.TableBear;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by user on 6/9/2016.
 */

public class KirimChiqim {

    private Calendar date;
    private ArrayList<Double> debt;
    private ArrayList<Double> outlay;
    private double proceeds;

    public KirimChiqim(Calendar date, ArrayList<Double> debt, ArrayList<Double> outlay, double proceeds) {
        this.date = date;
        this.debt = debt;
        this.outlay = outlay;
        this.proceeds = proceeds;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public ArrayList<Double> getDebt() {
        return debt;
    }

    public void setDebt(ArrayList<Double> debt) {
        this.debt = debt;
    }

    public ArrayList<Double> getOutlay() {
        return outlay;
    }

    public void setOutlay(ArrayList<Double> outlay) {
        this.outlay = outlay;
    }

    public double getProceeds() {
        return proceeds;
    }

    public void setProceeds(double proceeds) {
        this.proceeds = proceeds;
    }
}
