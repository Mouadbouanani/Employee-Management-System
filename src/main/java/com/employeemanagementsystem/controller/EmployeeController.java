package com.employeemanagementsystem.controller;

import com.employeemanagementsystem.model.Employee;
import com.employeemanagementsystem.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    public String listEmployees(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir,
            Model model) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortField);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Employee> employeePage;
        if (keyword != null && !keyword.isEmpty()) {
            employeePage = employeeService.searchEmployees(keyword, pageable);
            model.addAttribute("keyword", keyword);
        } else {
            employeePage = employeeService.getEmployeesPage(pageable);
        }

        model.addAttribute("employeePage", employeePage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", employeePage.getTotalPages());
        model.addAttribute("totalItems", employeePage.getTotalElements());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        return "employees/list";
    }

    @GetMapping("/{id}")
    public String viewEmployee(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Employee employee = employeeService.getEmployeeById(id)
                .orElse(null);

        if (employee == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Employee not found");
            return "redirect:/employees";
        }

        model.addAttribute("employee", employee);
        return "employees/view";
    }

    @GetMapping("/add")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public String showAddForm(Model model) {
        model.addAttribute("employee", new Employee());
        return "employees/add";
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public String addEmployee(@Valid @ModelAttribute("employee") Employee employee,
                              BindingResult result, RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "employees/add";
        }

        if (employeeService.existsByEmail(employee.getEmail())) {
            result.rejectValue("email", "error.employee", "An employee with this email already exists");
            return "employees/add";
        }

        employeeService.saveEmployee(employee);
        redirectAttributes.addFlashAttribute("successMessage", "Employee added successfully");
        return "redirect:/employees";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Employee employee = employeeService.getEmployeeById(id)
                .orElse(null);

        if (employee == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Employee not found");
            return "redirect:/employees";
        }

        model.addAttribute("employee", employee);
        return "employees/edit";
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public String updateEmployee(@PathVariable Long id,
                                 @Valid @ModelAttribute("employee") Employee employee,
                                 BindingResult result, RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            employee.setId(id);
            return "employees/edit";
        }

        // Check if email exists but belongs to a different employee
        employeeService.getEmployeeByEmail(employee.getEmail())
                .ifPresent(existingEmployee -> {
                    if (!existingEmployee.getId().equals(id)) {
                        result.rejectValue("email", "error.employee", "This email is already in use");
                    }
                });

        if (result.hasErrors()) {
            employee.setId(id);
            return "employees/edit";
        }

        employee.setId(id);  // Ensure the ID is set correctly
        employeeService.saveEmployee(employee);
        redirectAttributes.addFlashAttribute("successMessage", "Employee updated successfully");
        return "redirect:/employees";
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteEmployee(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (!employeeService.getEmployeeById(id).isPresent()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Employee not found");
            return "redirect:/employees";
        }

        employeeService.deleteEmployee(id);
        redirectAttributes.addFlashAttribute("successMessage", "Employee deleted successfully");
        return "redirect:/employees";
    }
}
