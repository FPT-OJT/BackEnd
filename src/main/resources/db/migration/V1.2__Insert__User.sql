-- =====================================================
-- Flyway Migration V1.2: Insert Users
-- Password: All users have password "P@sswd123.!" (BCrypt encoded)

INSERT INTO users (id, user_name, google_id, email, role, password, first_name, last_name, created_at, updated_at, deleted_at) VALUES
-- ADMIN User (1 user)
('a1b2c3d4-e5f6-4a7b-8c9d-0e1f2a3b4c5d', 'admin_system', NULL, 'admin@fpt.edu.vn', 'ADMIN', '$2a$10$vBw/9O7LrqJNEqUqPKWRuuUrGJqnMqQJj8IzQhjqDUqBgCjDJfZ5S', 'Admin', 'System', '2025-01-15 08:30:00', '2025-01-15 08:30:00', NULL),

-- CUSTOMER Users (19 users)
('e5f6a7b8-c9d0-4e1f-2a3b-4c5d6e7f8a9b', 'john_smith', NULL, 'john.smith@gmail.com', 'CUSTOMER', '$2a$10$vBw/9O7LrqJNEqUqPKWRuuUrGJqnMqQJj8IzQhjqDUqBgCjDJfZ5S', 'John', 'Smith', '2024-11-20 14:30:00', '2025-01-20 16:45:00', NULL),
('f6a7b8c9-d0e1-4f2a-3b4c-5d6e7f8a9b0c', 'sarah_johnson', NULL, 'sarah.johnson@outlook.com', 'CUSTOMER', '$2a$10$vBw/9O7LrqJNEqUqPKWRuuUrGJqnMqQJj8IzQhjqDUqBgCjDJfZ5S', 'Sarah', 'Johnson', '2024-12-01 09:00:00', '2025-01-21 10:30:00', NULL),
('a7b8c9d0-e1f2-4a3b-4c5d-6e7f8a9b0c1d', 'michael_brown', NULL, 'michael.brown@yahoo.com', 'CUSTOMER', '$2a$10$vBw/9O7LrqJNEqUqPKWRuuUrGJqnMqQJj8IzQhjqDUqBgCjDJfZ5S', 'Michael', 'Brown', '2024-12-05 13:15:00', '2025-01-22 08:20:00', NULL),
('b8c9d0e1-f2a3-4b4c-5d6e-7f8a9b0c1d2e', 'emily_davis', NULL, 'emily.davis@gmail.com', 'CUSTOMER', '$2a$10$vBw/9O7LrqJNEqUqPKWRuuUrGJqnMqQJj8IzQhjqDUqBgCjDJfZ5S', 'Emily', 'Davis', '2024-12-10 16:45:00', '2025-01-23 14:00:00', NULL),
('c9d0e1f2-a3b4-4c5d-6e7f-8a9b0c1d2e3f', 'david_wilson', NULL, 'david.wilson@gmail.com', 'CUSTOMER', '$2a$10$vBw/9O7LrqJNEqUqPKWRuuUrGJqnMqQJj8IzQhjqDUqBgCjDJfZ5S', 'David', 'Wilson', '2024-12-15 11:00:00', '2025-01-24 09:30:00', NULL),
('d0e1f2a3-b4c5-4d6e-7f8a-9b0c1d2e3f4a', 'jessica_moore', NULL, 'jessica.moore@outlook.com', 'CUSTOMER', '$2a$10$vBw/9O7LrqJNEqUqPKWRuuUrGJqnMqQJj8IzQhjqDUqBgCjDJfZ5S', 'Jessica', 'Moore', '2024-12-18 08:30:00', '2025-01-25 11:15:00', NULL),
('e1f2a3b4-c5d6-4e7f-8a9b-0c1d2e3f4a5b', 'james_taylor', NULL, 'james.taylor@gmail.com', 'CUSTOMER', '$2a$10$vBw/9O7LrqJNEqUqPKWRuuUrGJqnMqQJj8IzQhjqDUqBgCjDJfZ5S', 'James', 'Taylor', '2024-12-20 15:20:00', '2025-01-26 13:40:00', NULL),
('f2a3b4c5-d6e7-4f8a-9b0c-1d2e3f4a5b6c', 'ashley_anderson', NULL, 'ashley.anderson@yahoo.com', 'CUSTOMER', '$2a$10$vBw/9O7LrqJNEqUqPKWRuuUrGJqnMqQJj8IzQhjqDUqBgCjDJfZ5S', 'Ashley', 'Anderson', '2024-12-22 10:10:00', '2025-01-27 15:25:00', NULL),
('a3b4c5d6-e7f8-4a9b-0c1d-2e3f4a5b6c7d', 'robert_thomas', NULL, 'robert.thomas@gmail.com', 'CUSTOMER', '$2a$10$vBw/9O7LrqJNEqUqPKWRuuUrGJqnMqQJj8IzQhjqDUqBgCjDJfZ5S', 'Robert', 'Thomas', '2025-01-02 12:00:00', '2025-01-27 10:00:00', NULL),
('b4c5d6e7-f8a9-4b0c-1d2e-3f4a5b6c7d8e', 'amanda_jackson', NULL, 'amanda.jackson@outlook.com', 'CUSTOMER', '$2a$10$vBw/9O7LrqJNEqUqPKWRuuUrGJqnMqQJj8IzQhjqDUqBgCjDJfZ5S', 'Amanda', 'Jackson', '2025-01-05 14:35:00', '2025-01-27 16:50:00', NULL),
('c5d6e7f8-a9b0-4c1d-2e3f-4a5b6c7d8e9f', 'christopher_white', NULL, 'christopher.white@gmail.com', 'CUSTOMER', '$2a$10$vBw/9O7LrqJNEqUqPKWRuuUrGJqnMqQJj8IzQhjqDUqBgCjDJfZ5S', 'Christopher', 'White', '2025-01-08 09:45:00', '2025-01-28 08:10:00', NULL),
('d6e7f8a9-b0c1-4d2e-3f4a-5b6c7d8e9f0a', 'melissa_harris', NULL, 'melissa.harris@yahoo.com', 'CUSTOMER', '$2a$10$vBw/9O7LrqJNEqUqPKWRuuUrGJqnMqQJj8IzQhjqDUqBgCjDJfZ5S', 'Melissa', 'Harris', '2025-01-10 11:30:00', '2025-01-28 09:20:00', NULL),
('e7f8a9b0-c1d2-4e3f-4a5b-6c7d8e9f0a1b', 'daniel_martin', NULL, 'daniel.martin@gmail.com', 'CUSTOMER', '$2a$10$vBw/9O7LrqJNEqUqPKWRuuUrGJqnMqQJj8IzQhjqDUqBgCjDJfZ5S', 'Daniel', 'Martin', '2025-01-12 13:15:00', '2025-01-28 11:35:00', NULL),
('f8a9b0c1-d2e3-4f4a-5b6c-7d8e9f0a1b2c', 'jennifer_garcia', NULL, 'jennifer.garcia@outlook.com', 'CUSTOMER', '$2a$10$vBw/9O7LrqJNEqUqPKWRuuUrGJqnMqQJj8IzQhjqDUqBgCjDJfZ5S', 'Jennifer', 'Garcia', '2025-01-14 15:50:00', '2025-01-28 12:45:00', NULL),
('a9b0c1d2-e3f4-4a5b-6c7d-8e9f0a1b2c3d', 'matthew_martinez', NULL, 'matthew.martinez@gmail.com', 'CUSTOMER', '$2a$10$vBw/9O7LrqJNEqUqPKWRuuUrGJqnMqQJj8IzQhjqDUqBgCjDJfZ5S', 'Matthew', 'Martinez', '2025-01-16 08:20:00', '2025-01-28 14:00:00', NULL),
('b0c1d2e3-f4a5-4b6c-7d8e-9f0a1b2c3d4e', 'stephanie_robinson', NULL, 'stephanie.robinson@yahoo.com', 'CUSTOMER', '$2a$10$vBw/9O7LrqJNEqUqPKWRuuUrGJqnMqQJj8IzQhjqDUqBgCjDJfZ5S', 'Stephanie', 'Robinson', '2025-01-18 10:40:00', '2025-01-28 15:30:00', NULL),
('c1d2e3f4-a5b6-4c7d-8e9f-0a1b2c3d4e5f', 'anthony_clark', NULL, 'anthony.clark@gmail.com', 'CUSTOMER', '$2a$10$vBw/9O7LrqJNEqUqPKWRuuUrGJqnMqQJj8IzQhjqDUqBgCjDJfZ5S', 'Anthony', 'Clark', '2025-01-20 09:15:00', '2025-01-28 16:20:00', NULL),
('d2e3f4a5-b6c7-4d8e-9f0a-1b2c3d4e5f6a', 'nicole_rodriguez', NULL, 'nicole.rodriguez@outlook.com', 'CUSTOMER', '$2a$10$vBw/9O7LrqJNEqUqPKWRuuUrGJqnMqQJj8IzQhjqDUqBgCjDJfZ5S', 'Nicole', 'Rodriguez', '2025-01-22 11:30:00', '2025-01-28 17:10:00', NULL),
('e3f4a5b6-c7d8-4e9f-0a1b-2c3d4e5f6a7b', 'william_lewis', NULL, 'william.lewis@gmail.com', 'CUSTOMER', '$2a$10$vBw/9O7LrqJNEqUqPKWRuuUrGJqnMqQJj8IzQhjqDUqBgCjDJfZ5S', 'William', 'Lewis', '2025-01-24 14:45:00', '2025-01-28 18:00:00', NULL);