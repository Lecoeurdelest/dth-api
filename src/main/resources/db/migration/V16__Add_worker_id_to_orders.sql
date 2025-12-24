ALTER TABLE orders_orders ADD COLUMN worker_id BIGINT NULL AFTER service_id;
ALTER TABLE orders_orders ADD COLUMN scheduled_at TIMESTAMP NULL AFTER worker_id;
ALTER TABLE orders_orders ADD COLUMN duration_minutes INT NULL AFTER scheduled_at;
ALTER TABLE orders_orders ADD INDEX idx_worker_id (worker_id);
ALTER TABLE orders_orders ADD INDEX idx_scheduled_at (scheduled_at);
