package org.example.controller;

import org.example.model.Employee;
import org.example.model.Order;
import org.example.model.OrderStatistics;
import org.example.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/recordOrder")
    public void recordLunchOrder(Order order) throws Exception {
        try {
            orderService.assignMeals(order);
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @GetMapping("/getStatistics")
    public List<OrderStatistics> getEmployeeByCode() throws Exception {
        try {
            return orderService.getStatistics();
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }


}
