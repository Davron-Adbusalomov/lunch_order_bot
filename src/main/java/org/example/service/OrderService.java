package org.example.service;

import org.example.model.Order;
import org.example.model.OrderStatistics;
import org.example.repoImpl.EmployeeRepoImpl;
import org.example.repoImpl.OrderRepoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepoImpl orderRepo;

    @Autowired
    private EmployeeRepoImpl employeeRepo;

    public void assignMeals(Order order) throws SQLException {
        try{
            orderRepo.orderMeal(order);
        }catch (Exception e){
            throw new SQLException("Meals could not be ordered!");
        }
    }

    public List<OrderStatistics> getStatistics() throws Exception {
        try {
            return orderRepo.getStatistics();
        }catch (Exception e){
            throw new Exception("Sorry statistics are not available yet!");
        }
    }
}
