# Hướng dẫn CI/CD - Dagger + GitHub Actions

Hướng dẫn đầy đủ cách setup và sử dụng CI/CD pipeline để tự động build và deploy backend lên EC2.

**✅ Hỗ trợ:** Windows, macOS, Linux - Tự động hóa hoàn toàn, không cần can thiệp thủ công.

---

## 📋 Mục lục

1. [Setup (làm 1 lần)](#1-setup-làm-1-lần)
2. [Chạy Local](#2-chạy-local)
3. [Setup GitHub Actions](#3-setup-github-actions)
4. [Giải thích các phần](#4-giải-thích-các-phần)
5. [Troubleshooting](#5-troubleshooting)

---

## 1. Setup (làm 1 lần)

### Bước 1.1: Cài đặt Dagger CLI

**macOS:**
```bash
brew install dagger/tap/dagger
```

**Linux:**
```bash
# Option 1: Install script (khuyến nghị)
curl -L https://dl.dagger.io/dagger/install.sh | sh

# Option 2: Manual download
# Download từ: https://docs.dagger.io/install
```

**Windows:**
```powershell
# Option 1: Winget
winget install dagger --source winget

# Option 2: Download từ
# https://docs.dagger.io/install
```

**Kiểm tra cài đặt:**
```bash
dagger version
```

**Giải thích:** Dagger CLI là công cụ chạy pipeline. Cần cài đặt trước khi sử dụng. Script sẽ tự động kiểm tra và báo lỗi nếu chưa có.

### Bước 1.2: Chuẩn bị SSH Key và Config

```bash
cd infra

# 1. Copy file pdth.pem vào thư mục infra/
cp /path/to/pdth.pem infra/

# 2. Set permissions (Mac/Linux only, tự động trên Windows)
chmod 600 infra/pdth.pem

# 3. Tạo file .env từ template
cp .env.example .env

# 4. Chỉnh sửa .env, set SERVER_IP
# macOS/Linux:
nano .env

# Windows (Notepad):
notepad .env
```

**Nội dung file `.env`:**
```bash
# Bắt buộc: IP của EC2 server
SERVER_IP=your-server-ip-here

# Optional: Có thể để mặc định hoặc thay đổi
SERVER_USER=ec2-user
DEPLOY_PATH=/opt/dth/dth-api
SERVICE_NAME=dth-api
```

**Giải thích:**
- File `pdth.pem` là SSH private key để kết nối EC2. Script tự động đọc file này.
- File `.env` chứa config riêng của bạn, đã được ignore trong Git.
- `SERVER_IP` là **bắt buộc**, các biến khác có giá trị mặc định.

**✨ Python dependencies tự động:** Script sẽ tự động cài đặt `dagger-io` và `python-dotenv` nếu chưa có.

---

## 2. Chạy Local

Script tự động làm tất cả: kiểm tra dependencies, load config, và chạy pipeline.

### Build (chỉ build, không deploy)

**macOS/Linux:**
```bash
cd infra
./deploy build
```

**Windows:**
```cmd
cd infra
deploy.bat build
```

**Hoặc dùng Python trực tiếp (tất cả platforms):**
```bash
cd infra
python deploy.py build
```

**Giải thích:** 
- Build JAR file từ source code trong Docker container
- Script tự động kiểm tra và cài đặt dependencies nếu cần
- Tự động load `.env` và SSH key từ `pdth.pem`

### Deploy (build + deploy lên server)

**macOS/Linux:**
```bash
cd infra
./deploy deploy
```

**Windows:**
```cmd
cd infra
deploy.bat deploy
```

**Hoặc dùng Python trực tiếp (tất cả platforms):**
```bash
cd infra
python deploy.py deploy
```

**Giải thích:**
- Tự động build JAR file
- Tự động copy JAR lên EC2 server qua SSH
- Tự động restart service trên server
- **Không cần export thủ công** - script tự động load mọi thứ từ `.env` và `pdth.pem`

**✨ Tự động hoàn toàn:**
- ✅ Tự động cài Python dependencies nếu thiếu
- ✅ Tự động load `.env` file
- ✅ Tự động load SSH key từ `pdth.pem`
- ✅ Tự động validate config trước khi chạy
- ✅ Tự động set file permissions (Mac/Linux)

**Giải thích:** 
- Build JAR file
- Copy JAR lên EC2 server qua SSH
- Restart service trên server

**Lưu ý:** 
- Script sẽ tự động kiểm tra và cài đặt Python dependencies nếu cần
- Tất cả config được load tự động từ `.env` và `pdth.pem`
- Không cần export environment variables thủ công

---

## 3. Setup GitHub Actions

### Bước 3.1: Tạo GitHub Secrets

Vào GitHub repository → **Settings** → **Secrets and variables** → **Actions** → **New repository secret**

Thêm 2 secrets **bắt buộc**:

| Secret Name | Value | Cách lấy |
|------------|-------|----------|
| `SSH_PRIVATE_KEY` | Nội dung file `pdth.pem` | `cat pdth.pem` → Copy toàn bộ (kể cả BEGIN/END lines) |
| `SERVER_IP` | IP của EC2 server | IP public của EC2 instance |

**Secrets tùy chọn** (có default):

| Secret Name | Default | Mô tả |
|------------|---------|-------|
| `SERVER_USER` | `ec2-user` | SSH user (thường là ec2-user cho Amazon Linux) |
| `DEPLOY_PATH` | `/opt/dth/dth-api` | Đường dẫn deploy trên server |
| `SERVICE_NAME` | `dth-api` | Tên systemd service |

**Giải thích:** 
- GitHub Secrets được dùng để lưu thông tin nhạy cảm (SSH key, IP)
- Workflow sẽ tự động lấy các secrets này khi chạy
- Không cần hardcode trong code

### Bước 3.2: File workflow đã có sẵn

File `.github/workflows/ci-cd.yml` đã được tạo sẵn với cấu hình đầy đủ. Bạn không cần chỉnh sửa gì.

### Bước 3.3: Push code để trigger

```bash
git add .
git commit -m "Setup CI/CD"
git push origin main
```

**Giải thích:**
- Khi push code vào `main` branch, GitHub Actions sẽ tự động:
  1. **Build job:** Build JAR file
  2. **Deploy job:** Deploy JAR lên EC2 server
- Xem progress trong tab **Actions** trên GitHub

---

## 4. Giải thích các phần

### 4.1. Cấu trúc thư mục `infra/`

```
infra/
├── main.py           # Dagger pipeline script (build + deploy)
├── deploy            # Wrapper script (tự động load .env và pdth.pem)
├── requirements.txt  # Python dependencies
├── .env.example      # Template cho file .env
├── GUIDE.md          # File này
└── pdth.pem          # SSH key (không commit vào Git)
```

**Giải thích:**
- `main.py`: Script chính chứa logic build và deploy
- `deploy`: Wrapper script giúp tự động load config, tiện hơn khi chạy
- `.env`: File config của bạn (tạo từ `.env.example`)

### 4.2. Flow hoạt động

#### Local:
```
1. Chạy: ./deploy deploy
2. Script load .env và pdth.pem
3. Dagger build JAR trong Docker container
4. Dagger SSH vào server và copy JAR
5. Restart service trên server
```

#### GitHub Actions:
```
1. Push code → GitHub nhận được
2. Build job chạy → Build JAR, upload artifact
3. Deploy job chạy → Download JAR, deploy lên EC2
```

### 4.3. Environment Variables

Script sử dụng biến môi trường để cấu hình. Có 2 cách set:

**Cách 1: File `.env` (khuyến nghị cho local)**
```bash
# Tạo file .env
cp .env.example .env
# Chỉnh sửa .env
```

**Cách 2: Export trực tiếp (cho testing)**
```bash
export SERVER_IP=your-ip
export SSH_PRIVATE_KEY="$(cat pdth.pem)"
python main.py deploy
```

**Giải thích các biến quan trọng:**

| Biến | Bắt buộc | Mô tả | Ví dụ |
|------|----------|-------|-------|
| `SERVER_IP` | ✅ Yes | IP public của EC2 | `13.213.230.73` |
| `SSH_PRIVATE_KEY` | ✅ Yes* | Nội dung SSH key | `-----BEGIN RSA...` |
| `SERVER_USER` | No | SSH user | `ec2-user` |
| `DEPLOY_PATH` | No | Đường dẫn deploy | `/opt/dth/dth-api` |
| `SERVICE_NAME` | No | Tên service | `dth-api` |

*Có thể dùng `SSH_KEY_PATH=pdth.pem` thay vì `SSH_PRIVATE_KEY`

### 4.4. Dagger Pipeline (main.py)

**Build function:**
```python
async def build(client: dagger.Client) -> dagger.File:
    # 1. Lấy source code từ local
    source = client.host().directory(".", exclude=[".git", "build", ...])
    
    # 2. Tạo container với Gradle
    builder = client.container().from_("gradle:8.5-jdk17")
    
    # 3. Mount source code vào container
    builder = builder.with_mounted_directory("/app", source)
    
    # 4. Chạy Gradle build
    builder = builder.with_exec(["gradle", "clean", "build"])
    
    # 5. Lấy JAR file
    jar_file = builder.file("build/libs/app-0.0.1-SNAPSHOT.jar")
    
    return jar_file
```

**Giải thích:**
- Dagger tạo Docker container để build
- Đảm bảo môi trường build nhất quán (không phụ thuộc máy local)
- Tự động cache, build nhanh hơn lần sau

**Deploy function:**
```python
async def deploy_jar(client, jar_file):
    # 1. Lấy SSH key từ env
    ssh_key = os.getenv("SSH_PRIVATE_KEY")
    
    # 2. Tạo container với SSH tools
    deployer = client.container().from_("alpine:latest")
    deployer = deployer.with_exec(["apk", "add", "openssh-client"])
    
    # 3. Copy SSH key vào container
    deployer = deployer.with_new_file("/root/.ssh/id_rsa", contents=ssh_key)
    
    # 4. Copy JAR vào container
    deployer = deployer.with_file("/app/app.jar", jar_file)
    
    # 5. SCP JAR lên server
    deployer = deployer.with_exec(["scp", "/app/app.jar", "ec2-user@IP:/path/"])
    
    # 6. SSH restart service
    deployer = deployer.with_exec(["ssh", "ec2-user@IP", "sudo systemctl restart dth-api"])
```

**Giải thích:**
- Dagger tạo container với SSH client
- Copy JAR và SSH key vào container
- Chạy `scp` và `ssh` từ trong container
- Đảm bảo SSH key không bị lộ (chỉ trong container)

### 4.5. GitHub Actions Workflow

File `.github/workflows/ci-cd.yml`:

```yaml
on:
  push:
    branches: [main]
```

**Giải thích:** Workflow chạy khi push vào `main` branch.

```yaml
jobs:
  build:
    steps:
      - uses: actions/checkout@v4
      - uses: dagger/dagger-for-github@v5
      - run: cd infra && python main.py build
```

**Giải thích:** 
- Checkout code từ repo
- Setup Dagger CLI
- Chạy build pipeline

```yaml
  deploy:
    needs: build
    if: github.ref == 'refs/heads/main'
    steps:
      - env:
          SSH_PRIVATE_KEY: ${{ secrets.SSH_PRIVATE_KEY }}
          SERVER_IP: ${{ secrets.SERVER_IP }}
        run: cd infra && python main.py deploy
```

**Giải thích:**
- Chỉ chạy sau khi build thành công
- Chỉ chạy khi push vào `main` (không chạy khi PR)
- Lấy secrets từ GitHub Secrets và set làm env vars

---

## 5. Troubleshooting

### ❌ Lỗi: "dagger: command not found"

**Nguyên nhân:** Chưa cài Dagger CLI

**Giải pháp:**
```bash
# macOS
brew install dagger/tap/dagger

# Linux
curl -L https://dl.dagger.io/dagger/install.sh | sh

# Windows
winget install dagger --source winget

# Verify
dagger version
```

Script sẽ tự động báo lỗi và hướng dẫn cài đặt nếu chưa có Dagger CLI.

### ❌ Lỗi: "ModuleNotFoundError: No module named 'dagger'"

**Nguyên nhân:** Chưa cài Python package

**Giải pháp:** 
Script sẽ tự động cài đặt. Nếu vẫn lỗi, chạy thủ công:
```bash
cd infra
pip install -r requirements.txt
# hoặc
python -m pip install -r requirements.txt
```

### ❌ Lỗi: "SERVER_IP environment variable is required"

**Nguyên nhân:** Chưa set `SERVER_IP` trong `.env` hoặc export

**Giải pháp:**
```bash
# Tạo .env
cp .env.example .env
# Chỉnh sửa .env, set SERVER_IP=your-ip
```

### ❌ Lỗi: "SSH key not found"

**Nguyên nhân:** 
- Local: File `pdth.pem` không tồn tại trong `infra/`
- GitHub Actions: Secret `SSH_PRIVATE_KEY` chưa được set

**Giải pháp:**
- Local: Copy `pdth.pem` vào `infra/`
- GitHub Actions: Thêm secret `SSH_PRIVATE_KEY` với nội dung file `.pem`

### ❌ Lỗi: "Permission denied (publickey)"

**Nguyên nhân:** SSH key không đúng hoặc Security Group chặn

**Giải pháp:**
1. Kiểm tra SSH key có đúng không: `ssh -i pdth.pem ec2-user@SERVER_IP`
2. Kiểm tra Security Group của EC2 cho phép SSH từ IP của bạn
3. GitHub Actions: Security Group phải cho phép từ IP của GitHub (hoặc 0.0.0.0/0 cho SSH)

### ❌ Lỗi: "systemctl: command not found" hoặc service không restart

**Nguyên nhân:** Service `dth-api` chưa được tạo trên server

**Giải pháp:**
1. SSH vào server: `ssh -i pdth.pem ec2-user@SERVER_IP`
2. Kiểm tra service: `sudo systemctl status dth-api`
3. Nếu chưa có, tạo service (xem hướng dẫn deploy backend)

### ❌ Build thành công nhưng app không chạy trên server

**Kiểm tra logs trên server:**
```bash
ssh -i pdth.pem ec2-user@SERVER_IP
sudo journalctl -u dth-api -n 50 -f
```

**Common issues:**
- JAR file không tồn tại → Kiểm tra `DEPLOY_PATH`
- Java chưa cài → `sudo yum install java-17-amazon-corretto`
- Port 8080 bị chiếm → `sudo lsof -i :8080`

### ❌ GitHub Actions fail ở bước Deploy

**Kiểm tra:**
1. Vào tab **Actions** trên GitHub
2. Click vào workflow run failed
3. Xem logs của step "Deploy to EC2"
4. Thường là lỗi SSH hoặc thiếu secrets

**Giải pháp:**
- Đảm bảo đã set đầy đủ secrets: `SSH_PRIVATE_KEY`, `SERVER_IP`
- Kiểm tra Security Group của EC2 cho phép SSH từ GitHub Actions IP

---

## 📝 Tóm tắt

### Setup 1 lần (tất cả platforms):

1. **Cài Dagger CLI:**
   - macOS: `brew install dagger/tap/dagger`
   - Linux: `curl -L https://dl.dagger.io/dagger/install.sh | sh`
   - Windows: `winget install dagger --source winget`

2. **Chuẩn bị config:**
   ```bash
   cd infra
   cp .env.example .env
   # Chỉnh sửa .env, set SERVER_IP=your-ip
   cp /path/to/pdth.pem infra/
   ```

3. **Python dependencies:** Script tự động cài (không cần làm gì)

### Chạy Local:

**macOS/Linux:**
```bash
cd infra
./deploy deploy
```

**Windows:**
```cmd
cd infra
deploy.bat deploy
```

**Hoặc Python (tất cả platforms):**
```bash
cd infra
python deploy.py deploy
```

### Setup GitHub Actions:

1. Thêm secrets: `SSH_PRIVATE_KEY`, `SERVER_IP` vào GitHub Secrets
2. Push code → Tự động deploy!

**✨ Tất cả đều tự động:** Script tự động kiểm tra, cài đặt, và load config. Bạn chỉ cần chạy 1 lệnh!

---

**Cần help?** Xem logs hoặc kiểm tra từng bước trong phần Troubleshooting.

