<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
    <title>Bảng Điều Khiển Quản Trị - ACCOUNT HUB</title>
</head>
<body>
    <div class="welcome-banner">
        <div class="row align-items-center">
            <div class="col-md-8">
                <span class="badge bg-white text-primary fw-bold px-3 py-2 mb-2">
                    <i class="bi bi-shield-check"></i> Xác thực thành công (Spring Security)
                </span>
                <h2 class="fw-bold mb-1">
                    Xin chào, <c:out value="${currentUser.fullName}" default="Quản Trị Viên" />!
                </h2>
                <p class="mb-0 text-white-50">
                    Chào mừng bạn đến với trang quản trị <strong>ACCOUNT HUB</strong> (JSP & SiteMesh 3).
                </p>
            </div>
            <div class="col-md-4 text-md-end mt-3 mt-md-0">
                <a href="<c:url value='/admin/categories/searchpaginated'/>" class="btn btn-light text-primary fw-semibold shadow-sm me-2">
                    <i class="bi bi-tags-fill me-1"></i> Quản lý Category
                </a>
                <a href="<c:url value='/admin/profile'/>" class="btn btn-outline-light fw-semibold">
                    <i class="bi bi-person-circle me-1"></i> Hồ Sơ
                </a>
            </div>
        </div>
    </div>

    <!-- Quick Cards Grid -->
    <div class="row g-4 mb-4">
        <div class="col-md-4">
            <div class="info-card">
                <div class="d-flex align-items-center gap-3 mb-3">
                    <div class="p-3 bg-primary bg-opacity-10 text-primary rounded-3">
                        <i class="bi bi-person-badge fs-3"></i>
                    </div>
                    <div>
                        <div class="info-label">Tài khoản</div>
                        <div class="info-value text-truncate"><c:out value="${currentUser.username}" /></div>
                    </div>
                </div>
                <p class="text-muted small mb-0">Tên định danh đăng nhập trong hệ thống.</p>
            </div>
        </div>

        <div class="col-md-4">
            <div class="info-card">
                <div class="d-flex align-items-center gap-3 mb-3">
                    <div class="p-3 bg-info bg-opacity-10 text-info rounded-3">
                        <i class="bi bi-envelope-at fs-3"></i>
                    </div>
                    <div>
                        <div class="info-label">Địa chỉ Email</div>
                        <div class="info-value text-truncate" style="font-size: 1rem;"><c:out value="${currentUser.email}" /></div>
                    </div>
                </div>
                <p class="text-muted small mb-0">Email xác thực liên kết tài khoản.</p>
            </div>
        </div>

        <div class="col-md-4">
            <div class="info-card">
                <div class="d-flex align-items-center gap-3 mb-3">
                    <div class="p-3 bg-success bg-opacity-10 text-success rounded-3">
                        <i class="bi bi-tags-fill fs-3"></i>
                    </div>
                    <div>
                        <div class="info-label">Quản lý Category</div>
                        <div class="info-value">
                            <a href="<c:url value='/admin/categories/searchpaginated'/>" class="text-decoration-none">
                                Xem danh sách <i class="bi bi-arrow-right-short"></i>
                            </a>
                        </div>
                    </div>
                </div>
                <p class="text-muted small mb-0">Hỗ trợ tìm kiếm phân trang theo yêu cầu giảng viên.</p>
            </div>
        </div>
    </div>

    <!-- Card Chi tiết bảo mật -->
    <div class="card border-0 shadow-sm rounded-4 p-4">
        <h5 class="fw-bold text-primary mb-3">
            <i class="bi bi-cpu-fill me-2"></i> Kiến Trúc & Cấu Hình Đã Triển Khai
        </h5>
        <div class="row g-3">
            <div class="col-md-6">
                <ul class="list-group list-group-flush small">
                    <li class="list-group-item d-flex justify-content-between align-items-center px-0">
                        <span class="text-muted">Giao diện (View Engine):</span>
                        <span class="badge bg-primary text-white">JSP + JSTL + Tomcat Jasper</span>
                    </li>
                    <li class="list-group-item d-flex justify-content-between align-items-center px-0">
                        <span class="text-muted">Layout Decorator:</span>
                        <span class="badge bg-info text-white">SiteMesh 3 (web.jsp & admin.jsp)</span>
                    </li>
                    <li class="list-group-item d-flex justify-content-between align-items-center px-0">
                        <span class="text-muted">Mã hóa mật khẩu:</span>
                        <span class="badge bg-success text-white">BCryptPasswordEncoder</span>
                    </li>
                </ul>
            </div>
            <div class="col-md-6">
                <ul class="list-group list-group-flush small">
                    <li class="list-group-item d-flex justify-content-between align-items-center px-0">
                        <span class="text-muted">Cổng máy chủ:</span>
                        <span class="fw-semibold">8090 (server.port=8090)</span>
                    </li>
                    <li class="list-group-item d-flex justify-content-between align-items-center px-0">
                        <span class="text-muted">Xử lý tiếng Việt (Unicode):</span>
                        <span class="badge bg-success text-white">CharacterEncodingFilter UTF-8</span>
                    </li>
                    <li class="list-group-item d-flex justify-content-between align-items-center px-0">
                        <span class="text-muted">Tìm kiếm phân trang:</span>
                        <span class="badge bg-primary text-white">Spring Data JPA Pageable</span>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</body>
</html>
