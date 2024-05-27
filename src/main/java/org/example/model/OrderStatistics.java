package org.example.model;

public class OrderStatistics {
    private int employeeId;

    private String employeeName;

    private int mealId;

    private String mealName;

    public OrderStatistics(Integer employeeId, Integer mealId, String employeeName, String mealName){
        this.employeeId=employeeId;
        this.mealId=mealId;
        this.employeeName=employeeName;
        this.mealName=mealName;
    }

    public OrderStatistics(){}

    public int getEmployeeId(){
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId){
        this.employeeId=employeeId;
    }

    public int getMealId() {
        return mealId;
    }

    public void setMealId(int mealId) {
        this.mealId = mealId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }


}
