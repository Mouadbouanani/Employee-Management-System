package com.employeemanagementsystem.service;

import com.employeemanagementsystem.model.Employee;
import com.employeemanagementsystem.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties") // Utilisation d'un fichier de config pour la base de données de test (H2 ou autre)
public class EmployeeServiceIT {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee testEmployee;

    @BeforeEach
    public void setUp() {
        // Préparer un employé pour les tests
        testEmployee = new Employee();
        testEmployee.setEmail("test@example.com");
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("HR");
        testEmployee.setPosition("Manager");
        testEmployee.setPassword("password123");

        // Sauvegarder dans la base de données pour qu'il soit disponible pour les tests
        employeeRepository.save(testEmployee);
    }

    @Test
    public void testSaveEmployee() {
        // Créer un nouvel employé pour tester la sauvegarde
        Employee newEmployee = new Employee();
        newEmployee.setEmail("new@example.com");
        newEmployee.setFirstName("Jane");
        newEmployee.setLastName("Smith");
        newEmployee.setDepartment("Engineering");
        newEmployee.setPosition("Developer");
        newEmployee.setPassword("securePassword");

        // Sauvegarder l'employé
        Employee savedEmployee = employeeService.saveEmployee(newEmployee);

        // Vérifier que l'employé a bien été sauvegardé
        assertNotNull(savedEmployee.getId());
        assertEquals("new@example.com", savedEmployee.getEmail());
        assertEquals("Jane", savedEmployee.getFirstName());
    }

    @Test
    public void testGetEmployeeById() {
        // Récupérer l'employé par son ID
        Employee foundEmployee = employeeService.getEmployeeById(testEmployee.getId()).orElse(null);

        // Vérifier que l'employé a bien été retrouvé
        assertNotNull(foundEmployee);
        assertEquals(testEmployee.getEmail(), foundEmployee.getEmail());
        assertEquals(testEmployee.getFirstName(), foundEmployee.getFirstName());
    }

    @Test
    public void testDeleteEmployee() {
        // Supprimer un employé par son ID
        employeeService.deleteEmployee(testEmployee.getId());

        // Vérifier que l'employé a été supprimé
        assertFalse(employeeRepository.existsById(testEmployee.getId()));
    }

    @Test
    public void testExistsByEmail() {
        // Vérifier si un employé existe par son email
        boolean exists = employeeService.existsByEmail(testEmployee.getEmail());

        // Vérifier que l'employé existe
        assertTrue(exists);
    }

    @Test
    public void testSearchEmployees() {
        // Rechercher des employés par un mot-clé
        String keyword = "John";
        var pageable = org.springframework.data.domain.PageRequest.of(0, 10);
        var page = employeeService.searchEmployees(keyword, pageable);

        // Vérifier que le résultat de la recherche contient des employés correspondants
        assertTrue(page.getContent().size() > 0);
        assertTrue(page.getContent().get(0).getFirstName().contains(keyword));
    }
}
