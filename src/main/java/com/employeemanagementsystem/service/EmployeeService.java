package com.employeemanagementsystem.service;

import com.employeemanagementsystem.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {

    List<Employee> getAllEmployees();

    Page<Employee> getEmployeesPage(Pageable pageable);

    Page<Employee> searchEmployees(String keyword, Pageable pageable);

    Optional<Employee> getEmployeeById(Long id);

    Optional<Employee> getEmployeeByEmail(String email);

    Employee saveEmployee(Employee employee);

    void deleteEmployee(Long id);

    boolean existsByEmail(String email);
}