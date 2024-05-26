package org.example.service;

import org.example.config.DatabaseConfig;
import org.example.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class EmployeeService {

    @Autowired
    private DatabaseConfig databaseConfig;

    private static final String GET_ALL_EMPLOYEE = "Select * from employee";

    private static final String GET_EMPLOYEE_BY_CODE = "SELECT * FROM employee WHERE code = ?";

    public List<Employee> getAll() {
        List<Employee> resultList = new ArrayList<>();
        try (Connection connection = databaseConfig.dataSource().getConnection();
             PreparedStatement ps = connection.prepareStatement(GET_ALL_EMPLOYEE)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    resultList.add(new Employee(rs.getInt(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getInt(4)));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultList;
    }

    public Employee getEmployeeByCode(int code) throws Exception {
        Employee employee;
        try (Connection connection = databaseConfig.dataSource().getConnection();
             PreparedStatement ps = connection.prepareStatement(GET_EMPLOYEE_BY_CODE)) {
            ps.setInt(1, code);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    employee = new Employee(rs.getInt(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getInt(4));
                } else {
                    throw new Exception("Bu nomerdagi ishchi topilmadi!");
                }
            }
        } catch (SQLException e) {
            throw new Exception("Ma'lumotlar bazasiga ulanishda xatolik yuz berdi!");
        }
        return employee;
    }

}
