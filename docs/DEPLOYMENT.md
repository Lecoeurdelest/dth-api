# Deploy Backend lên EC2 (Amazon Linux)

Hướng dẫn deploy Backend Spring Boot lên EC2 instance Amazon Linux. Frontend Next.js đã được deploy sẵn trên server.

## 📋 Yêu cầu

- **EC2 Instance**: t2.medium (2 vCPU, 4GB RAM) - Amazon Linux 2023
- **Frontend**: Đã chạy trên server (Next.js port 3000)
- **Database**: MariaDB đã cài đặt và cấu hình
- **Access**: SSH key để kết nối EC2

## 🚀 Deploy Backend

### Bước 1: Kết nối EC2 và Chuẩn bị

```bash
# Kết nối vào EC2
ssh -i your-key.pem ec2-user@your-ec2-ip

# Tạo thư mục cho backend
sudo mkdir -p /opt/dth/dth-api
sudo chown ec2-user:ec2-user /opt/dth/dth-api
mkdir -p /opt/dth/logs
```

### Bước 2: Cài đặt Java 17 (nếu chưa có)

```bash
# Amazon Linux 2023
sudo dnf install -y java-17-amazon-corretto-devel

# Kiểm tra
java -version
# Output: openjdk version "17.x.x"
```

### Bước 3: Copy code lên server

**Cách 1: Sử dụng Git (khuyến nghị)**

```bash
cd /opt/dth
git clone <your-repo-url> dth-api
cd dth-api
```

**Cách 2: Copy từ local machine**

```bash
# Từ local machine, compress và copy
tar -czf dth-api.tar.gz dth-api/
scp -i your-key.pem dth-api.tar.gz ec2-user@your-ec2-ip:/tmp/

# Trên server
cd /opt/dth
tar -xzf /tmp/dth-api.tar.gz
rm /tmp/dth-api.tar.gz
cd dth-api
```

### Bước 4: Cấu hình Production Properties

Tạo file `src/main/resources/application-prod.properties`:

```properties
# Application
spring.application.name=app
server.port=8080

# Database - MariaDB (Production)
spring.datasource.url=jdbc:mariadb://localhost:3306/app_db?useSSL=false&serverTimezone=UTC
spring.datasource.username=app_user
spring.datasource.password=YOUR_STRONG_PASSWORD_HERE
spring.datasource.driver-class-name=org.mariadb.jdbc.Driver

# Connection Pool (Optimized for T2.Medium)
spring.datasource.hikari.maximum-pool-size=5
spring.datasource.hikari.minimum-idle=2
spring.datasource.hikari.connection-timeout=20000
spring.datasource.hikari.idle-timeout=300000
spring.datasource.hikari.max-lifetime=600000

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MariaDBDialect
spring.jpa.properties.hibernate.format_sql=false
spring.jpa.open-in-view=false

# Flyway
spring.flyway.enabled=true
spring.flyway.baseline-on-migrate=true
spring.flyway.locations=classpath:db/migration
spring.flyway.clean-disabled=true
spring.flyway.out-of-order=true

# JWT (CHANGE THIS - Generate strong secret key!)
app.jwt.secret=YOUR_SUPER_SECRET_JWT_KEY_CHANGE_THIS_MIN_256_BITS
app.jwt.access-token-expiration-ms=3600000
app.jwt.refresh-token-expiration-ms=86400000

# OpenAPI/Swagger
springdoc.api-docs.path=/v3/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.swagger-ui.operationsSorter=method

# Logging
logging.level.root=INFO
logging.level.com.example.app=INFO
logging.file.name=/opt/dth/logs/application.log
logging.file.max-size=10MB
logging.file.max-history=7

# Server Configuration
server.error.include-message=never
server.error.include-binding-errors=never
server.error.include-stacktrace=never
server.error.include-exception=false

# Compression
server.compression.enabled=true
server.compression.mime-types=application/json,application/xml,text/html,text/xml,text/plain
```

**⚠️ QUAN TRỌNG**: Thay đổi các giá trị sau:
- `spring.datasource.password`: Mật khẩu database
- `app.jwt.secret`: Generate secret key mạnh (ít nhất 256 bits)

### Bước 5: Build và Deploy

**Cách 1: Sử dụng script tự động (Khuyến nghị)**

```bash
cd /opt/dth/dth-api

# Chạy script deploy
bash scripts/deploy-backend.sh
```

**Cách 2: Deploy thủ công**

```bash
cd /opt/dth/dth-api

# Stop service nếu đang chạy
sudo systemctl stop dth-api 2>/dev/null || true

# Build JAR
./gradlew clean build -x test --no-daemon

# Kiểm tra JAR đã được tạo
ls -lh build/libs/app-0.0.1-SNAPSHOT.jar

# Tạo systemd service
sudo tee /etc/systemd/system/dth-api.service > /dev/null << 'EOF'
[Unit]
Description=DTH API Service
After=network.target mariadb.service

[Service]
Type=simple
User=ec2-user
WorkingDirectory=/opt/dth/dth-api
ExecStart=/usr/bin/java -Xms256m -Xmx768m -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -jar -Dspring.profiles.active=prod /opt/dth/dth-api/build/libs/app-0.0.1-SNAPSHOT.jar
Restart=always
RestartSec=10
StandardOutput=append:/opt/dth/logs/api.log
StandardError=append:/opt/dth/logs/api-error.log
Environment="SPRING_PROFILES_ACTIVE=prod"
Environment="JAVA_OPTS=-Xms256m -Xmx768m -XX:+UseG1GC -XX:MaxGCPauseMillis=200"

# Resource limits for T2.Medium
MemoryLimit=1536M
CPUQuota=180%

[Install]
WantedBy=multi-user.target
EOF

# Tạo thư mục logs
mkdir -p /opt/dth/logs

# Khởi động service
sudo systemctl daemon-reload
sudo systemctl enable dth-api
sudo systemctl start dth-api

# Kiểm tra status
sudo systemctl status dth-api
```

