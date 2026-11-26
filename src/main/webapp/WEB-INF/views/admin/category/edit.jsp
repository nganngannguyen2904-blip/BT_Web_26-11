<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
    <title>Cập nhật Category - ACCOUNT HUB</title>
</head>
<body>
    <div class="row justify-content-center py-4">
        <div class="col-md-7">
            <div class="card border-0 shadow-sm rounded-4">
                <div class="card-header bg-white border-0 pt-4 px-4 pb-0">
                    <h4 class="fw-bold text-primary mb-1">
                        <i class="bi bi-pencil-square me-2"></i> Cập Nhật Category #${category.id}
                    </h4>
                    <p class="text-muted small">Chỉnh sửa thông tin danh mục sản phẩm</p>
                </div>
                <div class="card-body p-4">
                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                            <i class="bi bi-exclamation-triangle-fill me-2"></i><c:out value="${errorMessage}"/>
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                        </div>
                    </c:if>
                    <c:if test="${not empty message}">
                        <div class="alert alert-info alert-dismissible fade show" role="alert">
                            <i class="bi bi-info-circle-fill me-2"></i><c:out value="${message}"/>
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                        </div>
                    </c:if>
                    <form action="<c:url value='/admin/categories/save'/>" method="post">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <input type="hidden" name="id" value="${category.id}" />

                        <div class="mb-3">
                            <label for="name" class="form-label fw-semibold">Tên Danh Mục <span class="text-danger">*</span></label>
                            <input type="text" name="name" id="name" value="<c:out value='${category.name}'/>" class="form-control" required autofocus />
                        </div>

                        <div class="mb-3">
                            <label for="images" class="form-label fw-semibold">Tên Tệp / Đường Dẫn Hình Ảnh</label>
                            <input type="text" name="images" id="images" value="<c:out value='${category.images}'/>" class="form-control" />
                        </div>

                        <div class="mb-4">
                            <label for="status" class="form-label fw-semibold">Trạng Thái</label>
                            <select name="status" id="status" class="form-select">
                                <option value="1" ${category.status == 1 ? 'selected' : ''}>1 - Hoạt động (Active)</option>
                                <option value="0" ${category.status == 0 ? 'selected' : ''}>0 - Tạm khóa (Inactive)</option>
                            </select>
                        </div>

                        <div class="d-flex justify-content-between align-items-center pt-3 border-top">
                            <a href="<c:url value='/admin/categories/searchpaginated'/>" class="btn btn-outline-secondary">
                                <i class="bi bi-arrow-left me-1"></i> Hủy & Quay lại
                            </a>
                            <button type="submit" class="btn btn-primary px-4">
                                <i class="bi bi-check-lg me-1"></i> Cập Nhật
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
