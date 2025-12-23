# Dự án DTH - Milestone Checklist

## Tổng quan dự án
Hệ thống đặt dịch vụ sửa chữa với tính năng booking thợ, quản lý đơn hàng, đánh giá và quản trị viên.

---

## 1. Fetch Data - Service và Thợ

### 1.1 Service (Dịch vụ)
- [ ] API lấy danh sách dịch vụ
  - [ ] GET `/api/services` - Lấy tất cả dịch vụ
  - [ ] Hỗ trợ filter theo category
  - [ ] Hỗ trợ pagination
  - [ ] Trả về: id, name, description, basePrice, imageUrl, category

- [ ] API lấy chi tiết dịch vụ
  - [ ] GET `/api/services/{id}` - Lấy thông tin chi tiết 1 dịch vụ

- [ ] Frontend integration
  - [ ] Component hiển thị danh sách dịch vụ
  - [ ] Component hiển thị chi tiết dịch vụ

### 1.2 Workers/Thợ (Craftsmen)
- [ ] API lấy danh sách thợ
  - [ ] GET `/api/workers` - Lấy danh sách thợ có sẵn
  - [ ] Filter theo specialization (chuyên môn)
  - [ ] Filter theo availability (có sẵn hay không)
  - [ ] Hỗ trợ pagination
  - [ ] Trả về: id, name, avatar, phone, experience, rating, specializations, available

- [ ] API lấy chi tiết thợ
  - [ ] GET `/api/workers/{id}` - Lấy thông tin chi tiết 1 thợ

- [ ] Frontend integration
  - [ ] Component hiển thị danh sách thợ
  - [ ] Component chọn thợ khi booking
  - [ ] Hiển thị rating, đánh giá của thợ

---

## 2. Booking Thợ và Quản lý Request

### 2.1 Book Thợ
- [ ] API tạo booking request
  - [ ] POST `/api/orders` hoặc `/api/bookings` - Tạo đơn booking
  - [ ] Body: userId, serviceId, workerId, bookingDate, bookingTime, address, notes
  - [ ] Validation: kiểm tra thợ có available không
  - [ ] Trả về: orderId, status (PENDING)

- [ ] Kiểm tra danh sách thợ đã book (trong 1 request)
  - [ ] Khi tạo booking, kiểm tra xem thợ đã được book trong khoảng thời gian đó chưa
  - [ ] Trả về thông tin nếu thợ không available (đã có booking khác)

### 2.2 List Request (Danh sách đơn đặt)
- [ ] API lấy danh sách requests của user
  - [ ] GET `/api/orders` - Lấy tất cả đơn của user hiện tại
  - [ ] Filter theo status (PENDING, CONFIRMED, IN_PROGRESS, COMPLETED, CANCELLED)
  - [ ] Hỗ trợ pagination
  - [ ] Trả về: danh sách orders với thông tin service, worker, status, date

- [ ] API lấy chi tiết 1 request
  - [ ] GET `/api/orders/{id}` - Lấy chi tiết đơn đặt
  - [ ] Trả về đầy đủ thông tin: service, worker, booking details, status

### 2.3 Xác nhận trạng thái Request
- [ ] API xác nhận request (cho thợ)
  - [ ] PUT `/api/orders/{id}/confirm` - Thợ xác nhận nhận đơn
  - [ ] Chỉ thợ được assign mới có quyền confirm
  - [ ] Update status: PENDING → CONFIRMED

- [ ] API bắt đầu thực hiện
  - [ ] PUT `/api/orders/{id}/start` - Thợ bắt đầu làm việc
  - [ ] Update status: CONFIRMED → IN_PROGRESS

- [ ] API hoàn thành
  - [ ] PUT `/api/orders/{id}/complete` - Thợ hoàn thành
  - [ ] Update status: IN_PROGRESS → COMPLETED
  - [ ] Sau khi complete, cho phép user đánh giá

