package com.jim.pocketaccounter.TableBear;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

/**
 * Created by user on 6/9/2016.
 */

public class KirimChiqim {

    private Calendar date;
    private Map<String, Double> debt;
    private Map<String, Double> outlay;
    private double proceeds;

    public KirimChiqim(Calendar date, Map<String, Double> debt, Map<String, Double> outlay, double proceeds) {
        this.date = date;
        this.debt = debt;
        this.outlay = outlay;
        this.proceeds = proceeds;
    }

    public KirimChiqim() {}

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public Map<String, Double> getDebt() {
        return debt;
    }

    public void setDebt(Map<String, Double> debt) {
        this.debt = debt;
    }

    public Map<String, Double> getOutlay() {
        return outlay;
    }

    public void setOutlay(Map<String, Double> outlay) {
        this.outlay = outlay;
    }

    public double getProceeds() {
        return proceeds;
    }

    public void setProceeds(double proceeds) {
        this.proceeds = proceeds;
    }
}
