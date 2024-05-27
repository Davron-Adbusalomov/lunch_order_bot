package org.example.controller;

import org.example.model.Employee;
import org.example.model.Meal;
import org.example.service.EmployeeService;
import org.example.service.MealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.DayOfWeek;
import java.util.List;

@RestController
public class MealController {
    @Autowired
    private MealService mealService;

    @GetMapping(value = "/getMealByWeekDay/{weekDay}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Meal> getMealByWeekDay(@PathVariable("weekDay") DayOfWeek weekDay) throws Exception {
        try {
            return mealService.getByWeekDay(weekDay);
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    public Meal getMealById(int mealId) {
        try {
            return mealService.getMealById(mealId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
