# Windows Performance Optimization Guide

## Vấn đề

Windows Docker Desktop thường chậm hơn Mac/Linux (~4 phút so với ~8 giây) do:
- File I/O chậm hơn (WSL2/Hyper-V overhead)
- Volume mounts chậm hơn
- Gradle daemon startup chậm hơn

## Giải pháp đã áp dụng

### 1. Tối ưu Dockerfile.dev
- Gộp các RUN commands thành 1 layer
- Bỏ pre-download dependencies (giảm build time từ 114s → 2s)
- Dependencies download khi chạy và cache vào volume

### 2. Tối ưu Gradle
- Giảm JVM memory: `-Xmx1024m` (thay vì 2048m) - GC nhanh hơn
- Giảm workers: `org.gradle.workers.max=4` - phù hợp với Windows
- Tắt welcome message: `-Dorg.gradle.console=plain`
- Tắt warnings: `--warning-mode=none`
- Quiet mode: `--quiet`

### 3. Tối ưu docker-compose.dev.yml
- Simplified command (bỏ checks không cần thiết)
- Reduced logging
- G1GC với MaxGCPauseMillis=200ms

## Thời gian ước tính

**Lần đầu chạy:**
- Mac: ~30-60s (download dependencies)
- Windows: ~2-4 phút (download dependencies + slower I/O)

**Lần sau (đã có cache):**
- Mac: ~5-10s
- Windows: ~30-60s (vẫn chậm hơn do I/O nhưng đã cải thiện)

## Tips thêm cho Windows

### 1. Docker Desktop Settings
- **Resources → Advanced:**
  - CPU: 4+ cores
  - Memory: 8GB+ (nếu có)
  - Disk image size: Đủ lớn cho cache

### 2. WSL2 (nếu dùng)
- Cập nhật WSL2 lên version mới nhất
- Tối ưu `.wslconfig`:
  ```ini
  [wsl2]
  memory=8GB
  processors=4
  swap=2GB
  ```

### 3. File Sharing
- Chỉ share thư mục cần thiết
- Tránh share thư mục lớn hoặc nhiều files

### 4. Antivirus Exclusion
- Thêm thư mục project vào Windows Defender exclusion
- Thêm Docker volumes path vào exclusion

## Alternative: Pre-build dependencies

Nếu vẫn quá chậm, có thể pre-download dependencies trong Dockerfile:

```dockerfile
# Uncomment trong Dockerfile.dev:
RUN --mount=type=cache,target=/root/.gradle/caches \
    --mount=type=cache,target=/root/.gradle/wrapper \
    gradle dependencies --no-daemon --quiet || true
```

**Trade-off:**
- Build time: ~2-4 phút (mỗi khi build lại)
- Run time: ~5-10s (không cần download)

## Monitoring Performance

Để xem thời gian thực tế:
```powershell
Measure-Command { docker-compose -f docker/docker-compose.yml -f docker/docker-compose.dev.yml up }
```

## Kết luận

Windows sẽ luôn chậm hơn Mac/Linux do overhead, nhưng với các tối ưu này đã cải thiện đáng kể. Lần đầu chạy sẽ chậm nhất, nhưng các lần sau sẽ nhanh hơn nhờ cache.

