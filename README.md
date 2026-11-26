# HƯỚNG DẪN CÀI ĐẶT VÀ SỬ DỤNG DỰ ÁN ACCOUNT HUB

## 1. Giới thiệu
**ACCOUNT HUB** (`Account Management System`) là ứng dụng web Spring Boot hoàn chỉnh đáp ứng đầy đủ 3 nội dung chính:
- **Cấu hình JSP + JSTL + Apache Tomcat Embedded + SiteMesh 3** (bố cục tách biệt giữa trang công khai `web.jsp` và trang quản trị `admin.jsp`, hỗ trợ Unicode tiếng Việt đầy đủ).
- **Quản lý Category (CRUD) kèm Tìm kiếm và Phân trang** (`Page<Category> findByNameContaining(String keyword, Pageable page)`), hỗ trợ giữ từ khóa tìm kiếm khi đổi trang hoặc thay đổi `page size` (`3, 5, 10, 15, 20`).
- **Đăng ký, Đăng nhập và Đăng xuất tài khoản** bảo mật với **Spring Security** & **BCrypt** (hỗ trợ đăng nhập bằng Email hoặc Username, bảo vệ CSRF, kiểm tra tài khoản bị khóa, ngăn cache sau khi đăng xuất).

### Công nghệ sử dụng
- **Ngôn ngữ & Runtime:** Java 21
- **Framework:** Spring Boot 3.3.5 (Spring MVC, Spring Security, Spring Data JPA / Hibernate, Bean Validation)
- **View & Layout:** JSP, Jakarta JSTL, Apache Tomcat Embedded Jasper, SiteMesh 3 (`3.3.0-RC1`)
- **Frontend:** HTML5, CSS3, JavaScript, Bootstrap 5.3
- **Cơ sở dữ liệu:** MySQL (hỗ trợ chạy nhanh bằng H2 file-based chế độ MySQL khi chưa bật dịch vụ MySQL)
- **Build Tool:** Apache Maven (`mvnw.cmd`)

---

## 2. Cấu trúc thư mục chính
- `src/main/java/com/accounthub/`:
  - `config/`: `SecurityConfig`, `CustomAuthenticationFailureHandler`, `CustomSiteMeshFilter`, `WebConfig`, `JSPStaticResourceConfigurer`, `TomcatJSPConfiguration`, `DataInitializer`
  - `controller/`: `HomeController`, `AuthController`, `CategoryController`
  - `dto/`: `RegisterDto`
  - `entity/`: `User`, `Category`
  - `repository/`: `UserRepository`, `CategoryRepository`
  - `service/`: `UserService`, `UserServiceImpl`, `CategoryService`, `CategoryServiceImpl`, `CustomUserDetailsService`
- `src/main/webapp/`:
  - `WEB-INF/views/decorators/`: `web.jsp`, `admin.jsp`
  - `WEB-INF/views/web/`: `home.jsp`, `login.jsp`, `register.jsp`
  - `WEB-INF/views/admin/`: `dashboard.jsp`, `profile.jsp`, `category/list.jsp`, `category/add.jsp`, `category/edit.jsp`
  - `static/`: `css/style.css`, `js/main.js`, `images/logo.svg`
- `database/`: `schema.sql`, `data.sql`
- `run.bat`: Tệp khởi chạy nhanh trên Windows

---

## 3. Cấu hình Cơ sở dữ liệu

### Chế độ mặc định (H2 File-based tương thích MySQL)
Khi chạy trực tiếp mà chưa cấu hình MySQL, ứng dụng lưu dữ liệu bền vững tại `./database/account_hub` (MySQL compatibility mode).

### Chế độ MySQL thực tế (Production Profile)
1. Khởi động MySQL Server trên máy (`localhost:3306`).
2. Chạy script [`database/schema.sql`](database/schema.sql) và [`database/data.sql`](database/data.sql) (hoặc để Hibernate tự tạo bảng khi khởi động).
3. Chạy ứng dụng với profile `mysql`:
```cmd
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=mysql
```
Có thể truyền biến môi trường `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` (mặc định user `root`, password rỗng).

### Tài khoản mẫu & dữ liệu khởi tạo (`DataInitializer`)
Khi bảng `users` / `categories` trống, hệ thống tự động tạo:
- `nguyenbao` / `Password@123` (Email: `nguyenbaongan@example.com`, Trạng thái: `ACTIVE`)
- `admin` / `Admin@123` (Email: `admin@accounthub.vn`, Trạng thái: `ACTIVE`)
- `lockeduser` / `Password@123` (Email: `locked@example.com`, Trạng thái: `LOCKED` — dùng kiểm thử chặn đăng nhập)
- 8 danh mục mẫu phục vụ kiểm thử tìm kiếm & phân trang.

---

## 4. Cách chạy ứng dụng trên Visual Studio Code
- **Cách 1:** Nhấn đúp chuột vào tệp `run.bat`.
- **Cách 2:** Mở Terminal trong VS Code tại thư mục `D:\BT26-11` và chạy:
```cmd
.\mvnw.cmd spring-boot:run
```
Sau khi server khởi động trên cổng **8090**, truy cập các đường dẫn:
- Trang chủ: `http://localhost:8090/`
- Đăng nhập: `http://localhost:8090/login`
- Đăng ký: `http://localhost:8090/register`
- Quản trị Dashboard (yêu cầu đăng nhập): `http://localhost:8090/admin/dashboard`
- Quản lý Category & Phân trang (yêu cầu đăng nhập): `http://localhost:8090/admin/categories/searchpaginated`
- Hồ sơ tài khoản (yêu cầu đăng nhập): `http://localhost:8090/admin/profile`

---

## 5. Kiểm thử tự động
Chạy toàn bộ Unit Test và Integration Test (bao gồm Tomcat thật + JSP + SiteMesh + Security + Category CRUD):
```cmd
.\mvnw.cmd test
```
Đóng gói WAR:
```cmd
.\mvnw.cmd package -DskipTests
```
