package com.jim.pocketaccounter.debt;
import com.jim.pocketaccounter.finance.Account;
import com.jim.pocketaccounter.finance.Currency;
import java.util.Calendar;
public class DebtBorrow {
    private Person person;
    private Calendar takenDate, returnDate;
    private boolean remind;
    private Account account;
    private Currency currency;
    private double amount;
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
}