### 2.4 Đánh giá (Rating/Review)
- [ ] API tạo đánh giá
  - [ ] POST `/api/orders/{id}/reviews` - User đánh giá sau khi hoàn thành
  - [ ] Body: rating (1-5), comment
  - [ ] Chỉ cho phép đánh giá khi order status = COMPLETED
  - [ ] Update rating của worker sau khi có review mới

- [ ] API lấy đánh giá
  - [ ] GET `/api/orders/{id}/review` - Lấy review của order
  - [ ] GET `/api/workers/{id}/reviews` - Lấy tất cả reviews của thợ

---

## 3. Thợ: Xem Requests được Book

### 3.1 Dashboard cho Thợ
- [ ] API lấy danh sách requests được assign cho thợ
  - [ ] GET `/api/workers/{workerId}/orders` - Lấy đơn đã được assign
  - [ ] Filter theo status
  - [ ] Sắp xếp theo ngày booking
  - [ ] Trả về: danh sách orders với thông tin user, service, booking time

- [ ] API accept/reject request
  - [ ] PUT `/api/orders/{id}/accept` - Thợ chấp nhận đơn
  - [ ] PUT `/api/orders/{id}/reject` - Thợ từ chối đơn (nếu có)

### 3.2 Thông tin Request cho Thợ
- [ ] Hiển thị thông tin user (name, phone, address)
- [ ] Hiển thị thông tin service cần thực hiện
- [ ] Hiển thị thời gian booking
- [ ] Hiển thị ghi chú từ user
- [ ] Hiển thị trạng thái hiện tại

---

## 4. Tình trạng Vấn đề - Mức độ Sửa chữa

### 4.1 Phân loại theo mức độ
- [ ] Thêm field `severity` vào Order entity
  - [ ] Enum: HIGH (Cao), MEDIUM (Trung bình), LOW (Thấp)
  - [ ] Migration: ALTER TABLE orders_orders ADD severity VARCHAR(20)

- [ ] API update severity
  - [ ] PUT `/api/orders/{id}/severity` - Cập nhật mức độ (có thể thợ hoặc user cập nhật)
  - [ ] Body: severity (HIGH, MEDIUM, LOW)

- [ ] Tính giá theo mức độ
  - [ ] HIGH: basePrice * 1.5
  - [ ] MEDIUM: basePrice * 1.2
  - [ ] LOW: basePrice * 1.0

### 4.2 Hiển thị trong Frontend
- [ ] Badge/Indicator hiển thị mức độ
- [ ] Color coding: HIGH (red), MEDIUM (yellow), LOW (green)
- [ ] Thông tin trong order detail page

---

## 5. Màn hình Admin (Chưa có)

### 5.1 Admin Dashboard
- [ ] Tổng quan hệ thống
  - [ ] Tổng số users
  - [ ] Tổng số orders (theo status)
  - [ ] Tổng số workers
  - [ ] Revenue (nếu có)

### 5.2 Quản lý Users
- [ ] Danh sách users
- [ ] Xem chi tiết user
- [ ] Block/Unblock user
- [ ] Xem lịch sử orders của user

### 5.3 Quản lý Workers/Thợ
- [ ] Danh sách workers
- [ ] Xem chi tiết worker
- [ ] Activate/Deactivate worker
- [ ] Xem lịch sử orders của worker
- [ ] Quản lý specializations

### 5.4 Quản lý Orders
- [ ] Danh sách tất cả orders
- [ ] Filter theo status, date, worker, user
- [ ] Xem chi tiết order
- [ ] Có thể assign worker thủ công (nếu cần)
- [ ] Xem reviews/ratings

### 5.5 Quản lý Services
- [ ] CRUD operations cho services
- [ ] Create, Update, Delete service
- [ ] Quản lý categories
- [ ] Quản lý pricing

### 5.6 Reports & Analytics
- [ ] Thống kê orders theo thời gian
- [ ] Top workers (rating, số lượng orders)
- [ ] Top services được book nhiều nhất
- [ ] Revenue reports

