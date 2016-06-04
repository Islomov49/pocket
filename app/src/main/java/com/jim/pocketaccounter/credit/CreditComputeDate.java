package com.jim.pocketaccounter.credit;

/**
 * Created by developer on 02.06.2016.
 */

public class CreditComputeDate {
    private  int ID;
    private  String NameCredit;
    private  String valuta;
    private double shouldPay;
    private double overallDebt;
    private double periodPayed;
    private double totalPaid;
    private Long perionEndsInMilisec;

    public String getValuta() {
        return valuta;
    }

    public void setValuta(String valuta) {
        this.valuta = valuta;
    }

    public CreditComputeDate(int ID, String nameCredit, String valuta, double shouldPay, double overallDebt, double periodPayed, double totalPaid, long perionEndsInMilisec) {
        this.ID = ID;
        NameCredit = nameCredit;
        this.valuta=valuta;
        this.shouldPay = shouldPay;
        this.overallDebt = overallDebt;
        this.periodPayed = periodPayed;
        this.totalPaid = totalPaid;
        this.perionEndsInMilisec = perionEndsInMilisec;
    }




    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public double getShouldPay() {
        return shouldPay;
    }

    public void setShouldPay(double shouldPay) {
        this.shouldPay = shouldPay;
    }

    public double getOverallDebt() {
        return overallDebt;
    }

    public void setOverallDebt(double overallDebt) {
        this.overallDebt = overallDebt;
    }

    public double getPeriodPayed() {
        return periodPayed;
    }

    public void setPeriodPayed(double periodPayed) {
        this.periodPayed = periodPayed;
    }

    public double getTotalPaid() {
        return totalPaid;
    }

    public void setTotalPaid(double totalPaid) {
        this.totalPaid = totalPaid;
    }

    public String getNameCredit() {
        return NameCredit;
    }

    public void setNameCredit(String nameCredit) {
        NameCredit = nameCredit;
    }

    public long getPerionEndsInMilisec() {
        return perionEndsInMilisec;
    }

    public void setPerionEndsInMilisec(long perionEndsInMilisec) {
        this.perionEndsInMilisec = perionEndsInMilisec;
    }
}
