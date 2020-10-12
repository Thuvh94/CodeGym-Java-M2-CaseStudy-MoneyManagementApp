package model;

public class MoneyType {
    private int id;
    private boolean isIncomeType;
    private String name;

    public MoneyType() {
    }

//    public MoneyType(String name) {
//        this.name = name;
//    }

    public MoneyType(int id,boolean isIncomeType, String name) {
        this.id = id;
        this.isIncomeType = isIncomeType;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isIncomeType() {
        return isIncomeType;
    }

    public void setIncomeType(boolean incomeType) {
        isIncomeType = incomeType;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
