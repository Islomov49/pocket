package com.jim.pocketaccounter.debt;

/**
 * Created by user on 6/8/2016.
 */

public class Recking {

    private String payDate;
    private double amount;
    private String id;

    public Recking(String payDate, double amount, String id) {
        this.payDate = payDate;
        this.amount = amount;
        this.id = id;
    }

    public Recking() {
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
