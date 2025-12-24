-- V14: Insert sample orders for demo/testing
-- This migration creates sample orders with different statuses

-- Insert sample orders using the first USER role account
-- Order 1: Completed order
INSERT INTO orders_orders (
    user_id,
    service_id,
    status,
    total_amount,
    notes,
    created_at,
    updated_at
)
SELECT 
    (SELECT id FROM auth_users WHERE role = 'USER' ORDER BY id LIMIT 1),
    1,
    'COMPLETED',
    350000,
    'Sửa ống nước tại nhà',
    '2024-01-15 10:00:00',
    '2024-01-15 15:00:00'
WHERE EXISTS (SELECT 1 FROM auth_users WHERE role = 'USER');

-- Order 2: In Progress order
INSERT INTO orders_orders (
    user_id,
    service_id,
    status,
    total_amount,
    notes,
    created_at,
    updated_at
)
SELECT 
    (SELECT id FROM auth_users WHERE role = 'USER' ORDER BY id LIMIT 1),
    2,
    'IN_PROGRESS',
    250000,
    'Sửa điện tại văn phòng',
    '2024-01-14 09:00:00',
    '2024-01-14 09:00:00'
WHERE EXISTS (SELECT 1 FROM auth_users WHERE role = 'USER');

-- Order 3: Pending order
INSERT INTO orders_orders (
    user_id,
    service_id,
    status,
    total_amount,
    notes,
    created_at,
    updated_at
)
SELECT 
    (SELECT id FROM auth_users WHERE role = 'USER' ORDER BY id LIMIT 1 OFFSET 1),
    3,
    'PENDING',
    500000,
    'Bảo trì hệ thống điện',
    '2024-01-13 14:00:00',
    '2024-01-13 14:00:00'
WHERE EXISTS (SELECT 1 FROM auth_users WHERE role = 'USER' LIMIT 1 OFFSET 1);

-- Order 4: Completed order
INSERT INTO orders_orders (
    user_id,
    service_id,
    status,
    total_amount,
    notes,
    created_at,
    updated_at
)
SELECT 
    (SELECT id FROM auth_users WHERE role = 'USER' ORDER BY id LIMIT 1 OFFSET 1),
    4,
    'COMPLETED',
    400000,
    'Thay bơm nước',
    '2024-01-12 11:00:00',
    '2024-01-12 16:00:00'
WHERE EXISTS (SELECT 1 FROM auth_users WHERE role = 'USER' LIMIT 1 OFFSET 1);

-- Order 5: Cancelled order
INSERT INTO orders_orders (
    user_id,
    service_id,
    status,
    total_amount,
    notes,
    created_at,
    updated_at
)
SELECT 
    (SELECT id FROM auth_users WHERE role = 'USER' ORDER BY id LIMIT 1 OFFSET 2),
    5,
    'CANCELLED',
    300000,
    'Khách hủy đơn',
    '2024-01-11 08:00:00',
    '2024-01-11 08:30:00'
WHERE EXISTS (SELECT 1 FROM auth_users WHERE role = 'USER' LIMIT 1 OFFSET 2);
