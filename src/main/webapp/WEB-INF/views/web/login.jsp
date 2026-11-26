<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng nhập - ACCOUNT HUB</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
    <link rel="stylesheet" href="<c:url value='/static/css/style.css'/>">
</head>
<body>
    <div class="auth-wrapper">
        <div class="auth-card">
            <div class="brand-header">
                <div class="brand-logo-icon">
                    <i class="bi bi-shield-lock-fill"></i>
                </div>
                <h1 class="brand-title">ACCOUNT HUB</h1>
                <p class="brand-subtitle">Hệ thống quản lý tài khoản & category</p>
            </div>

            <!-- Thông báo Lỗi -->
            <c:if test="${not empty errorMessage}">
                <div class="alert-custom alert-danger-custom" role="alert">
                    <i class="bi bi-exclamation-triangle-fill fs-5"></i>
                    <div>
                        <strong class="d-block">Đăng nhập không thành công</strong>
                        <span><c:out value="${errorMessage}"/></span>
                    </div>
                </div>
            </c:if>

            <!-- Thông báo Đăng xuất -->
            <c:if test="${not empty logoutMessage}">
                <div class="alert-custom alert-info-custom" role="alert">
                    <i class="bi bi-info-circle-fill fs-5"></i>
                    <div>
                        <span><c:out value="${logoutMessage}"/></span>
                    </div>
                </div>
            </c:if>

            <!-- Thông báo Đăng ký thành công -->
            <c:if test="${not empty successMessage}">
                <div class="alert-custom alert-success-custom" role="alert">
                    <i class="bi bi-check-circle-fill fs-5"></i>
                    <div>
                        <span><c:out value="${successMessage}"/></span>
                    </div>
                </div>
            </c:if>

            <!-- Form Đăng nhập -->
            <form action="<c:url value='/login'/>" method="post" class="needs-validation" novalidate>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                <div class="mb-3">
                    <label for="identifier" class="form-label">Tên đăng nhập hoặc Email <span class="text-danger">*</span></label>
                    <div class="input-group-modern">
                        <i class="bi bi-person-fill input-icon-left"></i>
                        <input type="text" 
                               id="identifier" 
                               name="identifier" 
                               value="<c:out value='${identifier}'/>" 
                               class="form-control-modern" 
                               placeholder="Nhập username hoặc email" 
                               required 
                               autocomplete="username" 
                               autofocus>
                    </div>
                </div>

                <div class="mb-4">
                    <label for="password" class="form-label">Mật khẩu <span class="text-danger">*</span></label>
                    <div class="input-group-modern">
                        <i class="bi bi-lock-fill input-icon-left"></i>
                        <input type="password" 
                               id="password" 
                               name="password" 
                               class="form-control-modern" 
                               placeholder="Nhập mật khẩu" 
                               required 
                               autocomplete="current-password">
                        <button type="button" class="btn-toggle-pwd" data-target="password" aria-label="Hiện mật khẩu">
                            <i class="bi bi-eye"></i>
                        </button>
                    </div>
                </div>

                <button type="submit" class="btn-primary-gradient mb-3" id="btnLogin">
                    <i class="bi bi-box-arrow-in-right"></i> Đăng Nhập
                </button>

                <div class="text-center mt-3 pt-2 border-top">
                    <span class="text-muted small">Chưa có tài khoản?</span>
                    <a href="<c:url value='/register'/>" class="fw-semibold text-decoration-none ms-1 text-primary">
                        Đăng ký ngay <i class="bi bi-arrow-right-short"></i>
                    </a>
                    <div class="mt-2">
                        <a href="<c:url value='/'/>" class="text-muted small text-decoration-none">
                            <i class="bi bi-house-door me-1"></i> Quay lại Trang chủ
                        </a>
                    </div>
                </div>
            </form>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="<c:url value='/static/js/main.js'/>"></script>
</body>
</html>
