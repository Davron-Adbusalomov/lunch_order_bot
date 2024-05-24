package org.example.repository;

import org.example.model.Employee;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository {
     List<Employee> getAll();

     Optional<Employee> getEmployeeByCode(int code);
}
