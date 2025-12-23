-- Add role and skills columns to auth_users for worker support
ALTER TABLE auth_users
    ADD COLUMN IF NOT EXISTS role VARCHAR(50),
    ADD COLUMN IF NOT EXISTS skills TEXT;



