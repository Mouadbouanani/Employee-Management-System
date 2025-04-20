package com.employeemanagementsystem.integration;

import com.employeemanagementsystem.model.Employee;
import com.employeemanagementsystem.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class EmployeeControllerIT {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    public void testCreateAndFindEmployee() {
        // given
        Employee employee = new Employee();
        employee.setFirstName("Mouad");
        employee.setLastName("Bouanani");
        employee.setEmail("mouad@example.com");
        employee.setPassword("test123");

        // when
        employee = employeeRepository.save(employee);
        Employee found = employeeRepository.findById(employee.getId()).orElse(null);

        // then
        assertThat(found).isNotNull();
        assertThat(found.getEmail()).isEqualTo("mouad@example.com");
    }
}
