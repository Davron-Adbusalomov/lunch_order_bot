package org.example.controller;

import org.example.model.Order;
import org.example.model.OrderStatistics;
import org.example.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping(value = "/recordOrder", produces = MediaType.APPLICATION_JSON_VALUE)
    public void recordLunchOrder(Order order) throws Exception {
        try {
            orderService.orderMeal(order);
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @GetMapping(value = "/getStatistics", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<OrderStatistics> getStatistics() throws Exception {
        try {
            return orderService.getStatistics();
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }


}
