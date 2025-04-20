package com.employeemanagementsystem.service;

import com.employeemanagementsystem.model.Employee;
import com.employeemanagementsystem.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional  // Cette annotation permet de rollback automatiquement les transactions après chaque test
class EmployeeServiceImplIT {

    @Autowired
    private EmployeeServiceImpl employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    void setUp() {
        // Suppression des données existantes pour chaque test (si nécessaire)
        employeeRepository.deleteAll();
    }

    @Test
    void testSaveEmployee_shouldSaveToDatabase() {
        Employee emp = new Employee();
        emp.setFirstName("John");
        emp.setLastName("Doe");
        emp.setEmail("john.doe@example.com");
        emp.setPassword("plainPassword");

        Employee savedEmployee = employeeService.saveEmployee(emp);

        assertNotNull(savedEmployee.getId());  // Vérifier que l'ID est généré
        assertEquals("John", savedEmployee.getFirstName());
        assertEquals("Doe", savedEmployee.getLastName());
        assertEquals("john.doe@example.com", savedEmployee.getEmail());
    }

    @Test
    void testGetEmployeeById_shouldReturnEmployeeFromDatabase() {
        // Ajouter un employé dans la base de données
        Employee emp = new Employee();
        emp.setFirstName("Jane");
        emp.setLastName("Doe");
        emp.setEmail("jane.doe@example.com");
        emp.setPassword("password");

        Employee savedEmployee = employeeRepository.save(emp);

        // Tester la récupération de l'employé par ID
        Employee foundEmployee = employeeService.getEmployeeById(savedEmployee.getId()).orElse(null);

        assertNotNull(foundEmployee);
        assertEquals(savedEmployee.getId(), foundEmployee.getId());
        assertEquals(savedEmployee.getEmail(), foundEmployee.getEmail());
    }

    @Test
    void testDeleteEmployee_shouldRemoveEmployeeFromDatabase() {
        // Ajouter un employé dans la base de données
        Employee emp = new Employee();
        emp.setFirstName("Sam");
        emp.setLastName("Smith");
        emp.setEmail("sam.smith@example.com");
        emp.setPassword("password");

        Employee savedEmployee = employeeRepository.save(emp);

        // Supprimer l'employé
        employeeService.deleteEmployee(savedEmployee.getId());

        // Vérifier que l'employé est bien supprimé
        assertFalse(employeeRepository.existsById(savedEmployee.getId()));
    }
}
