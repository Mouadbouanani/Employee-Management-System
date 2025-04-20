package com.employeemanagementsystem.controller;

import com.employeemanagementsystem.config.SecurityTestConfig;
import com.employeemanagementsystem.model.Employee;
import com.employeemanagementsystem.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = EmployeeController.class)
@Import(SecurityTestConfig.class)
class EmployeeIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private EmployeeService employeeService;

    private Employee testEmployee;

    @BeforeEach
    void setUp() {
        // Initialize test data
        testEmployee = new Employee();
        testEmployee.setId(1L);
        testEmployee.setFirstName("mouad");
        testEmployee.setLastName("bouanani");
        testEmployee.setEmail("mouad@example.com");

        // Reinitialize mockMvc with security context
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(roles = "USER")
    void testListEmployees_ShouldReturnEmployeePage() throws Exception {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        List<Employee> employees = Collections.singletonList(testEmployee);
        Page<Employee> employeePage = new PageImpl<>(employees, pageable, employees.size());

        when(employeeService.getEmployeesPage(any(Pageable.class))).thenReturn(employeePage);

        // When/Then
        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(view().name("employees/list"))
                .andExpect(model().attributeExists("employeePage"))
                .andExpect(model().attribute("employeePage", employeePage))
                .andExpect(model().attribute("totalPages", 1))
                .andExpect(model().attribute("totalItems", 1L));

        verify(employeeService, times(1)).getEmployeesPage(any(Pageable.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testViewEmployeeFound_ShouldReturnEmployeeView() throws Exception {
        // Given
        when(employeeService.getEmployeeById(1L)).thenReturn(Optional.of(testEmployee));

        // When/Then
        mockMvc.perform(get("/employees/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("employees/view"))
                .andExpect(model().attributeExists("employee"))
                .andExpect(model().attribute("employee", testEmployee));

        verify(employeeService, times(1)).getEmployeeById(1L);
    }

    @Test
    @WithMockUser(roles = "USER")
    void testViewEmployeeNotFound_ShouldRedirectToList() throws Exception {
        // Given
        when(employeeService.getEmployeeById(1L)).thenReturn(Optional.empty());

        // When/Then
        mockMvc.perform(get("/employees/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/employees"))
                .andExpect(flash().attributeExists("errorMessage"));

        verify(employeeService, times(1)).getEmployeeById(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testAddEmployeeForm_ShouldReturnAddForm() throws Exception {
        mockMvc.perform(get("/employees/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("employees/add"))
                .andExpect(model().attributeExists("employee"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testAddEmployeeForm_WithoutAdminRole_ShouldBeForbidden() throws Exception {
        mockMvc.perform(get("/employees/add"))
                .andExpect(status().isForbidden());
    }
}













//package com.employeemanagementsystem.controller;
//
//import com.employeemanagementsystem.config.SecurityTestConfig;
//import com.employeemanagementsystem.model.Employee;
//import com.employeemanagementsystem.service.EmployeeService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.Import;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(controllers = EmployeeController.class)
//@Import(SecurityTestConfig.class) // Import your test security config
//class EmployeeIntegrationTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private EmployeeService employeeService;
//
//    @Test
//    @WithMockUser(roles = "USER")
//    void testListEmployees() throws Exception {
//        // Create a test employee
//        Employee employee = new Employee();
//        employee.setId(1L);
//        employee.setFirstName("mouad");
//        employee.setLastName("bouanani");
//        employee.setEmail("mouad@example.com");
//
//        List<Employee> employees = List.of(employee);
//        Page<Employee> employeePage = new PageImpl<>(employees);
//
//        // Mock the exact service call that will be made from the controller
//        when(employeeService.getEmployeesPage(any(Pageable.class)))
//                .thenReturn(employeePage);
//
//        // Perform the test
//        mockMvc.perform(get("/employees"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("employees/list"))
//                .andExpect(model().attributeExists("employeePage"))
//                .andExpect(model().attribute("totalPages", 1))
//                .andExpect(model().attribute("totalItems", 1L));
//
//        // Verify the service was called
//        verify(employeeService, times(1)).getEmployeesPage(any(Pageable.class));
//    }
//
//    @Test
//    @WithMockUser
//    void testViewEmployeeFound() throws Exception {
//        // Create a test employee with all required fields
//        Employee employee = new Employee();
//        employee.setId(1L);
//        employee.setFirstName("mouad");
//        employee.setLastName("bouanani");
//        employee.setEmail("bouanani@example.com");
//
//        when(employeeService.getEmployeeById(1L)).thenReturn(Optional.of(employee));
//
//        mockMvc.perform(get("/employees/1"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("employees/view"))
//                .andExpect(model().attributeExists("employee"))
//                .andExpect(model().attribute("employee", employee));
//    }
//
//    @Test
//    @WithMockUser // Add mock user for authentication
//    void testViewEmployeeNotFound() throws Exception {
//        when(employeeService.getEmployeeById(1L)).thenReturn(Optional.empty());
//
//        mockMvc.perform(get("/employees/1"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/employees"));
//    }
//}