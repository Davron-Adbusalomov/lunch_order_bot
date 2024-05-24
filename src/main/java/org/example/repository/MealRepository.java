package org.example.repository;

import org.example.enums.WeekDays;
import org.example.model.Meal;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

@Repository
public interface MealRepository {
    List<Meal> getAll();

    List<Meal> getByWeekDay(DayOfWeek weekDay);
}
