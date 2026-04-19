# 6_10_TMDT_backend
# 🛒 Shop Laptop Service

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.X-brightgreen.svg?logo=spring)](https://spring.io/projects/spring-boot)
[![Docker](https://img.shields.io/badge/Docker-Enabled-blue.svg?logo=docker)](https://www.docker.com/)
[![Liquibase](https://img.shields.io/badge/Liquibase-Migration-red.svg?logo=liquibase)](https://www.liquibase.org/)
[![Swagger](https://img.shields.io/badge/Swagger%20UI-API%20Docs-85EA2D.svg?logo=swagger)](https://swagger.io/)

Hệ thống backend cung cấp các API quản lý và vận hành cửa hàng bán Laptop. Dự án được triển khai bằng framework Spring Boot, hỗ trợ container hóa với Docker và quản lý lịch sử cơ sở dữ liệu (Database Migration) bằng Liquibase.

---

## Tech Stack

- **Framework**: Spring Boot (Java)
- **Database**: PostgreSQL
- **Database Migration**: Liquibase
- **Containerization**: Docker & Docker Compose
- **API Documentation**: Swagger UI / OpenAPI 3.0

---

## Yêu cầu cấu hình (Prerequisites)

Để có thể khởi chạy dự án tại local, máy tính của bạn cần cài đặt sẵn:
- [Java Development Kit (JDK) 17+](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [Apache Maven](https://maven.apache.org/)
- [Docker & Docker Compose](https://www.docker.com/products/docker-desktop)

---

## Hướng dẫn cài đặt & Khởi chạy (Getting Started)

### Bước 1: Khởi tạo Database với Docker
Sử dụng Docker Compose để tạo container cơ sở dữ liệu một cách nhanh chóng.
```bash
cd docker
docker compose up -d 
# Hoặc docker-compose up -d tùy thuộc vào phiên bản docker của bạn
```

### Bước 2: Build dự án và tải các thư viện lệ thuộc
Quay lại thư mục gốc của dự án và chạy Maven:
```bash
mvn clean install
```
*(Lưu ý: Bạn có thể thêm flag `-DskipTests` nếu muốn bỏ qua bước chạy test khi build).*

### Bước 3: Cập nhật cấu trúc Database (Migration)
Dự án sử dụng Liquibase để quản lý cấu trúc bảng. Chạy lệnh sau để tự động tạo schema và các bảng cần thiết:
```bash
mvn liquibase:update
```

### Bước 4: Chạy Application
Sau khi database chuẩn bị xong, bạn chạy ứng dụng qua lệnh:
```bash
mvn spring-boot:run
```
Hoặc khởi chạy file `ShopServiceApplication.java` trực tiếp thông qua IDE của bạn (IntelliJ IDEA, Eclipse, VS Code...).

---

##  Hướng dẫn quản lý Database (Quan trọng)

Để đồng bộ hóa môi trường giữa các thành viên, hãy tuân thủ nguyên tắc sau:
1. **Tuyệt đối không sửa bảng trực tiếp** bằng các tool/IDE quản trị database (DBeaver, DataGrip, pgAdmin...).
2. Bất cứ khi nào cần tạo bảng mới hoặc sửa đổi cấu trúc CSDL, bạn **cần phải tạo file changelog (`.xml`)** tương ứng. 
3. Sau khi tạo/sửa file changelog, thực thi lại lệnh `mvn liquibase:update` để Liquibase tự động áp dụng các thay đổi xuống database một cách an toàn.

---

##  API Documentation (Swagger UI)

Ứng dụng cung cấp sẵn giao diện UI để tra cứu và test APIs qua Swagger. 

- **Link truy cập**: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

### Phân quyền & Authentication

Dự án hiện đang sử dụng cơ chế **API Key** thay vì JWT hay Basic Auth. 
Để gọi các API an toàn trên Swagger, bạn cần cấu hình Header `X-API-KEY`.

| Thuộc tính | Giá trị cấu hình |
| --- | --- |
| **Header Name** | `X-API-KEY` |
| **Key mặc định cho Admin** | `api-key-admin` *(Có toàn quyền thao tác, bao gồm các endpoint của Admin)* |
| **Key mặc định cho User** | `api-key-user` *(Chỉ cấp quyền sử dụng cho role Customer cơ bản)* |

---
## Quy tắc đóng góp (Coding & Commit Rules)

Tài liệu này quy định cách **code**, **tạo branch**, **commit** và **mở PR** cho dự án.

### 1) Quy tắc Git (Branch / PR)
- **Không commit trực tiếp lên `main`** (trừ trường hợp khẩn cấp và đã thống nhất).
- Tạo branch theo mục đích:
  - `feat/<ten-ngan-gon>`: làm tính năng
  - `fix/<ten-loi>`: sửa bug
  - `refactor/<pham-vi>`: refactor
- Mỗi branch nên tương ứng **1 task/issue** (nếu có), tránh gom nhiều việc không liên quan.
- PR cần có:
  - Mô tả rõ **làm gì / vì sao / cách test**

### 2) Quy tắc coding (chung)
- Ưu tiên code **dễ đọc, dễ bảo trì** hơn code “khó hiểu nhưng ngắn”.
- **Không hard-code** thông tin môi trường/nhạy cảm:
  - Dùng `.env` / config theo môi trường.
- **Không commit file nhạy cảm**: `.env`, key, credential, dump DB, log nặng, file build.
- Tách chức năng rõ ràng:
  - Hàm/module nên làm **một nhiệm vụ cụ thể**
  - Hạn chế hàm quá dài, tránh lặp code

### 3) Quy tắc đặt tên
- Tên biến/hàm rõ nghĩa, tránh viết tắt khó hiểu.
- Quy ước gợi ý:
  - `camelCase` cho biến/hàm
  - `PascalCase` cho class
  - `UPPER_SNAKE_CASE` cho hằng số
- Tên branch/commit/PR: không dấu, dùng `-` hoặc `_` nhất quán.

### 4) Quy tắc commit (Conventional Commits)
**Format:**

`<type>(<scope>): <message>`

**Type chuẩn:**
- `feat`: thêm tính năng
- `fix`: sửa bug
- `refactor`: cải tổ code (không đổi behavior)
- `perf`: tối ưu hiệu năng
- `test`: thêm/sửa test
- `docs`: tài liệu
- `chore`: việc phụ (deps, config, scripts…)
- `ci`: CI/CD
- `build`: build system
