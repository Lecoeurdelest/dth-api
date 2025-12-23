-- Add details column and insert seed data for services (includes minimal details JSON)
ALTER TABLE services_services
    ADD COLUMN IF NOT EXISTS details TEXT;

INSERT INTO services_services (name, description, base_price, image_url, category, details, active)
VALUES
('Sửa điện tại nhà',
 'Dịch vụ sửa chữa điện chuyên nghiệp, an toàn và hiệu quả.',
 0.00,
 'https://images.unsplash.com/photo-1636218685495-8f6545aadb71?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&q=80&w=1080',
 'ĐIỆN',
 '{"title":"SỬA CHỮA ĐIỆN","headerImage":"https://images.unsplash.com/photo-1636218685495-8f6545aadb71?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&q=80&w=1080","description":"Dịch vụ sửa chữa điện chuyên nghiệp, an toàn và hiệu quả.","pricingCategories":[{"category":"SỬA CHỮA ĐIỆN DÂN DỤNG","items":[{"item":"Kiểm tra – chẩn đoán sự cố","price":"50.000 – 80.000đ"},{"item":"Sửa công tắc, ổ cắm","price":"80.000 – 150.000đ"}]}],"commitments":["Báo giá trước khi làm – không phát sinh","Thợ chuyên nghiệp, làm việc tận tâm"], "images":["https://images.unsplash.com/photo-1621905251918-48416bd8575a?w=800"] }',
 true
);

INSERT INTO services_services (name, description, base_price, image_url, category, details, active)
VALUES
('Sửa nước tại nhà',
 'Sửa chữa hệ thống nước - thông tắc công trình, hút bể phốt',
 0.00,
 'https://images.unsplash.com/photo-1654440122140-f1fc995ddb34?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&q=80&w=1080',
 'NƯỚC',
 '{"title":"SỬA CHỮA NƯỚC","headerImage":"https://images.unsplash.com/photo-1654440122140-f1fc995ddb34?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&q=80&w=1080","description":"Dịch vụ sửa chữa hệ thống nước chuyên nghiệp.","pricingCategories":[{"category":"SỬA NƯỚC","items":[{"item":"Sửa rò rỉ ống nước","price":"120.000 – 250.000đ"}]}],"commitments":["Báo giá trước khi làm – không phát sinh"], "images":["https://images.unsplash.com/photo-1607472586893-edb57bdc0e39?w=800"] }',
 true
);

INSERT INTO services_services (name, description, base_price, image_url, category, details, active)
VALUES
('Sửa chữa đồ mộc',
 'Sửa chữa đồ mộc – Thay thế - tân trang',
 0.00,
 'https://images.unsplash.com/photo-1704784846246-d81b3542d3d3?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&q=80&w=1080',
 'MỘC',
 '{"title":"SỬA CHỮA ĐỒ MỘC","headerImage":"https://images.unsplash.com/photo-1704784846246-d81b3542d3d3?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&q=80&w=1080","description":"Dịch vụ sửa chữa, tân trang đồ gỗ chuyên nghiệp.","commitments":["Làm cẩn thận, chắc chắn – hoàn thiện đẹp"], "images":["https://images.unsplash.com/photo-1616047006789-b7af5afb8c20?w=800"] }',
 true
);


