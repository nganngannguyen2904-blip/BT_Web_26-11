<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Trang Chủ - ACCOUNT HUB</title>
</head>
<body>
    <div class="container py-5">
        <div class="p-5 mb-4 bg-light rounded-4 shadow-sm text-center">
            <div class="brand-logo-icon mx-auto mb-3" style="width: 70px; height: 70px; font-size: 2.2rem;">
                <i class="bi bi-shield-lock-fill"></i>
            </div>
            <h1 class="display-5 fw-bold text-primary">ACCOUNT HUB & CATEGORY MANAGER</h1>
            <p class="fs-5 text-muted col-md-8 mx-auto">
                Hệ thống Quản lý Category sản phẩm kết hợp Phân quyền, Đăng ký, Đăng nhập, Đăng xuất bảo mật bằng Spring Security, JPA & JSP SiteMesh 3.
            </p>
            <div class="d-flex justify-content-center gap-3 mt-4">
                <a href="<c:url value='/admin/categories/searchpaginated'/>" class="btn btn-primary btn-lg px-4">
                    <i class="bi bi-tags-fill me-1"></i> Quản lý Category
                </a>
                <a href="<c:url value='/login'/>" class="btn btn-outline-primary btn-lg px-4">
                    <i class="bi bi-box-arrow-in-right me-1"></i> Đăng Nhập
                </a>
                <a href="<c:url value='/register'/>" class="btn btn-outline-secondary btn-lg px-4">
                    <i class="bi bi-person-plus-fill me-1"></i> Đăng Ký
                </a>
            </div>
        </div>

        <div class="row g-4 py-3">
            <div class="col-md-4">
                <div class="info-card">
                    <div class="p-3 bg-primary bg-opacity-10 text-primary rounded-3 d-inline-block mb-3">
                        <i class="bi bi-shield-check fs-3"></i>
                    </div>
                    <h5 class="fw-bold">Bảo Mật Spring Security</h5>
                    <p class="text-muted small">
                        Mã hóa mật khẩu bằng BCrypt, chống SQL Injection qua JPA, phân quyền truy cập và bảo vệ CSRF.
                    </p>
                </div>
            </div>
            <div class="col-md-4">
                <div class="info-card">
                    <div class="p-3 bg-success bg-opacity-10 text-success rounded-3 d-inline-block mb-3">
                        <i class="bi bi-search fs-3"></i>
                    </div>
                    <h5 class="fw-bold">Tìm Kiếm & Phân Trang</h5>
                    <p class="text-muted small">
                        Tìm kiếm linh hoạt theo tên danh mục, phân trang tùy biến (size 3, 5, 10, 15, 20), giữ từ khóa khi đổi trang.
                    </p>
                </div>
            </div>
            <div class="col-md-4">
                <div class="info-card">
                    <div class="p-3 bg-info bg-opacity-10 text-info rounded-3 d-inline-block mb-3">
                        <i class="bi bi-layout-text-window-reverse fs-3"></i>
                    </div>
                    <h5 class="fw-bold">JSP + SiteMesh 3</h5>
                    <p class="text-muted small">
                        Layout kiến trúc SiteMesh 3 phân tách decorator Web và Admin, giao diện Bootstrap 5 hiện đại.
                    </p>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