---

## 6. Admin API

### 6.1 Authentication & Authorization
- [ ] Admin role trong User entity
- [ ] JWT với role ADMIN
- [ ] Security config cho admin endpoints
  - [ ] `/api/admin/**` chỉ accessible bởi ADMIN role

### 6.2 Admin Controllers
- [ ] `AdminUserController` - Quản lý users
  - [ ] GET `/api/admin/users` - List users
  - [ ] GET `/api/admin/users/{id}` - User detail
  - [ ] PUT `/api/admin/users/{id}/block` - Block user
  - [ ] PUT `/api/admin/users/{id}/unblock` - Unblock user

- [ ] `AdminWorkerController` - Quản lý workers
  - [ ] GET `/api/admin/workers` - List workers
  - [ ] GET `/api/admin/workers/{id}` - Worker detail
  - [ ] PUT `/api/admin/workers/{id}/activate` - Activate worker
  - [ ] PUT `/api/admin/workers/{id}/deactivate` - Deactivate worker
  - [ ] POST `/api/admin/workers` - Create worker (nếu cần)

- [ ] `AdminOrderController` - Quản lý orders
  - [ ] GET `/api/admin/orders` - List all orders
  - [ ] GET `/api/admin/orders/{id}` - Order detail
  - [ ] PUT `/api/admin/orders/{id}/assign-worker` - Assign worker manually

- [ ] `AdminServiceController` - Quản lý services
  - [ ] POST `/api/admin/services` - Create service
  - [ ] PUT `/api/admin/services/{id}` - Update service
  - [ ] DELETE `/api/admin/services/{id}` - Delete service

- [ ] `AdminDashboardController` - Dashboard stats
  - [ ] GET `/api/admin/dashboard/stats` - Tổng quan stats

---

## 7. Review ERD Design

### 7.1 Current Schema Review
- [ ] Review tất cả tables hiện tại
  - [ ] auth_users
  - [ ] services_services
  - [ ] orders_orders
  - [ ] orders_reviews
  - [ ] loyalty_* tables
  - [ ] profile_* tables

### 7.2 Missing Tables/Schema Changes
- [ ] Workers tables
  - [ ] workers_workers (chưa có?)
  - [ ] workers_worker_availability (nếu cần)
  - [ ] workers_worker_specializations (nếu cần)

- [ ] Order enhancements
  - [ ] orders_technician_assignments (nếu chưa có trong orders_orders)
  - [ ] Field severity trong orders_orders
  - [ ] Field booking_date, booking_time trong orders_orders

- [ ] Admin tables (nếu cần riêng)
  - [ ] admin_logs (nếu cần audit log)

### 7.3 Migration Strategy
- [ ] Tạo migration files cho các thay đổi
- [ ] Test migrations trên dev environment
- [ ] Backup strategy trước khi migrate production

### 7.4 Impact on Service Calls
- [ ] Update OrderService khi có thay đổi schema
- [ ] Update WorkerService (nếu mới tạo)
- [ ] Update các DTOs liên quan
- [ ] Update Mappers
- [ ] Test tất cả endpoints sau khi thay đổi

---

## 8. API sử dụng Dummy Data

### 8.1 Strategy
- [ ] Tạo các Controller/DTOs với dummy data
- [ ] Hoặc tạo Mock Services trả về hardcoded data
- [ ] Useful cho development frontend mà không cần backend đầy đủ

### 8.2 Dummy Data Endpoints
- [ ] `/api/dummy/services` - Dummy services list
- [ ] `/api/dummy/workers` - Dummy workers list
- [ ] `/api/dummy/orders` - Dummy orders list
- [ ] `/api/dummy/users` - Dummy users (nếu cần)

### 8.3 Configuration
- [ ] Environment variable để switch giữa dummy và real data
  - [ ] `app.use-dummy-data=true/false`
