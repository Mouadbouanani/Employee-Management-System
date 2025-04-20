package com.employeemanagementsystem.service;

import com.employeemanagementsystem.model.Employee;
import com.employeemanagementsystem.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setEmail("john.doe@example.com");
    }

    // Test 1: Récupérer tous les employés
    @Test
    void getAllEmployees_shouldReturnAllEmployees() {
        // Arrange
        when(employeeRepository.findAll()).thenReturn(Arrays.asList(employee));

        // Act
        List<Employee> employees = employeeService.getAllEmployees();

        // Assert
        assertEquals(1, employees.size());
        assertEquals("John", employees.get(0).getFirstName());
        verify(employeeRepository).findAll();
    }

    // Test 2: Récupération paginée
    @Test
    void getEmployeesPage_shouldReturnPagedResults() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<Employee> page = new PageImpl<>(List.of(employee));
        when(employeeRepository.findAll(pageable)).thenReturn(page);

        // Act
        Page<Employee> result = employeeService.getEmployeesPage(pageable);

        // Assert
        assertEquals(1, result.getTotalElements());
        verify(employeeRepository).findAll(pageable);
    }

    // Test 3: Recherche d'employés
    @Test
    void searchEmployees_shouldReturnPage() {
        // Simuler la recherche d'employés avec un mot-clé
        Page<Employee> page = mock(Page.class);
        when(employeeRepository.searchEmployees("Doe", PageRequest.of(0, 10))).thenReturn(page);

        // Appeler la méthode à tester
        Page<Employee> result = employeeService.searchEmployees("Doe", PageRequest.of(0, 10));

        // Vérification
        assertNotNull(result);
        verify(employeeRepository, times(1)).searchEmployees("Doe", PageRequest.of(0, 10));
    }

    // Test 4: Trouver un employé par ID (existant)
    @Test
    void getEmployeeById_shouldReturnEmployeeWhenExists() {
        // Arrange
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        // Act
        Optional<Employee> found = employeeService.getEmployeeById(1L);

        // Assert
        assertTrue(found.isPresent());
        assertEquals("john.doe@example.com", found.get().getEmail());
    }

    // Test 5: Trouver un employé par ID (inexistant)
    @Test
    void getEmployeeById_shouldReturnEmptyWhenNotExists() {
        // Arrange
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        Optional<Employee> found = employeeService.getEmployeeById(99L);

        // Assert
        assertFalse(found.isPresent());
    }

    // Test 6: Sauvegarder un employé
    @Test
    void saveEmployee_shouldPersistEmployee() {
        // Simuler un employé existant
        Employee existingEmployee = new Employee();
        existingEmployee.setId(1L);
        existingEmployee.setEmail("existing@example.com");
        existingEmployee.setFirstName("John");
        existingEmployee.setLastName("Doe");

        // Simuler le comportement du repository
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(existingEmployee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(existingEmployee);

        // Créer un nouvel employé avec l'ID existant
        Employee employeeToSave = new Employee();
        employeeToSave.setId(1L);
        employeeToSave.setEmail("existing@example.com");
        employeeToSave.setFirstName("John");
        employeeToSave.setLastName("Doe");

        // Appeler la méthode de service pour sauvegarder
        Employee savedEmployee = employeeService.saveEmployee(employeeToSave);

        // Vérifier que l'employé a été sauvegardé
        assertNotNull(savedEmployee);
        assertEquals("existing@example.com", savedEmployee.getEmail());
        verify(employeeRepository, times(1)).save(employeeToSave);
    }

    // Test 7: Supprimer un employé
    @Test
    void deleteEmployee_shouldCallRepository() {
        // Arrange - Pas de when nécessaire pour void methods

        // Act
        employeeService.deleteEmployee(1L);

        // Assert
        verify(employeeRepository).deleteById(1L);
    }

    // Test 8: Vérifier l'existence par email (existant)
    @Test
    void existsByEmail_shouldReturnTrueWhenEmailExists() {
        // Arrange
        when(employeeRepository.existsByEmail("john.doe@example.com")).thenReturn(true);

        // Act
        boolean exists = employeeService.existsByEmail("john.doe@example.com");

        // Assert
        assertTrue(exists);
    }

    // Test 9: Vérifier l'existence par email (inexistant)
    @Test
    void existsByEmail_shouldReturnFalseWhenEmailNotExists() {
        // Arrange
        when(employeeRepository.existsByEmail("unknown@example.com")).thenReturn(false);

        // Act
        boolean exists = employeeService.existsByEmail("unknown@example.com");

        // Assert
        assertFalse(exists);
    }

    // Test 10: Trouver un employé par email
    @Test
    void getEmployeeByEmail_shouldReturnEmployee() {
        // Arrange
        when(employeeRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(employee));

        // Act
        Optional<Employee> found = employeeService.getEmployeeByEmail("john.doe@example.com");

        // Assert
        assertTrue(found.isPresent());
        assertEquals(1L, found.get().getId());
    }
}