<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng ký tài khoản - ACCOUNT HUB</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
    <link rel="stylesheet" href="<c:url value='/static/css/style.css'/>">
</head>
<body>
    <div class="auth-wrapper">
        <div class="auth-card" style="max-width: 520px;">
            <div class="brand-header">
                <div class="brand-logo-icon">
                    <i class="bi bi-person-plus-fill"></i>
                </div>
                <h1 class="brand-title">Tạo Tài Khoản Mới</h1>
                <p class="brand-subtitle">Chào mừng bạn đến với hệ thống ACCOUNT HUB</p>
            </div>

            <c:if test="${not empty errorMessage}">
                <div class="alert-custom alert-danger-custom" role="alert">
                    <i class="bi bi-exclamation-triangle-fill fs-5"></i>
                    <div>
                        <strong class="d-block">Đăng ký không thành công</strong>
                        <span><c:out value="${errorMessage}"/></span>
                    </div>
                </div>
            </c:if>

            <form:form action="${pageContext.request.contextPath}/register" modelAttribute="user" method="post" novalidate="true">

                <!-- Họ và tên -->
                <div class="mb-3">
                    <label for="fullName" class="form-label">Họ và tên <span class="text-danger">*</span></label>
                    <div class="input-group-modern">
                        <i class="bi bi-person-badge-fill input-icon-left"></i>
                        <form:input path="fullName" id="fullName" cssClass="form-control-modern" placeholder="Nguyễn Văn A" required="required"/>
                    </div>
                    <form:errors path="fullName" cssClass="invalid-feedback-custom"/>
                </div>

                <!-- Email -->
                <div class="mb-3">
                    <label for="email" class="form-label">Địa chỉ Email <span class="text-danger">*</span></label>
                    <div class="input-group-modern">
                        <i class="bi bi-envelope-fill input-icon-left"></i>
                        <form:input path="email" id="email" type="email" cssClass="form-control-modern" placeholder="name@example.com" required="required"/>
                    </div>
                    <form:errors path="email" cssClass="invalid-feedback-custom"/>
                </div>

                <!-- Username -->
                <div class="mb-3">
                    <label for="username" class="form-label">Tên đăng nhập <span class="text-danger">*</span></label>
                    <div class="input-group-modern">
                        <i class="bi bi-person-fill input-icon-left"></i>
                        <form:input path="username" id="username" cssClass="form-control-modern" placeholder="username123" required="required"/>
                    </div>
                    <form:errors path="username" cssClass="invalid-feedback-custom"/>
                    <div class="text-muted small mt-1">Chỉ gồm chữ cái, chữ số và dấu gạch dưới (4-50 ký tự)</div>
                </div>

                <!-- Mật khẩu -->
                <div class="mb-3">
                    <label for="password" class="form-label">Mật khẩu <span class="text-danger">*</span></label>
                    <div class="input-group-modern">
                        <i class="bi bi-lock-fill input-icon-left"></i>
                        <form:password path="password" id="password" cssClass="form-control-modern" placeholder="Tối thiểu 8 ký tự, có chữ hoa, thường & số" required="required"/>
                        <button type="button" class="btn-toggle-pwd" data-target="password" aria-label="Hiện mật khẩu">
                            <i class="bi bi-eye"></i>
                        </button>
                    </div>
                    <div class="password-meter">
                        <div class="password-meter-fill" id="pwdMeterFill"></div>
                    </div>
                    <div class="small mt-1" id="pwdStrengthText"></div>
                    <form:errors path="password" cssClass="invalid-feedback-custom"/>
                </div>

                <!-- Xác nhận mật khẩu -->
                <div class="mb-4">
                    <label for="confirmPassword" class="form-label">Xác nhận mật khẩu <span class="text-danger">*</span></label>
                    <div class="input-group-modern">
                        <i class="bi bi-shield-check input-icon-left"></i>
                        <form:password path="confirmPassword" id="confirmPassword" cssClass="form-control-modern" placeholder="Nhập lại mật khẩu" required="required"/>
                        <button type="button" class="btn-toggle-pwd" data-target="confirmPassword" aria-label="Hiện mật khẩu">
                            <i class="bi bi-eye"></i>
                        </button>
                    </div>
                    <div id="confirmMatchText"></div>
                    <form:errors path="confirmPassword" cssClass="invalid-feedback-custom"/>
                </div>

                <button type="submit" class="btn-primary-gradient mb-3" id="btnRegister">
                    <i class="bi bi-check2-circle"></i> Đăng Ký Tài Khoản
                </button>

                <div class="text-center mt-3 pt-2 border-top">
                    <span class="text-muted small">Đã có tài khoản?</span>
                    <a href="<c:url value='/login'/>" class="fw-semibold text-decoration-none ms-1 text-primary">
                        Đăng nhập tại đây <i class="bi bi-arrow-right-short"></i>
                    </a>
                    <div class="mt-2">
                        <a href="<c:url value='/'/>" class="text-muted small text-decoration-none">
                            <i class="bi bi-house-door me-1"></i> Quay lại Trang chủ
                        </a>
                    </div>
                </div>
            </form:form>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="<c:url value='/static/js/main.js'/>"></script>
</body>
</html>
