package org.example.service;

import org.example.model.Employee;
import org.example.repoImpl.EmployeeRepoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepoImpl employeeRepo;

    public Employee getEmployeeByCode(int code){
        Optional<Employee> employee = employeeRepo.getEmployeeByCode(code);
        if (employee.isEmpty()){
            throw new NoSuchElementException("Kodeni noto'g'ri kiritdingiz!");
        }
        return employee.get();
    }
}