- [ ] Profile-based: `dev-dummy` profile

### 8.4 Migration Path
- [ ] Dễ dàng switch từ dummy sang real data
- [ ] Dummy data structure phải match với real DTOs
- [ ] Documentation về cách switch

---

## 9. Deploy lên EC2

### 9.1 EC2 Setup
- [ ] Chọn EC2 instance type (đủ khỏe cho cả BE + FE)
  - [ ] Recommended: t3.medium hoặc t3.large (2-4 vCPU, 4-8GB RAM)
  - [ ] Hoặc t2.medium/t2.large nếu budget thấp hơn

- [ ] Security Group configuration
  - [ ] Port 22 (SSH)
  - [ ] Port 80 (HTTP)
  - [ ] Port 443 (HTTPS)
  - [ ] Port 8080 (Spring Boot - internal)
  - [ ] Port 3000 (Next.js - internal)

### 9.2 Backend Deployment (Spring Boot)
- [ ] Install Java 17+ trên EC2
- [ ] Install MariaDB/PostgreSQL
- [ ] Clone repository
- [ ] Build JAR file: `./gradlew build`
- [ ] Setup systemd service cho Spring Boot app
- [ ] Configure application.properties cho production
- [ ] Setup database migrations
- [ ] Configure logging
- [ ] Health check endpoint

### 9.3 Frontend Deployment (Next.js/React)
- [ ] Install Node.js 18+ trên EC2
- [ ] Clone repository
- [ ] Build: `npm run build`
- [ ] Run production server: `npm start` hoặc dùng PM2
- [ ] Hoặc build static và serve bằng Nginx

### 9.4 Reverse Proxy (Nginx)
- [ ] Install Nginx
- [ ] Configure Nginx
  - [ ] `/api/**` → proxy to Spring Boot (localhost:8080)
  - [ ] `/` → proxy to Next.js (localhost:3000)
  - [ ] Static files caching
  - [ ] SSL/TLS với Let's Encrypt

### 9.5 Database
- [ ] Install MariaDB hoặc PostgreSQL
- [ ] Create database và user
- [ ] Run migrations
- [ ] Backup strategy
- [ ] Connection pooling configuration

### 9.6 Monitoring & Logging
- [ ] Setup application logs
- [ ] Log rotation
- [ ] Basic monitoring (CPU, Memory, Disk)
- [ ] Error tracking (optional)

### 9.7 CI/CD (Optional nhưng recommended)
- [ ] GitHub Actions hoặc GitLab CI
- [ ] Auto deploy khi push to main branch
- [ ] Run tests trước khi deploy

### 9.8 Documentation
- [ ] Deployment guide
- [ ] Environment variables list
- [ ] Troubleshooting guide
- [ ] Rollback procedure

---

## Priority Order

### Phase 1: Core Features (High Priority)
1. Fetch Data - Service và Thợ ✅
2. Booking Thợ cơ bản ✅
3. List Request và Xác nhận trạng thái ✅
4. Đánh giá ✅

### Phase 2: Worker Features
5. Thợ: Xem Requests được Book ✅
6. Thợ: Accept/Confirm requests ✅

### Phase 3: Advanced Features
7. Tình trạng Vấn đề - Mức độ (High/Medium/Low) ✅
8. Review và Update ERD nếu cần ✅

### Phase 4: Admin Features
9. Màn hình Admin ✅
10. Admin API ✅

### Phase 5: Infrastructure
11. Dummy Data API (nếu cần cho dev) ✅
12. Deploy lên EC2 ✅

---

## Notes
- Tất cả API endpoints cần có authentication (trừ public endpoints)
- Cần có proper error handling và validation
- Cần có unit tests và integration tests
- Documentation cho API endpoints (Swagger/OpenAPI)
- Frontend cần handle loading states và error states
- Consider pagination cho tất cả list endpoints
- Consider caching cho frequently accessed data

