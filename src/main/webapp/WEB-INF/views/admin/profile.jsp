<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
    <title>Hồ Sơ Cá Nhân - ACCOUNT HUB</title>
</head>
<body>
    <div class="row justify-content-center py-4">
        <div class="col-md-9">
            <div class="card border-0 shadow-sm rounded-4 overflow-hidden mb-4">
                <div style="background: var(--primary-gradient); height: 130px; position: relative;">
                    <div style="position: absolute; bottom: -45px; left: 35px;">
                        <div class="rounded-circle bg-white p-1 shadow">
                            <div class="rounded-circle d-flex align-items-center justify-content-center text-white" 
                                 style="width: 85px; height: 85px; background: #2563eb; font-size: 2.3rem;">
                                <i class="bi bi-person-fill"></i>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="card-body pt-5 px-4 pb-4">
                    <div class="d-flex justify-content-between align-items-start mt-2 mb-4">
                        <div>
                            <h3 class="fw-bold mb-0 text-dark"><c:out value="${currentUser.fullName}" /></h3>
                            <span class="text-muted">@<c:out value="${currentUser.username}" /></span>
                        </div>
                        <div>
                            <c:choose>
                                <c:when test="${currentUser.active}">
                                    <span class="badge bg-success-subtle text-success px-3 py-2 fw-bold">
                                        <i class="bi bi-check-circle-fill me-1"></i> Đang hoạt động
                                    </span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge bg-danger-subtle text-danger px-3 py-2 fw-bold">
                                        <i class="bi bi-lock-fill me-1"></i> Đã bị khóa
                                    </span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>

                    <div class="row g-3">
                        <div class="col-sm-6">
                            <div class="p-3 bg-light rounded-3">
                                <div class="text-muted small fw-semibold text-uppercase">Mã định danh (ID)</div>
                                <div class="fw-bold fs-6 text-dark mt-1">${currentUser.id}</div>
                            </div>
                        </div>

                        <div class="col-sm-6">
                            <div class="p-3 bg-light rounded-3">
                                <div class="text-muted small fw-semibold text-uppercase">Họ và tên</div>
                                <div class="fw-bold fs-6 text-dark mt-1"><c:out value="${currentUser.fullName}" /></div>
                            </div>
                        </div>

                        <div class="col-sm-6">
                            <div class="p-3 bg-light rounded-3">
                                <div class="text-muted small fw-semibold text-uppercase">Tên đăng nhập</div>
                                <div class="fw-bold fs-6 text-dark mt-1"><c:out value="${currentUser.username}" /></div>
                            </div>
                        </div>

                        <div class="col-sm-6">
                            <div class="p-3 bg-light rounded-3">
                                <div class="text-muted small fw-semibold text-uppercase">Địa chỉ Email</div>
                                <div class="fw-bold fs-6 text-dark mt-1 text-break"><c:out value="${currentUser.email}" /></div>
                            </div>
                        </div>

                        <div class="col-sm-6">
                            <div class="p-3 bg-light rounded-3">
                                <div class="text-muted small fw-semibold text-uppercase">Ngày tạo tài khoản</div>
                                <div class="fw-bold fs-6 text-dark mt-1"><c:out value="${currentUser.formattedCreatedAt}" /></div>
                            </div>
                        </div>

                        <div class="col-sm-6">
                            <div class="p-3 bg-light rounded-3">
                                <div class="text-muted small fw-semibold text-uppercase">Trạng thái xác thực</div>
                                <div class="fw-bold fs-6 text-dark mt-1">${currentUser.status}</div>
                            </div>
                        </div>
                    </div>

                    <div class="d-flex justify-content-between align-items-center mt-4 pt-3 border-top">
                        <a href="<c:url value='/admin/dashboard'/>" class="btn btn-outline-secondary">
                            <i class="bi bi-arrow-left me-1"></i> Về Dashboard
                        </a>
                        <a href="<c:url value='/admin/categories/searchpaginated'/>" class="btn btn-primary">
                            <i class="bi bi-tags me-1"></i> Quản lý Category
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
