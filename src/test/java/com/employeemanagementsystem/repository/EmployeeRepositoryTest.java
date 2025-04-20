package com.employeemanagementsystem.repository;

import com.employeemanagementsystem.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class EmployeeRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    public void setUp() {
        // Initialiser un employé avec le constructeur classique
        employee = new Employee();
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setEmail("john.doe@example.com");
        employee.setDepartment("IT");
        employee.setPosition("Developer");
        employee.setPassword("password123");
        employee.setSalary(BigDecimal.valueOf(50000));
        employee.setHireDate(LocalDate.of(2020, 1, 1));
        employee.setPhoneNumber("123-456-7890");

        entityManager.persistAndFlush(employee);
    }

    @Test
    public void whenFindByExistingEmail_thenReturnEmployee() {
        Optional<Employee> found = employeeRepository.findByEmail("john.doe@example.com");

        assertTrue(found.isPresent());
        assertEquals("John", found.get().getFirstName());
    }

    @Test
    public void whenFindByNonExistingEmail_thenReturnEmpty() {
        Optional<Employee> found = employeeRepository.findByEmail("nonexistent@example.com");

        assertFalse(found.isPresent());
    }

    @Test
    public void whenExistsByExistingEmail_thenReturnTrue() {
        boolean exists = employeeRepository.existsByEmail("john.doe@example.com");
        assertTrue(exists);
    }

    @Test
    public void whenSearchByFirstName_thenReturnEmployees() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Employee> result = employeeRepository.searchEmployees("John", pageable);

        assertFalse(result.isEmpty());
        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void whenSearchByNonExistingKeyword_thenReturnEmpty() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Employee> result = employeeRepository.searchEmployees("Nonexistent", pageable);

        assertTrue(result.isEmpty());
    }
}