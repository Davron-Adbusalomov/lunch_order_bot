package org.example.model;

public class OrderStatistics {
    private int employeeId;

    private int mealId;

    public OrderStatistics(Integer employeeId, Integer mealId){
        this.employeeId=employeeId;
        this.mealId=mealId;
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


}
