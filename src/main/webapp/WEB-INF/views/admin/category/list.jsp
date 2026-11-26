<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
    <title>Quản lý Category - Tìm kiếm và Phân trang</title>
</head>
<body>
    <div class="d-flex justify-content-between align-items-center mb-4">
        <div>
            <h3 class="fw-bold text-primary mb-1">
                <i class="bi bi-tags-fill me-2"></i> Danh Sách Category
            </h3>
            <p class="text-muted small mb-0">Quản lý danh mục sản phẩm, tìm kiếm và phân trang dữ liệu</p>
        </div>
        <a href="<c:url value='/admin/categories/add'/>" class="btn btn-primary d-inline-flex align-items-center gap-1 shadow-sm">
            <i class="bi bi-plus-circle"></i> Add category
        </a>
    </div>

    <!-- Hiển thị thông báo -->
    <c:if test="${not empty message}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <i class="bi bi-check-circle-fill me-2"></i> <i><c:out value="${message}"/></i>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>
    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <i class="bi bi-exclamation-triangle-fill me-2"></i> <i><c:out value="${errorMessage}"/></i>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>
    <!-- Hết thông báo -->

    <div class="card border-0 shadow-sm rounded-3 mb-4">
        <div class="card-body p-3">
            <div class="row g-3 align-items-center justify-content-between">
                <!-- Form Tìm kiếm -->
                <div class="col-md-6">
                    <form action="<c:url value='/admin/categories/searchpaginated'/>" method="get" class="d-flex gap-2">
                        <input type="hidden" name="size" value="${categoryPage.size}" />
                        <div class="input-group">
                            <span class="input-group-text bg-white"><i class="bi bi-search text-muted"></i></span>
                            <input type="text" name="name" id="name" value="<c:out value='${name}'/>" class="form-control" placeholder="Nhập từ khóa để tìm" />
                        </div>
                        <button type="submit" class="btn btn-primary px-3">Search</button>
                        <c:if test="${not empty name}">
                            <a href="<c:url value='/admin/categories/searchpaginated?size=${categoryPage.size}'/>" class="btn btn-outline-secondary">Xóa lọc</a>
                        </c:if>
                    </form>
                </div>

                <!-- Chọn Page Size -->
                <div class="col-md-3 text-md-end">
                    <form action="<c:url value='/admin/categories/searchpaginated'/>" method="get" class="d-inline-flex align-items-center gap-2">
                        <c:if test="${not empty name}">
                            <input type="hidden" name="name" value="<c:out value='${name}'/>" />
                        </c:if>
                        <label for="size" class="text-nowrap small text-muted fw-semibold">Page size:</label>
                        <select name="size" id="size" class="form-select form-select-sm w-auto" onchange="this.form.submit()">
                            <option ${categoryPage.size == 3 ? 'selected':''} value="3">3</option>
                            <option ${categoryPage.size == 5 ? 'selected':''} value="5">5</option>
                            <option ${categoryPage.size == 10 ? 'selected':''} value="10">10</option>
                            <option ${categoryPage.size == 15 ? 'selected':''} value="15">15</option>
                            <option ${categoryPage.size == 20 ? 'selected':''} value="20">20</option>
                        </select>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <!-- Bảng dữ liệu -->
    <div class="card border-0 shadow-sm rounded-3 overflow-hidden mb-4">
        <div class="table-responsive">
            <c:if test="${!categoryPage.hasContent()}">
                <div class="text-center py-5">
                    <i class="bi bi-inbox fs-1 text-muted d-block mb-2"></i>
                    <h5 class="text-muted">No Category</h5>
                    <p class="small text-muted">Không tìm thấy danh mục nào phù hợp với từ khóa.</p>
                </div>
            </c:if>

            <c:if test="${categoryPage.hasContent()}">
                <table class="table table-hover align-middle mb-0">
                    <thead class="table-light">
                        <tr>
                            <th class="ps-3" style="width: 70px;">STT</th>
                            <th>Images</th>
                            <th>Category name</th>
                            <th>Status</th>
                            <th class="text-end pe-3" style="width: 160px;">Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${categoryPage.content}" var="cate" varStatus="STT">
                            <tr>
                                <td class="ps-3 fw-semibold text-muted">${STT.index + 1 + (categoryPage.number * categoryPage.size)}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${not empty cate.images}">
                                            <span class="badge bg-light text-dark border"><i class="bi bi-image me-1"></i><c:out value="${cate.images}"/></span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="text-muted small">Chưa có ảnh</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <span class="fw-bold text-dark"><c:out value="${cate.name}"/></span>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${cate.status == 1}">
                                            <span class="badge bg-success-subtle text-success px-2 py-1"><i class="bi bi-check-circle me-1"></i>Hoạt động</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-secondary-subtle text-secondary px-2 py-1"><i class="bi bi-dash-circle me-1"></i>Tạm khóa</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="text-end pe-3">
                                    <a href="<c:url value='/admin/categories/edit/${cate.id}'/>" class="btn btn-sm btn-outline-primary me-1">
                                        <i class="bi bi-pencil-square"></i> Sửa
                                    </a>
                                    <a href="<c:url value='/admin/categories/delete/${cate.id}'/>" class="btn btn-sm btn-outline-danger" onclick="return confirm('Bạn có chắc muốn xóa danh mục này?');">
                                        <i class="bi bi-trash"></i> Xóa
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:if>
        </div>

        <!-- Thanh phân trang -->
        <c:if test="${categoryPage.totalPages > 0}">
            <div class="card-footer bg-white border-0 py-3 d-flex justify-content-between align-items-center">
                <span class="small text-muted">
                    Hiển thị <strong>${categoryPage.numberOfElements}</strong> / <strong>${categoryPage.totalElements}</strong> danh mục (Trang ${categoryPage.number + 1} / ${categoryPage.totalPages})
                </span>

                <c:if test="${categoryPage.totalPages > 1}">
                    <ul class="pagination pagination-sm mb-0">
                        <!-- Trang trước -->
                        <c:choose>
                            <c:when test="${categoryPage.first}">
                                <li class="page-item disabled">
                                    <span class="page-link" aria-label="Trang trước">&laquo;</span>
                                </li>
                            </c:when>
                            <c:otherwise>
                                <c:url var="prevUrl" value="/admin/categories/searchpaginated">
                                    <c:if test="${not empty name}">
                                        <c:param name="name" value="${name}" />
                                    </c:if>
                                    <c:param name="size" value="${categoryPage.size}" />
                                    <c:param name="page" value="${categoryPage.number}" />
                                </c:url>
                                <li class="page-item">
                                    <a class="page-link" href="${prevUrl}" aria-label="Trang trước">&laquo;</a>
                                </li>
                            </c:otherwise>
                        </c:choose>

                        <!-- Danh sách số trang -->
                        <c:forEach items="${pageNumbers}" var="pageNumber">
                            <c:url var="pageUrl" value="/admin/categories/searchpaginated">
                                <c:if test="${not empty name}">
                                    <c:param name="name" value="${name}" />
                                </c:if>
                                <c:param name="size" value="${categoryPage.size}" />
                                <c:param name="page" value="${pageNumber}" />
                            </c:url>
                            <li class="${pageNumber == categoryPage.number + 1 ? 'page-item active' : 'page-item'}">
                                <a class="page-link" href="${pageUrl}">${pageNumber}</a>
                            </li>
                        </c:forEach>

                        <!-- Trang sau -->
                        <c:choose>
                            <c:when test="${categoryPage.last}">
                                <li class="page-item disabled">
                                    <span class="page-link" aria-label="Trang sau">&raquo;</span>
                                </li>
                            </c:when>
                            <c:otherwise>
                                <c:url var="nextUrl" value="/admin/categories/searchpaginated">
                                    <c:if test="${not empty name}">
                                        <c:param name="name" value="${name}" />
                                    </c:if>
                                    <c:param name="size" value="${categoryPage.size}" />
                                    <c:param name="page" value="${categoryPage.number + 2}" />
                                </c:url>
                                <li class="page-item">
                                    <a class="page-link" href="${nextUrl}" aria-label="Trang sau">&raquo;</a>
                                </li>
                            </c:otherwise>
                        </c:choose>
                    </ul>
                </c:if>
            </div>
        </c:if>
    </div>
</body>
</html>
