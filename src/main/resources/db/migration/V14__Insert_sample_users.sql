-- V15: Insert sample user accounts for demo/testing
-- Password for all users: User@123
-- BCrypt hash: $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy

-- Insert sample USER accounts
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
    phone,
    created_at,
    updated_at
)
SELECT * FROM (
    SELECT
        'nguyen.van.a@example.com' as email,
        'nguyenvana' as username,
        '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy' as password,
        'USER' as role,
        true as enabled,
        true as account_non_expired,
        true as account_non_locked,
        true as credentials_non_expired,
        'Văn A' as first_name,
        'Nguyễn' as last_name,
        '0901234567' as phone,
        NOW() as created_at,
        NOW() as updated_at
    UNION ALL
    SELECT
        'tran.thi.b@example.com',
        'tranthib',
        '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        'USER',
        true,
        true,
        true,
        true,
        'Thị B',
        'Trần',
        '0902345678',
        NOW(),
        NOW()
    UNION ALL
    SELECT
        'le.van.c@example.com',
        'levanc',
        '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        'USER',
        true,
        true,
        true,
        true,
        'Văn C',
        'Lê',
        '0903456789',
        NOW(),
        NOW()
    UNION ALL
    SELECT
        'pham.thi.d@example.com',
        'phamthid',
        '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        'USER',
        true,
        true,
        true,
        true,
        'Thị D',
        'Phạm',
        '0904567890',
        NOW(),
        NOW()
    UNION ALL
    SELECT
        'hoang.van.e@example.com',
        'hoangvane',
        '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        'USER',
        true,
        true,
        true,
        true,
        'Văn E',
        'Hoàng',
        '0905678901',
        NOW(),
        NOW()
) AS tmp
WHERE NOT EXISTS (
    SELECT 1 FROM auth_users WHERE username = tmp.username
);
