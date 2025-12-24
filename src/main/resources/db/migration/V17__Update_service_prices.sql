-- V16: Update service prices to realistic values
-- This migration updates base_price for all services

UPDATE services_services
SET base_price = 150000.00
WHERE name = 'Sửa điện tại nhà';

UPDATE services_services
SET base_price = 180000.00
WHERE name = 'Sửa nước tại nhà';

UPDATE services_services
SET base_price = 200000.00
WHERE name = 'Sửa chữa đồ mộc';

UPDATE services_services
SET base_price = 250000.00
WHERE name = 'Vận chuyển – khuân vác';

UPDATE services_services
SET base_price = 120000.00
WHERE name = 'Lắp đặt đồ gia dụng';

UPDATE services_services
SET base_price = 100000.00
WHERE name = 'Đa dịch vụ sửa chữa nhà cửa';
