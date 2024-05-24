package org.example.model;

import java.util.List;

public class Order {
    private int employee_id;

    private List<Integer> meals_id;

    public Order(int employee_id, List<Integer> meal_id) {
        this.employee_id = employee_id;
        this.meals_id = meal_id;
    }

    public Order(){}

    public int getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(int employee_id) {
        this.employee_id = employee_id;
    }

    public List<Integer> getMeals_id() {
        return meals_id;
    }

    public void setMeals_id(List<Integer> meals_id) {
        this.meals_id = meals_id;
    }
}
