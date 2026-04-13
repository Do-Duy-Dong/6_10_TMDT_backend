# 6_10_TMDT_backend

## Quy tắc đóng góp (Coding & Commit Rules)

Tài liệu này quy định cách **code**, **tạo branch**, **commit** và **mở PR** cho dự án.

### 1) Quy tắc Git (Branch / PR)
- **Không commit trực tiếp lên `main`** (trừ trường hợp khẩn cấp và đã thống nhất).
- Tạo branch theo mục đích:
  - `feature/<ten-ngan-gon>`: làm tính năng
  - `fix/<ten-loi>`: sửa bug
  - `refactor/<pham-vi>`: refactor
  - `chore/<viec-linh-tinh>`: cấu hình, scripts, deps
- Mỗi branch nên tương ứng **1 task/issue** (nếu có), tránh gom nhiều việc không liên quan.
- PR cần có:
  - Mô tả rõ **làm gì / vì sao / cách test**
  - Hạn chế PR quá lớn (khuyến nghị nhỏ và dễ review)

### 2) Quy tắc coding (chung)
- Ưu tiên code **dễ đọc, dễ bảo trì** hơn code “khó hiểu nhưng ngắn”.
- **Không hard-code** thông tin môi trường/nhạy cảm:
  - URL, secret key, token, password, API key…
  - Dùng `.env` / config theo môi trường.
- **Không commit file nhạy cảm**: `.env`, key, credential, dump DB, log nặng, file build.
- Tách chức năng rõ ràng:
  - Hàm/module nên làm **một nhiệm vụ cụ thể**
  - Hạn chế hàm quá dài, tránh lặp code
- Xử lý lỗi đầy đủ:
  - Không “nuốt” lỗi (catch rỗng)
  - Trả lỗi có cấu trúc, message rõ ràng

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

**Ví dụ commit đúng:**
- `feat(auth): add jwt refresh token`
- `fix(order): handle null address`
- `refactor(product): simplify filter pipeline`
- `chore: bump dependencies`

**Quy tắc commit:**
- Message **ngắn gọn**, mô tả đúng thay đổi.
- Mỗi commit nên ở trạng thái **có thể chạy/build** (tránh commit nửa vời).
- Trước khi commit/push: chạy format/lint/test (nếu dự án có).

### 5) Checklist trước khi push / mở PR
- Pull/rebase cập nhật mới nhất từ `main`, xử lý conflict (nếu có).
- Không để lại:
  - `console.log` / log debug thừa
  - code chết, comment vô nghĩa
- Tự test luồng chính:
  - API chạy được
  - Case quan trọng không gãy
- PR mô tả rõ:
  - What / Why / How to test
  - Đính kèm log/screenshot nếu cần

### 6) Review & merge
- Ít nhất 1 người review (nếu làm nhóm).
- Ưu tiên **Squash & merge** để lịch sử gọn (trừ khi team muốn giữ từng commit).