### Bước 6: Cấu hình Nginx (nếu chưa có)

Nếu Nginx chưa được cấu hình để proxy đến backend:

```bash
# Cài đặt Nginx (nếu chưa có)
sudo dnf install -y nginx

# Cấu hình Nginx
sudo tee /etc/nginx/conf.d/dth-api.conf > /dev/null << 'EOF'
# Backend API
location /api {
    proxy_pass http://localhost:8080;
    proxy_http_version 1.1;
    proxy_set_header Host $host;
    proxy_set_header X-Real-IP $remote_addr;
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    proxy_set_header X-Forwarded-Proto $scheme;
    
    # Timeouts
    proxy_connect_timeout 60s;
    proxy_send_timeout 60s;
    proxy_read_timeout 60s;
}

# Swagger UI
location /swagger-ui {
    proxy_pass http://localhost:8080;
    proxy_set_header Host $host;
    proxy_set_header X-Real-IP $remote_addr;
}

# API Docs
location /v3/api-docs {
    proxy_pass http://localhost:8080;
    proxy_set_header Host $host;
}
EOF

# Test và reload Nginx
sudo nginx -t
sudo systemctl restart nginx
```

### Bước 7: Kiểm tra Deployment

```bash
# Kiểm tra service status
sudo systemctl status dth-api

# Xem logs
sudo journalctl -u dth-api -f
# Hoặc
tail -f /opt/dth/logs/api.log

# Test API
curl http://localhost:8080/actuator/health
# Hoặc từ bên ngoài
curl http://your-domain.com/api/actuator/health

# Kiểm tra port
sudo netstat -tlnp | grep 8080
```

## 🔄 Update/Re-deploy

Khi có code mới, chỉ cần chạy lại script deploy:

```bash
cd /opt/dth/dth-api

# Pull code mới (nếu dùng Git)
git pull origin main

# Deploy lại
bash scripts/deploy-backend.sh
```

Hoặc thủ công:

```bash
cd /opt/dth/dth-api

# Stop service
sudo systemctl stop dth-api

# Pull code và build
git pull origin main  # hoặc copy code mới
./gradlew clean build -x test --no-daemon

# Start lại
sudo systemctl start dth-api
sudo systemctl status dth-api
```

## 📊 Monitoring & Logs

### Xem logs

```bash
# Systemd logs (real-time)
sudo journalctl -u dth-api -f

# Application logs
tail -f /opt/dth/logs/api.log
tail -f /opt/dth/logs/api-error.log

# Last 100 lines
sudo journalctl -u dth-api -n 100
```

### Health check

```bash
# Local
curl http://localhost:8080/actuator/health

# Through Nginx
curl http://your-domain.com/api/actuator/health
```

### Resource usage

```bash
# Memory usage
free -h

# Java process memory
ps aux | grep java

# CPU và Memory
top -p $(pgrep -f "app-0.0.1-SNAPSHOT.jar")
```

## ⚙️ Quản lý Service

```bash
# Start
sudo systemctl start dth-api

# Stop
sudo systemctl stop dth-api

# Restart
sudo systemctl restart dth-api

# Status
sudo systemctl status dth-api

# Enable auto-start on boot
sudo systemctl enable dth-api

# Disable auto-start
sudo systemctl disable dth-api
```

## 🔧 Troubleshooting

### Service không start

```bash
# Xem logs chi tiết
sudo journalctl -u dth-api -n 100 --no-pager

# Kiểm tra JAR file tồn tại
ls -lh /opt/dth/dth-api/build/libs/app-0.0.1-SNAPSHOT.jar

# Kiểm tra Java
java -version

# Test chạy thủ công
cd /opt/dth/dth-api
java -jar build/libs/app-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

### Database connection error

```bash
# Test kết nối database
mysql -u app_user -p app_db

# Kiểm tra MariaDB đang chạy
sudo systemctl status mariadb

# Kiểm tra credentials trong application-prod.properties
cat src/main/resources/application-prod.properties | grep datasource
```

### Out of Memory

Nếu gặp OutOfMemoryError, có thể giảm heap size trong systemd service:

```bash
sudo systemctl edit dth-api
```

Thêm hoặc sửa:
```ini
[Service]
Environment="JAVA_OPTS=-Xms128m -Xmx512m -XX:+UseG1GC"
```

Sau đó:
```bash
sudo systemctl daemon-reload
sudo systemctl restart dth-api
```

### Port 8080 đã được sử dụng

```bash
# Kiểm tra process nào đang dùng port 8080
sudo lsof -i :8080

# Kill process nếu cần
sudo kill -9 <PID>
```

## 📝 Quick Reference

```bash
# Deploy nhanh
cd /opt/dth/dth-api && bash scripts/deploy-backend.sh

# Xem logs
sudo journalctl -u dth-api -f

# Restart
sudo systemctl restart dth-api

# Status
sudo systemctl status dth-api

# Test API
curl http://localhost:8080/api/actuator/health
```
