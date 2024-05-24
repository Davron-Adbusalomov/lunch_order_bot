package org.example.controller;

import org.example.model.Employee;
import org.example.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("employees")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @GetMapping("getEmployeeByCode/{code}")
    public Employee getEmployeeByCode(@PathVariable int code) throws Exception {
        try {
            return employeeService.getEmployeeByCode(code);
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

}
