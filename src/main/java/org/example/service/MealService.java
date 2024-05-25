package org.example.service;

import org.example.config.DatabaseConfig;
import org.example.enums.WeekDays;
import org.example.model.Meal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MealService{
    @Autowired
    private DatabaseConfig databaseConfig;

    private static final String GET_ALL_MEALS = "SELECT * FROM meal";

    private static final String GET_MEAL_BY_WEEKDAY = "SELECT * FROM meal WHERE weekday = ?";

    public List<Meal> getAll() {
        List<Meal> resultList = new ArrayList<>();
        try (Connection connection = databaseConfig.dataSource().getConnection();
             PreparedStatement ps = connection.prepareStatement(GET_ALL_MEALS)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    resultList.add(new Meal(rs.getInt(1),
                            rs.getString(2),
                            rs.getString(3),
                            WeekDays.valueOf(rs.getString(4))));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultList;
    }

    public List<Meal> getByWeekDay(DayOfWeek weekDay) throws Exception {
        List<Meal> resultList = new ArrayList<>();
        try (Connection connection = databaseConfig.dataSource().getConnection();
             PreparedStatement ps = connection.prepareStatement(GET_MEAL_BY_WEEKDAY)) {
            ps.setString(1, weekDay.name());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    resultList.add(new Meal(rs.getInt(1),
                            rs.getString(2),
                            rs.getString(3),
                            WeekDays.valueOf(rs.getString(4))));
                }
            }
        } catch (SQLException e) {
            throw new Exception("No Food found for this day!");
        }
        return resultList;
    }
}