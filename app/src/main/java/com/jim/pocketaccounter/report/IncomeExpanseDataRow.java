package com.jim.pocketaccounter.report;
import com.jim.pocketaccounter.helper.PocketAccounterGeneral;

import java.util.ArrayList;
import java.util.Calendar;
public class IncomeExpanseDataRow {
    private Calendar date;
    private ArrayList<IncomeExpanseDayDetails> details;
    private double totalIncome;
    private double totalExpanse;
    private double totalProfit;
    public Calendar getDate() {return date;}
    public void setDate(Calendar date) {this.date = (Calendar)date.clone();}
    public ArrayList<IncomeExpanseDayDetails> getDetails() {return details;}
    public void setDetails(ArrayList<IncomeExpanseDayDetails> details) {this.details = details;}
    public double getTotalIncome() {return totalIncome;}
    public void setTotalIncome(double totalIncome) {this.totalIncome = totalIncome;}
    public double getTotalExpanse() {return totalExpanse;}
    public void setTotalExpanse(double totalExpanse) {this.totalExpanse = totalExpanse;}
    public double getTotalProfit() {return totalProfit;}
    public void setTotalProfit(double totalProfit) {this.totalProfit = totalProfit;}
    public IncomeExpanseDataRow(Calendar date) {this.date = (Calendar) date.clone();}
    public void calculate() {
        for (int i=0; i<details.size(); i++) {
            totalIncome = 0;
            totalExpanse = 0;
            totalProfit = 0;
            if (details.get(i).getCategory().getType() == PocketAccounterGeneral.INCOME)
                totalIncome = totalIncome + PocketAccounterGeneral.getCost(date, details.get(i).getCurrency(), details.get(i).getAmount());
            else
                totalExpanse = totalExpanse + PocketAccounterGeneral.getCost(date, details.get(i).getCurrency(), details.get(i).getAmount());
            totalProfit = totalIncome - totalExpanse;
        }
    }
}
