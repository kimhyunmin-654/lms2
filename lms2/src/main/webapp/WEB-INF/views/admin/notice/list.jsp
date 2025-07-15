<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>공지사항</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/main2.css">
</head>
<body>

<header>
    <jsp:include page="/WEB-INF/views/layout/mainheader.jsp" />
</header>

<main>
    <jsp:include page="/WEB-INF/views/layout/admin_mainsidebar.jsp" />

    <div class="container" style="margin-left: 220px; padding: 30px;">
        <div class="row">
            <div class="col">
                <h3 class="mb-4" style="margin-top: 50px;">공지사항</h3>

                <table class="table table-bordered table-hover text-center align-middle">
                    <thead class="table-light">
                        <tr>
                            <th width="10%">번호</th>
                            <th>제목</th>
                            <th width="15%">작성자</th>
                            <th width="15%">등록일</th>
                            <th width="10%">조회수</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="dto" items="${list}">
                            <tr>
                                <td>
                                    <c:if test="${dto.notice == 1}">
                                        <span class="badge bg-warning text-dark">공지</span>
                                    </c:if>
                                    <c:if test="${dto.notice != 1}">
                                        ${dto.num}
                                    </c:if>
                                </td>
                                <td class="text-start">
                                    <a href="${pageContext.request.contextPath}/admin/notice/article?notice_id=${dto.num}">
                                        ${dto.subject}
                                    </a>
                                </td>
                                <td>${dto.userName}</td>
                                <td>${dto.reg_date}</td>
                                <td>${dto.hitCount}</td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty list}">
                            <tr>
                                <td colspan="5">등록된 공지사항이 없습니다.</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>

                <!-- 페이징 처리 -->
                <div class="d-flex justify-content-center my-4">
                    ${page} <!-- 컨트롤러에서 생성된 페이징 HTML -->
                </div>

                <!-- 관리자 글쓰기 버튼 -->
                <div class="d-flex justify-content-end">
                    <a href="${pageContext.request.contextPath}/admin/notice/account" class="btn btn-primary">
                        <i class="bi bi-pencil-square"></i> 글쓰기
                    </a>
                </div>

            </div>
        </div>
    </div>
</main>

<script src="${pageContext.request.contextPath}/dist/js/sidebar-toggle.js"></script>
</body>
</html>
