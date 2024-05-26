package org.example.service;

import org.example.config.DatabaseConfig;
import org.example.model.Order;
import org.example.model.OrderStatistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderService {
    @Autowired
    private DatabaseConfig databaseConfig;

    private static final String ASSIGN_LUNCH_TO_EMPLOYEE = "INSERT INTO employee_meal (employee_id, meal_id) VALUES (?, ?)";

    private static final String GET_ORDER_STATISTICS = "SELECT * FROM employee_meal ";

    public void orderMeal(Order order) throws Exception {
        try (Connection connection = databaseConfig.dataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(ASSIGN_LUNCH_TO_EMPLOYEE)) {

            for (Integer mealId : order.getMeals_id()) {
                statement.setInt(1, order.getEmployee_id());
                statement.setInt(2, mealId);
                statement.executeUpdate();
            }

        } catch (SQLException e) {
            throw new Exception("Saqlashda xatolik yuz berdi!");
        }
    }

    public List<OrderStatistics> getStatistics(){
        List<OrderStatistics> orderStatisticsList = new ArrayList<>();
        OrderStatistics orderStatistics;
        try (Connection connection = databaseConfig.dataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_ORDER_STATISTICS)){
            try(ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()){
                    orderStatistics = new OrderStatistics(resultSet.getInt(1),
                            resultSet.getInt(2));
                    orderStatisticsList.add(orderStatistics);
                }
                return orderStatisticsList;
            }
        } catch (SQLException e) {
                throw new RuntimeException("Ma'lumotlar bazasiga ulanishda xatolik yuz berdi!");
        }
    }
}
