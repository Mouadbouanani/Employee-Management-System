package com.employeemanagementsystem.config;

import com.employeemanagementsystem.model.Employee;
import com.employeemanagementsystem.model.Role;
import com.employeemanagementsystem.repository.EmployeeRepository;
import com.employeemanagementsystem.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Check if we already have users
        if (employeeRepository.count() == 0) {
            createRoles();
            createAdminUser();
        }
    }

    private void createRoles() {
        // Check if roles exist
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role(null, "ADMIN"));
            roleRepository.save(new Role(null, "MANAGER"));
            roleRepository.save(new Role(null, "USER"));
        }
    }

    private void createAdminUser() {
        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseThrow(() -> new RuntimeException("Admin role not found"));

        Set<Role> roles = new HashSet<>();
        roles.add(adminRole);

        Employee admin = new Employee();
        admin.setFirstName("Admin");
        admin.setLastName("User");
        admin.setEmail("admin@company.com");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setPhoneNumber("123-456-7890");
        admin.setHireDate(LocalDate.of(2021, 4, 19));
        admin.setPosition("System Administrator");
        admin.setDepartment("IT");
        admin.setSalary(new BigDecimal("90000.00"));
        admin.setRoles(roles);

        employeeRepository.save(admin);
        System.out.println("Admin user created successfully!");
    }
}