# Quick Start Guide

Hướng dẫn nhanh để bắt đầu phát triển dự án DTH.

## 📋 Yêu cầu

- **Java 17+** (`java -version`)
- **Gradle** (sử dụng Gradle wrapper: `./gradlew`)
- **MariaDB** hoặc **Docker** (cho database)

## 🚀 Cách 1: Chạy với Docker (Khuyến nghị)

### Một lệnh để chạy tất cả

```bash
# Cách 1: Dùng script
./scripts/start.sh

# Cách 2: Dùng Make
make start

# Cách 3: Dùng docker-compose trực tiếp
docker-compose -f docker/docker-compose.yml up -d
```

### Development mode (hot reload)

```bash
# Local Spring Boot + Docker MariaDB (khuyến nghị)
make dev

# Hoặc
./scripts/start-dev.sh
```

**Access:**
- API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html
- Database: localhost:3306 (user: `root`, password: `root`)

## 🔧 Cách 2: Chạy local (không Docker)

### 1. Setup Database

Cài đặt và chạy MariaDB, sau đó tạo database:

```sql
CREATE DATABASE app_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 2. Cấu hình

Chỉnh sửa `src/main/resources/application-dev.properties`:

```properties
spring.datasource.url=jdbc:mariadb://localhost:3306/app_db?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=your_password
```

### 3. Chạy ứng dụng

```bash
# Sử dụng profile dev
SPRING_PROFILES_ACTIVE=dev ./gradlew bootRun

# Hoặc dùng Make
make dev
```

## 📦 Build & Test

### Build

```bash
./gradlew build
```

### Chạy tests

```bash
./gradlew test
```

### Clean build

```bash
./gradlew clean build
```

## 🧪 Test API

### 1. Đăng ký user

```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "username": "testuser",
    "password": "password123",
    "firstName": "Test",
    "lastName": "User"
  }'
```

### 2. Login

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "usernameOrEmail": "testuser",
    "password": "password123"
  }'
```

### 3. Sử dụng Access Token

```bash
# Thay YOUR_ACCESS_TOKEN bằng token từ login response
curl -X GET http://localhost:8080/profile \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

## 🐳 Docker Commands

### Make commands

```bash
make help          # Xem tất cả commands
make start         # Start containers
make dev           # Development mode (local app + Docker DB)
make down          # Stop containers
make logs          # Xem logs
make restart       # Restart containers
make clean         # Clean everything
make rebuild       # Rebuild and restart
```

### Docker Compose commands

```bash
# Start
docker-compose -f docker/docker-compose.yml up -d

# View logs
docker-compose -f docker/docker-compose.yml logs -f

# Stop
docker-compose -f docker/docker-compose.yml down

# Restart
docker-compose -f docker/docker-compose.yml restart
```

## 🔍 Kiểm tra ứng dụng

- **Health check**: http://localhost:8080/actuator/health
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs**: http://localhost:8080/v3/api-docs

## ⚠️ Troubleshooting

### Database connection error

- Kiểm tra MariaDB đang chạy
- Verify credentials trong `application-dev.properties`
- Đảm bảo database tồn tại hoặc `createDatabaseIfNotExist=true`

### Port đã được sử dụng

Thay đổi port trong `application.properties`:
```properties
server.port=8081
```

### Flyway migration errors

- Kiểm tra migration files trong `src/main/resources/db/migration/`
- Verify database schema matches migrations
- Xem Flyway schema history: `flyway_schema_history`

### Java version

Dự án yêu cầu Java 17+. Kiểm tra:
```bash
java -version
```

Nếu chưa có Java 17:
```bash
# macOS (Homebrew)
brew install openjdk@17

# Ubuntu
sudo apt install openjdk-17-jdk
```

## 📚 Bước tiếp theo

1. Xem [README.md](./README.md) để hiểu về kiến trúc
2. Xem [MODULE_ORGANIZATION.md](./MODULE_ORGANIZATION.md) để hiểu cách tổ chức modules
3. Xem [docs/MILESTONE.md](./docs/MILESTONE.md) để xem checklist features
4. Explore Swagger UI: http://localhost:8080/swagger-ui.html
