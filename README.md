# HSF302_Group3 - Docker Deployment

## 📦 Đóng gói và chạy trên máy khác

### Bước 1: Trên máy nguồn (Build image)

```bash
# Build Docker image
docker build -t hsf302-group3:latest .

# Export image thành file
docker save hsf302-group3:latest -o hsf302-group3-image.tar
```

### Bước 2: Copy sang máy đích

Copy các file sau:
- `hsf302-group3-image.tar` (file image đã export)
- `docker-compose.prod.yml`
- `.env` (file cấu hình - tùy chọn, có thể tạo mới)
- `README.md` (file này)

### Bước 3: Trên máy đích (Chạy ứng dụng)

```bash
# Load image vào Docker
docker load -i hsf302-group3-image.tar

# Chạy ứng dụng
docker-compose -f docker-compose.prod.yml up -d

# Xem logs
docker-compose -f docker-compose.prod.yml logs -f

# Truy cập: http://localhost:8080
```

### Dừng ứng dụng

```bash
docker-compose -f docker-compose.prod.yml down
```

### Xóa dữ liệu (reset database)

```bash
docker-compose -f docker-compose.prod.yml down -v
```

## ⚙️ Cấu hình

### Sử dụng file .env (Khuyến nghị)

Tạo file `.env` trong cùng thư mục với `docker-compose.prod.yml`:

```env
# Database Configuration
SA_PASSWORD=Dongl@m2025
DB_NAME=HSF302
DB_USERNAME=sa
DB_PASSWORD=Dongl@m2025

# Port Configuration
APP_PORT=8080
SQL_PORT=1435

# SQL Server Configuration
MSSQL_PID=Developer
```

Hoặc copy từ `.env.example`:
```bash
cp .env.example .env
```

### Các biến môi trường:

- `SA_PASSWORD`: Mật khẩu SQL Server (mặc định: Dongl@m2025)
- `DB_NAME`: Tên database (mặc định: HSF302)
- `DB_USERNAME`: Username database (mặc định: sa)
- `DB_PASSWORD`: Mật khẩu database (mặc định: Dongl@m2025)
- `APP_PORT`: Port ứng dụng (mặc định: 8080)
- `SQL_PORT`: Port SQL Server trên host (mặc định: 1435)
- `MSSQL_PID`: SQL Server edition (mặc định: Developer)

**Lưu ý**: Nếu không có file `.env`, docker-compose sẽ dùng giá trị mặc định.

## 🔧 Yêu cầu

- Docker (version 20.10+)
- Docker Compose (version 1.29+)
- Port 8080 và 1435 chưa bị sử dụng

