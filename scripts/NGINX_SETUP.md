# Hướng dẫn cấu hình Nginx cho Server

## Bước 1: Backup file cũ (nếu có)

```bash
sudo cp /etc/nginx/nginx.conf /etc/nginx/nginx.conf.backup
```

## Bước 2: Copy file cấu hình mới

**Trên máy local (Mac/Windows):**
```bash
# Copy file nginx.conf.example từ project lên server
scp -i pdth.pem scripts/nginx.conf.example ec2-user@13.213.230.73:/tmp/nginx.conf
```

**Trên server:**
```bash
# Di chuyển file vào vị trí đúng
sudo mv /tmp/nginx.conf /etc/nginx/nginx.conf
```

**Hoặc copy trực tiếp nội dung vào server:**
```bash
sudo nano /etc/nginx/nginx.conf
# Paste nội dung từ file nginx.conf.example
```

## Bước 3: Sửa domain name (nếu cần)

Mở file và thay đổi `server_name`:
```bash
sudo nano /etc/nginx/nginx.conf
```

Tìm dòng:
```nginx
server_name suachuanho.com.vn www.suachuanho.com.vn;
```

Sửa thành domain của bạn nếu khác.

## Bước 4: Cấu hình HTTPS (nếu có SSL certificate)

1. Uncomment (bỏ dấu `#`) phần HTTPS server block
2. Sửa đường dẫn SSL certificate:
   ```nginx
   ssl_certificate /etc/letsencrypt/live/suachuanho.com.vn/fullchain.pem;
   ssl_certificate_key /etc/letsencrypt/live/suachuanho.com.vn/privkey.pem;
   ```
3. Uncomment dòng redirect HTTP → HTTPS:
   ```nginx
   return 301 https://$server_name$request_uri;
   ```

## Bước 5: Test và reload Nginx

```bash
# Test cấu hình
sudo nginx -t

# Nếu test OK, reload Nginx
sudo systemctl reload nginx

# Hoặc restart nếu cần
sudo systemctl restart nginx

# Kiểm tra status
sudo systemctl status nginx
```

## Bước 6: Kiểm tra Swagger UI

```bash
# Test từ server
curl http://localhost:8080/api/swagger-ui.html
curl http://localhost:8080/api/swagger-ui/index.html

# Test qua Nginx
curl http://localhost/api/swagger-ui.html
curl http://localhost/api/swagger-ui/index.html

# Nếu có domain, test từ bên ngoài
curl https://suachuanho.com.vn/api/swagger-ui/index.html
```

## Troubleshooting

### Nếu Swagger UI không load được:

1. **Kiểm tra backend có chạy không:**
   ```bash
   sudo systemctl status dth-api
   curl http://localhost:8080/api/swagger-ui.html
   ```

2. **Kiểm tra Nginx logs:**
   ```bash
   sudo tail -f /var/log/nginx/error.log
   sudo tail -f /var/log/nginx/access.log
   ```

3. **Kiểm tra ports:**
   ```bash
   sudo netstat -tlnp | grep -E ':(80|443|3000|8080)'
   ```

4. **Test từng location:**
   ```bash
   # Test API
   curl -v http://localhost/api/v3/api-docs
   
   # Test Swagger UI
   curl -v http://localhost/api/swagger-ui.html
   curl -v http://localhost/api/swagger-ui/index.html
   ```

### Nếu lỗi 502 Bad Gateway:

- Backend không chạy hoặc không accessible
- Kiểm tra: `sudo systemctl status dth-api`
- Kiểm tra port 8080: `sudo lsof -i :8080`

### Nếu lỗi 404 Not Found:

- Kiểm tra location blocks trong nginx.conf
- Đảm bảo thứ tự: location cụ thể trước, location tổng quát sau
- Kiểm tra `proxy_pass` có đúng không

