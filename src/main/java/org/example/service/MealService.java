package org.example.service;

import org.example.enums.WeekDays;
import org.example.model.Meal;
import org.example.repoImpl.MealRepoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class MealService {

    @Autowired
    private MealRepoImpl mealRepo;

    public List<Meal> getMealsByWeekDay(DayOfWeek weekDay){
        List<Meal> meals = mealRepo.getByWeekDay(weekDay);
        if (meals.isEmpty()){
            throw new NoSuchElementException("Bugun uchun ovqatlar yo'q uzr!");
        }
        return meals;
    }
}
