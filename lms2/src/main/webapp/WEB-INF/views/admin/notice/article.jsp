<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>공지사항 상세보기</title>
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
            <div class="col-md-10 offset-md-1">
                <h3 class="mb-4">공지사항 상세보기</h3>

                <!-- 게시글 본문 -->
                <table class="table table-bordered">
                    <tr>
                        <th class="bg-light" width="15%">제목</th>
                        <td>${dto.subject}</td>
                    </tr>
                    <tr>
                        <th class="bg-light">작성자</th>
                        <td>${dto.name}</td>
                    </tr>
                    <tr>
                        <th class="bg-light">등록일</th>
                        <td>${dto.reg_date}</td>
                    </tr>
                    <tr>
                        <th class="bg-light">조회수</th>
                        <td>${dto.hit_count}</td>
                    </tr>
                    <tr>
                        <th class="bg-light">내용</th>
                        <td style="min-height: 200px;">${dto.content}</td>
                    </tr>

                    <!-- 첨부파일 -->
                    <c:if test="${not empty listFile}">
                        <tr>
                            <th class="bg-light">첨부파일</th>
                            <td>
                                <c:forEach var="vo" items="${listFile}" varStatus="status">
                                    <a href="${pageContext.request.contextPath}/admin/notice/download?fileNum=${vo.file_id}">
                                        ${vo.original_filename}
                                    </a>
                                    <c:if test="${!status.last}"> | </c:if>
                                </c:forEach>
                            </td>
                        </tr>
                    </c:if>
                </table>

                <!-- 이전글 / 다음글 -->
                <div class="mt-3">
                    <p>
                        <strong>이전글 :</strong>
                        <c:choose>
                            <c:when test="${not empty prevDto}">
                                <a href="${pageContext.request.contextPath}/admin/notice/article?notice_id=${prevDto.notice_id}&${query}">
                                    ${prevDto.subject}
                                </a>
                            </c:when>
                            <c:otherwise>없음</c:otherwise>
                        </c:choose>
                    </p>
                    <p>
                        <strong>다음글 :</strong>
                        <c:choose>
                            <c:when test="${not empty nextDto}">
                                <a href="${pageContext.request.contextPath}/admin/notice/article?notice_id=${nextDto.notice_id}&${query}">
                                    ${nextDto.subject}
                                </a>
                            </c:when>
                            <c:otherwise>없음</c:otherwise>
                        </c:choose>
                    </p>
                </div>

                <!-- 버튼 영역 -->
                <div class="text-end mt-4">
                    <a href="${pageContext.request.contextPath}/admin/notice/update?num=${dto.notice_id}&${query}" class="btn btn-outline-primary">수정</a>
                    <button type="button" class="btn btn-outline-danger" onclick="deleteOk();">삭제</button>
                    <a href="${pageContext.request.contextPath}/admin/notice/list?${query}" class="btn btn-secondary">목록</a>
                </div>
            </div>
        </div>
    </div>
</main>

<script>
function deleteOk() {
    if (confirm('공지사항을 삭제하시겠습니까?')) {
        location.href = '${pageContext.request.contextPath}/admin/notice/delete?num=${dto.notice_id}&' + '${query}';
    }
}
</script>

</body>
</html>
