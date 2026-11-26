<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
    <title>Thêm mới Category - ACCOUNT HUB</title>
</head>
<body>
    <div class="row justify-content-center py-4">
        <div class="col-md-7">
            <div class="card border-0 shadow-sm rounded-4">
                <div class="card-header bg-white border-0 pt-4 px-4 pb-0">
                    <h4 class="fw-bold text-primary mb-1">
                        <i class="bi bi-plus-circle-fill me-2"></i> Thêm Mới Category
                    </h4>
                    <p class="text-muted small">Điền các thông tin dưới đây để tạo danh mục sản phẩm mới</p>
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

                        <div class="mb-3">
                            <label for="name" class="form-label fw-semibold">Tên Danh Mục <span class="text-danger">*</span></label>
                            <input type="text" name="name" id="name" class="form-control" placeholder="Ví dụ: Điện thoại thông minh" required autofocus />
                        </div>

                        <div class="mb-3">
                            <label for="images" class="form-label fw-semibold">Tên Tệp / Đường Dẫn Hình Ảnh</label>
                            <input type="text" name="images" id="images" class="form-control" placeholder="Ví dụ: phone.png" />
                        </div>

                        <div class="mb-4">
                            <label for="status" class="form-label fw-semibold">Trạng Thái</label>
                            <select name="status" id="status" class="form-select">
                                <option value="1" selected>1 - Hoạt động (Active)</option>
                                <option value="0">0 - Tạm khóa (Inactive)</option>
                            </select>
                        </div>

                        <div class="d-flex justify-content-between align-items-center pt-3 border-top">
                            <a href="<c:url value='/admin/categories/searchpaginated'/>" class="btn btn-outline-secondary">
                                <i class="bi bi-arrow-left me-1"></i> Quay lại Danh sách
                            </a>
                            <button type="submit" class="btn btn-primary px-4">
                                <i class="bi bi-check-lg me-1"></i> Lưu Danh Mục
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
