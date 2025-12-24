-- V13: Setup admin user and update existing user roles
-- This migration:
-- 1. Updates existing users to have USER role (if role is NULL)
-- 2. Creates default admin user for system administration

-- Update existing users to have USER role if they don't have one
UPDATE auth_users 
SET role = 'USER' 
WHERE role IS NULL OR role = '';

-- Create admin user
-- Username: admin
-- Password: admin123 (BCrypt hash)
INSERT INTO auth_users (
    email, 
    username, 
    password, 
    role,
    enabled, 
    account_non_expired,
    account_non_locked, 
    credentials_non_expired,
    first_name,
    last_name,
    created_at, 
    updated_at
) 
SELECT 
    'admin@dth.com',
    'admin',
    '$2a$10$iQ0lg8pNob9zP/WetLeVHeJVE0pGWCv/WdREVpShccofDlDpSIQz2',
    'ADMIN',
    true, 
    true, 
    true, 
    true,
    'Admin',
    'User',
    NOW(), 
    NOW()
WHERE NOT EXISTS (
    SELECT 1 FROM auth_users WHERE username = 'admin'
);
