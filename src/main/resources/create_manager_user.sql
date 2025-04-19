USE employee_db;

-- Insérer le Manager
INSERT INTO employees (
    first_name, last_name, email, password, phone_number, hire_date,
    position, department, salary
) VALUES (
             'Manager', 'User', 'manager@company.com',
             '$2a$10$EXEMPLE_HASH_MANAGER',  -- Remplace avec le vrai hash
             '123-456-7890',
             DATE_SUB(CURDATE(), INTERVAL 2 YEAR),
             'System Manager', 'IT', 45000.00
         );

-- Insérer le Regular User
INSERT INTO employees (
    first_name, last_name, email, password, phone_number, hire_date,
    position, department, salary
) VALUES (
             'Regular', 'User', 'user@company.com',
             '$2a$10$EXEMPLE_HASH_USER',  -- Remplace avec le vrai hash
             '123-456-7890',
             DATE_SUB(CURDATE(), INTERVAL 2 YEAR),
             'System Manager', 'IT', 45000.00
         );



INSERT INTO employee_roles (employee_id, role_id)
VALUES
    ((SELECT id FROM employees WHERE email = 'manager@company.com'), 2),
    ((SELECT id FROM employees WHERE email = 'user@company.com'), 3);
