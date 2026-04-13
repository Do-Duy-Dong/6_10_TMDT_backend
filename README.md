# 6_10_TMDT_backend

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
