package model;

import java.time.LocalDate;

public class Money {
    private long amount;
    private boolean isIncome;
    private String description;
    private MoneyType moneyType;
    private LocalDate date;

    public Money() {
    }

    public Money(long amount, boolean isIncome, String description, LocalDate date) {
        this.amount = amount;
        this.isIncome = isIncome;
        this.description = description;
        this.date = date;
    }

    public Money(long amount, boolean isIncome, String description, MoneyType moneyType, LocalDate date) {
        this.amount = amount;
        this.isIncome = isIncome;
        this.description = description;
        this.moneyType = moneyType;
        this.date = date;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public boolean isIncome() {
        return isIncome;
    }

    public void setIncome(boolean income) {
        isIncome = income;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public MoneyType getMoneyType() {
        return moneyType;
    }

    public void setMoneyType(MoneyType moneyType) {
        this.moneyType = moneyType;
    }

    @Override
    public String toString() {
        return "Money{" +
                "amount=" + amount +
                ", isIncome=" + isIncome +
                ", description='" + description + '\'' +
                ", moneyType=" + moneyType +
                ", date=" + date +
                '}';
    }
}
