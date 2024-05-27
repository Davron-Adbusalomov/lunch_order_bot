package org.example.controller;

import org.example.model.Employee;
import org.example.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @GetMapping(value = "/{code}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Employee getEmployeeByCode(@PathVariable("code") int code) throws Exception {
        try {
            return employeeService.getEmployeeByCode(code);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

}
