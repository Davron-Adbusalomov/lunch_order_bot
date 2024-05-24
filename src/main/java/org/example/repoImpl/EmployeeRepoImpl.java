package org.example.repoImpl;

import org.example.config.DatabaseConfig;
import org.example.model.Employee;
import org.example.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class EmployeeRepoImpl implements EmployeeRepository {

    @Autowired
    private DatabaseConfig databaseConfig;

    private static final String GET_ALL_EMPLOYEE = "Select * from employee";

    private static final String GET_EMPLOYEE_BY_CODE = "SELECT * FROM employee WHERE code = ?";

    @Override
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

    @Override
    public Optional<Employee> getEmployeeByCode(int code) {
        Employee employee = null;
        try (Connection connection = databaseConfig.dataSource().getConnection();
             PreparedStatement ps = connection.prepareStatement(GET_EMPLOYEE_BY_CODE)) {
            ps.setInt(1, code);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    employee = new Employee(rs.getInt(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getInt(4));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(employee);
    }

}