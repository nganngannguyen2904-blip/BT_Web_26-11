<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><sitemesh:write property="title"/></title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
    <link rel="stylesheet" href="<c:url value='/static/css/style.css'/>">
    <sitemesh:write property="head"/>
</head>
<body>
    <nav class="app-navbar">
        <div class="container d-flex justify-content-between align-items-center">
            <a href="<c:url value='/admin/dashboard'/>" class="d-flex align-items-center gap-2 text-decoration-none">
                <div class="brand-logo-icon" style="width: 40px; height: 40px; font-size: 1.25rem; margin-bottom: 0;">
                    <i class="bi bi-shield-lock-fill"></i>
                </div>
                <span class="nav-brand-text">ACCOUNT HUB - ADMIN</span>
            </a>
            <div class="d-flex align-items-center gap-2">
                <a href="<c:url value='/admin/dashboard'/>" class="nav-link-custom">
                    <i class="bi bi-speedometer2"></i> Dashboard
                </a>
                <a href="<c:url value='/admin/categories/searchpaginated'/>" class="nav-link-custom">
                    <i class="bi bi-tags-fill"></i> Quản lý Category
                </a>
                <a href="<c:url value='/admin/profile'/>" class="nav-link-custom">
                    <i class="bi bi-person-circle"></i> Hồ Sơ
                </a>
                <c:if test="${pageContext.request.userPrincipal != null}">
                    <span class="badge bg-primary-subtle text-primary px-2 py-2 fw-semibold ms-1">
                        <i class="bi bi-person-check-fill me-1"></i><c:out value="${pageContext.request.userPrincipal.name}"/>
                    </span>
                </c:if>
                <form action="<c:url value='/logout'/>" method="post" class="d-inline ms-2">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <button type="submit" class="btn btn-outline-danger btn-sm px-3 py-1 fw-semibold d-inline-flex align-items-center gap-1">
                        <i class="bi bi-box-arrow-right"></i> Đăng Xuất
                    </button>
                </form>
            </div>
        </div>
    </nav>

    <main class="content-wrapper">
        <div class="container">
            <sitemesh:write property="body"/>
        </div>
    </main>

    <footer class="app-footer">
        <div class="container">
            <p class="mb-0">&copy; 2026 <strong>ACCOUNT HUB</strong> - Trang quản trị hệ thống.</p>
        </div>
    </footer>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="<c:url value='/static/js/main.js'/>"></script>
</body>
</html>
