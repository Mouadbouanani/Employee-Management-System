package com.employeemanagementsystem.service;

import com.employeemanagementsystem.model.Employee;
import com.employeemanagementsystem.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Test
    void getAllEmployees_shouldReturnList() {
        List<Employee> employees = Arrays.asList(new Employee(), new Employee());
        when(employeeRepository.findAll()).thenReturn(employees);

        List<Employee> result = employeeService.getAllEmployees();

        assertEquals(2, result.size());
    }

    @Test
    void getEmployeeById_shouldReturnEmployee_whenExists() {
        Employee employee = new Employee();
        employee.setId(1L);
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        Optional<Employee> result = employeeService.getEmployeeById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void getEmployeeById_shouldReturnEmpty_whenNotExists() {
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Employee> result = employeeService.getEmployeeById(99L);

        assertFalse(result.isPresent());
    }

    @Test
    void saveEmployee_shouldEncodePassword_whenNewPassword() {
        Employee emp = new Employee();
        emp.setPassword("plainpass");

        when(passwordEncoder.encode("plainpass")).thenReturn("hashedpass");
        when(employeeRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Employee saved = employeeService.saveEmployee(emp);

        assertEquals("hashedpass", saved.getPassword());
    }

    @Test
    void saveEmployee_shouldKeepOldPassword_whenNoNewPassword() {
        Employee emp = new Employee();
        emp.setId(1L); // simulate update
        emp.setPassword(""); // no new password

        Employee existing = new Employee();
        existing.setPassword("oldPass");

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(employeeRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Employee saved = employeeService.saveEmployee(emp);

        assertEquals("oldPass", saved.getPassword());
    }

    @Test
    void deleteEmployee_shouldCallDeleteById() {
        employeeService.deleteEmployee(1L);

        verify(employeeRepository).deleteById(1L);
    }

    @Test
    void existsByEmail_shouldReturnTrue_whenEmailExists() {
        when(employeeRepository.existsByEmail("test@example.com")).thenReturn(true);

        boolean result = employeeService.existsByEmail("test@example.com");

        assertTrue(result);
    }

    @Test
    void getEmployeesPage_shouldReturnPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Employee> page = new PageImpl<>(List.of(new Employee()));

        when(employeeRepository.findAll(pageable)).thenReturn(page);

        Page<Employee> result = employeeService.getEmployeesPage(pageable);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void searchEmployees_shouldReturnPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Employee> page = new PageImpl<>(List.of(new Employee()));

        when(employeeRepository.searchEmployees("user", pageable)).thenReturn(page);

        Page<Employee> result = employeeService.searchEmployees("user", pageable);

        assertEquals(1, result.getTotalElements());
    }
}
