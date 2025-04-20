package com.employeemanagementsystem.service;

import com.employeemanagementsystem.model.Employee;
import com.employeemanagementsystem.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    public EmployeeServiceImplTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveEmployeeWithPasswordEncoding() {
        Employee employee = new Employee();
        employee.setPassword("plainPassword");

        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        Employee saved = employeeService.saveEmployee(employee);

        assertEquals("encodedPassword", saved.getPassword());
        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    void testGetEmployeeById() {
        Employee employee = new Employee();
        employee.setId(1L);

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        Optional<Employee> result = employeeService.getEmployeeById(1L);
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void testExistsByEmail() {
        when(employeeRepository.existsByEmail("admin@company.com")).thenReturn(true);
        assertTrue(employeeService.existsByEmail("admin@company.com"));
    }
}
