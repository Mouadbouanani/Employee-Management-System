package com.employeemanagementsystem.controller;

import com.employeemanagementsystem.model.Employee;
import com.employeemanagementsystem.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {

    @Mock
    private EmployeeService employeeService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private EmployeeController employeeController;

    private Employee testEmployee;

    @BeforeEach
    void setUp() {
        testEmployee = new Employee();
        testEmployee.setId(1L);
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setEmail("john.doe@example.com");
    }

    @Test
    void listEmployees_shouldReturnViewWithEmployees() {
        // Arrange
        Page<Employee> employeePage = new PageImpl<>(List.of(testEmployee));
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));

        when(employeeService.getEmployeesPage(pageable)).thenReturn(employeePage);

        // Act
        String viewName = employeeController.listEmployees(null, 0, 10, "id", "asc", model);

        // Assert
        assertEquals("employees/list", viewName);
        verify(model).addAttribute("employeePage", employeePage);
        verify(model).addAttribute("currentPage", 0);
        verify(model).addAttribute("totalPages", 1);
        verify(model).addAttribute("totalItems", 1L);
        verify(model).addAttribute("sortField", "id");
        verify(model).addAttribute("sortDir", "asc");
        verify(model).addAttribute("reverseSortDir", "desc");
    }

    @Test
    void listEmployees_withKeyword_shouldReturnViewWithFilteredEmployees() {
        // Arrange
        String keyword = "John";
        Page<Employee> employeePage = new PageImpl<>(List.of(testEmployee));
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));

        when(employeeService.searchEmployees(keyword, pageable)).thenReturn(employeePage);

        // Act
        String viewName = employeeController.listEmployees(keyword, 0, 10, "id", "asc", model);

        // Assert
        assertEquals("employees/list", viewName);
        verify(model).addAttribute("employeePage", employeePage);
        verify(model).addAttribute("keyword", keyword);
    }

    @Test
    void viewEmployee_withValidId_shouldReturnViewWithEmployee() {
        // Arrange
        when(employeeService.getEmployeeById(1L)).thenReturn(Optional.of(testEmployee));

        // Act
        String viewName = employeeController.viewEmployee(1L, model, redirectAttributes);

        // Assert
        assertEquals("employees/view", viewName);
        verify(model).addAttribute("employee", testEmployee);
        verifyNoInteractions(redirectAttributes);
    }

    @Test
    void viewEmployee_withInvalidId_shouldRedirectWithErrorMessage() {
        // Arrange
        when(employeeService.getEmployeeById(99L)).thenReturn(Optional.empty());

        // Act
        String viewName = employeeController.viewEmployee(99L, model, redirectAttributes);

        // Assert
        assertEquals("redirect:/employees", viewName);
        verify(redirectAttributes).addFlashAttribute("errorMessage", "Employee not found");
        verifyNoMoreInteractions(model);
    }

    @Test
    @WithMockUser(roles = {"MANAGER"})
    void showAddForm_shouldReturnAddForm() {
        // Act
        String viewName = employeeController.showAddForm(model);

        // Assert
        assertEquals("employees/add", viewName);
        verify(model).addAttribute(eq("employee"), any(Employee.class));
    }

    @Test
    @WithMockUser(roles = {"MANAGER"})
    void addEmployee_withValidEmployee_shouldSaveAndRedirect() {
        // Arrange
        when(bindingResult.hasErrors()).thenReturn(false);
        when(employeeService.existsByEmail(testEmployee.getEmail())).thenReturn(false);

        // Act
        String viewName = employeeController.addEmployee(testEmployee, bindingResult, redirectAttributes);

        // Assert
        assertEquals("redirect:/employees", viewName);
        verify(employeeService).saveEmployee(testEmployee);
        verify(redirectAttributes).addFlashAttribute("successMessage", "Employee added successfully");
    }

    @Test
    @WithMockUser(roles = {"MANAGER"})
    void addEmployee_withExistingEmail_shouldReturnToForm() {
        // Arrange
        when(bindingResult.hasErrors()).thenReturn(false);
        when(employeeService.existsByEmail(testEmployee.getEmail())).thenReturn(true);

        // Act
        String viewName = employeeController.addEmployee(testEmployee, bindingResult, redirectAttributes);

        // Assert
        assertEquals("employees/add", viewName);
        verify(bindingResult).rejectValue("email", "error.employee", "An employee with this email already exists");
        verifyNoInteractions(redirectAttributes);
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void showEditForm_withValidId_shouldReturnEditForm() {
        // Arrange
        when(employeeService.getEmployeeById(1L)).thenReturn(Optional.of(testEmployee));

        // Act
        String viewName = employeeController.showEditForm(1L, model, redirectAttributes);

        // Assert
        assertEquals("employees/edit", viewName);
        verify(model).addAttribute("employee", testEmployee);
        verifyNoInteractions(redirectAttributes);
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void updateEmployee_withValidData_shouldUpdateAndRedirect() {
        // Arrange
        when(bindingResult.hasErrors()).thenReturn(false);
        when(employeeService.getEmployeeByEmail(testEmployee.getEmail())).thenReturn(Optional.empty());

        // Act
        String viewName = employeeController.updateEmployee(1L, testEmployee, bindingResult, redirectAttributes);

        // Assert
        assertEquals("redirect:/employees", viewName);
        verify(employeeService).saveEmployee(testEmployee);
        verify(redirectAttributes).addFlashAttribute("successMessage", "Employee updated successfully");
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void updateEmployee_withEmailConflict_shouldReturnToForm() {
        // Arrange
        Employee existingEmployee = new Employee();
        existingEmployee.setId(2L); // ID différent de l'employee test (1L)
        existingEmployee.setEmail(testEmployee.getEmail());

        when(bindingResult.hasErrors()).thenReturn(false);
        when(employeeService.getEmployeeByEmail(testEmployee.getEmail()))
                .thenReturn(Optional.of(existingEmployee));

        // Act
        String viewName = employeeController.updateEmployee(
                1L, testEmployee, bindingResult, redirectAttributes);

        // Assert
        assertEquals("employees/edit", viewName);
        verify(bindingResult).rejectValue("email", "error.employee", "This email is already in use");
        verify(employeeService, never()).saveEmployee(any());
        verifyNoInteractions(redirectAttributes);
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void deleteEmployee_withValidId_shouldDeleteAndRedirect() {
        // Arrange
        when(employeeService.getEmployeeById(1L)).thenReturn(Optional.of(testEmployee));

        // Act
        String viewName = employeeController.deleteEmployee(1L, redirectAttributes);

        // Assert
        assertEquals("redirect:/employees", viewName);
        verify(employeeService).deleteEmployee(1L);
        verify(redirectAttributes).addFlashAttribute("successMessage", "Employee deleted successfully");
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void deleteEmployee_withInvalidId_shouldRedirectWithErrorMessage() {
        // Arrange
        when(employeeService.getEmployeeById(99L)).thenReturn(Optional.empty());

        // Act
        String viewName = employeeController.deleteEmployee(99L, redirectAttributes);

        // Assert
        assertEquals("redirect:/employees", viewName);
        verify(redirectAttributes).addFlashAttribute("errorMessage", "Employee not found");
        verify(employeeService, never()).deleteEmployee(anyLong());
    }
}