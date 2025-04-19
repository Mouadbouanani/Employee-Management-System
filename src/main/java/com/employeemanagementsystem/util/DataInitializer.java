package com.employeemanagementsystem.util;

import com.employeemanagementsystem.model.Employee;
import com.employeemanagementsystem.model.Role;
import com.employeemanagementsystem.repository.EmployeeRepository;
import com.employeemanagementsystem.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final RoleRepository roleRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    @Transactional
    public void init() {
        if (roleRepository.count() == 0) {
            createRoles();
        }

        if (employeeRepository.count() == 0) {
            createUsers();
        }
    }

    private void createRoles() {
        List<Role> roles = Arrays.asList(
                new Role("ADMIN"),
                new Role("MANAGER"),
                new Role("USER")
        );

        roleRepository.saveAll(roles);
    }

    private void createUsers() {
        Role adminRole = roleRepository.findByName("ADMIN").orElseThrow();
        Role managerRole = roleRepository.findByName("MANAGER").orElseThrow();
        Role userRole = roleRepository.findByName("USER").orElseThrow();

        // Admin user
        Employee admin = new Employee();
        admin.setFirstName("Admin");
        admin.setLastName("User");
        admin.setEmail("admin@company.com");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setPhoneNumber("123-456-7890");
        admin.setHireDate(LocalDate.now().minusYears(2));
        admin.setPosition("System Administrator");
        admin.setDepartment("IT");
        admin.setSalary(new BigDecimal("90000.00"));
        admin.addRole(adminRole);



    }
    }