package com.employeemanagementsystem.config;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@Configuration
@EnableWebSecurity
public class SecurityTestConfig {
    @Autowired
    private MockMvc mockMvc;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // Allow all requests during tests
                );
        return http.build();
    }
    @Test
    public void testSecuredEndpointWithoutAuth() throws Exception {
        mockMvc.perform(get("/employees/add"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testUserCannotAccessAdminEndpoint() throws Exception {
        mockMvc.perform(get("/employees/delete/1"))
                .andExpect(status().isForbidden());
    }
}