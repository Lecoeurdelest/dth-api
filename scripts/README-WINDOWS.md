# Hướng dẫn chạy trên Windows với Hot Reload

Script Windows được tối ưu để:
- ✅ **Deploy nhanh**: Chỉ build lại những gì đã thay đổi (sử dụng Docker cache)
- ✅ **Hot Reload**: Tự động reload khi code thay đổi
- ✅ **Không rebuild lại**: Docker tự động detect và chỉ build lại layer cần thiết

## 🚀 Cách sử dụng

### Cách 1: Chạy trực tiếp (Khuyến nghị)

```cmd
cd dth-api
scripts\start-dev.bat
```

### Cách 2: Chạy PowerShell script

```powershell
cd dth-api
powershell -ExecutionPolicy Bypass -File scripts\start-dev.ps1
```

## ⚙️ Yêu cầu

1. **Docker Desktop for Windows** - Đã cài đặt và đang chạy
2. **PowerShell** - Có sẵn trên Windows 10/11
3. **File sharing** - Docker Desktop cần quyền truy cập thư mục dự án

### Cấu hình Docker Desktop (nếu chưa có)

1. Mở Docker Desktop
2. Settings → Resources → File Sharing
3. Thêm thư mục chứa project (ví dụ: `C:\Users\YourName\Documents`)
4. Click "Apply & Restart"

## 🔥 Hot Reload hoạt động như thế nào?

1. **Spring Boot DevTools**: Tự động restart khi code thay đổi
2. **Volume Mounts**: Code được mount vào container, thay đổi được detect ngay
3. **Gradle --continuous**: Tự động rebuild khi file thay đổi

### File được monitor:
- `src/**/*.java` - Java source files
- `src/**/*.kt` - Kotlin files
- `src/**/*.properties` - Properties files
- `build.gradle.kts` - Build configuration

## ⚡ Tối ưu hóa

### Build nhanh nhờ Docker Cache

Docker tự động sử dụng cache cho:
- ✅ Base image (`gradle:8.5-jdk17`) - Chỉ pull 1 lần
- ✅ Gradle dependencies - Cache trong volume `gradle_cache`
- ✅ Build layers - Chỉ rebuild layer thay đổi

**Ví dụ:**
```
Lần đầu build: ~5-10 phút (download dependencies)
Lần sau build: ~10-30 giây (chỉ build code đã thay đổi)
```

### Gradle Build Cache

Script sử dụng:
- `--parallel`: Build parallel
- `--continuous`: Watch files và auto-rebuild
- Gradle daemon: Giữ process chạy để build nhanh hơn
- Cache volumes: Persist Gradle cache giữa các lần restart

### Chỉ build lại khi cần

Docker tự động detect thay đổi:
- **Không đổi** → Dùng cache (rất nhanh)
- **Đổi build.gradle.kts** → Chỉ rebuild dependencies layer
- **Đổi code** → Chỉ rebuild source layer

## 📝 Lệnh hữu ích

### Xem logs
```cmd
docker-compose -f docker/docker-compose.yml -f docker/docker-compose.dev.yml logs -f app
```

### Stop containers
```cmd
docker-compose -f docker/docker-compose.yml -f docker/docker-compose.dev.yml down
```

### Restart chỉ app (giữ DB)
```cmd
docker-compose -f docker/docker-compose.yml -f docker/docker-compose.dev.yml restart app
```

### Force rebuild (clean build)
```cmd
docker-compose -f docker/docker-compose.yml -f docker/docker-compose.dev.yml build --no-cache app
```

### Xóa cache (nếu có vấn đề)
```cmd
docker system prune -a
docker volume prune
```

## 🔍 Troubleshooting

### Lỗi: "Cannot connect to Docker daemon"

**Giải pháp**: 
- Mở Docker Desktop
- Đợi Docker khởi động hoàn toàn (icon không còn spinning)

### Lỗi: "Bind mount failed"

**Giải pháp**:
- Kiểm tra File Sharing trong Docker Desktop Settings
- Thêm thư mục project vào File Sharing
- Restart Docker Desktop

### Build chậm

**Giải pháp**:
- Đảm bảo Docker BuildKit đã bật (script tự động set)
- Kiểm tra Docker Desktop có đủ RAM (recommend 4GB+)
- Đóng các ứng dụng khác để giải phóng tài nguyên

### Hot reload không hoạt động

**Kiểm tra**:
1. Code có trong `src/` directory?
2. File đã được save?
3. Container đang chạy? (`docker ps`)
4. Xem logs: `docker-compose logs app`

**Giải pháp**:
- Restart container: `docker-compose restart app`
- Kiểm tra Spring DevTools đã enable trong `build.gradle.kts`

