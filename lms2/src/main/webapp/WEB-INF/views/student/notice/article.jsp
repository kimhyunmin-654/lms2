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
    <jsp:include page="/WEB-INF/views/layout/student_menusidebar.jsp" />

    <div class="container" style="margin-left: 220px; padding: 30px;">
        <div class="row justify-content-center">
            <div class="col-md-10">

                <h3 class="mb-4 mt-5">공지사항</h3>

                <div class="row border-bottom">
                    <div class="col-sm-2 bg-light d-flex align-items-center fw-bold py-3">제목</div>
                    <div class="col-sm-10 bg-white py-3">${dto.subject}</div>
                </div>

                <div class="row border-bottom">
                    <div class="col-sm-2 bg-light d-flex align-items-center fw-bold py-3">작성자</div>
                    <div class="col-sm-10 bg-white py-3">${dto.name}</div>
                </div>

                <div class="row border-bottom">
                    <div class="col-sm-2 bg-light d-flex align-items-center fw-bold py-3">등록일</div>
                    <div class="col-sm-10 bg-white py-3">${dto.reg_date}</div>
                </div>

                <div class="row border-bottom">
                    <div class="col-sm-2 bg-light d-flex align-items-center fw-bold py-3">조회수</div>
                    <div class="col-sm-10 bg-white py-3">${dto.hit_count}</div>
                </div>

                <div class="row border-bottom">
                    <div class="col-sm-2 bg-light d-flex align-items-center fw-bold py-3">내용</div>
                    <div class="col-sm-10 bg-white py-3" style="min-height:200px;">${dto.content}</div>
                </div>

                <!-- 첨부파일 -->
                <c:if test="${not empty listFile}">
                    <div class="row border-bottom">
                        <div class="col-sm-2 bg-light d-flex align-items-center fw-bold py-3">첨부파일</div>
                        <div class="col-sm-10 bg-white py-3">
                            <c:forEach var="vo" items="${listFile}" varStatus="status">
                                <a href="${pageContext.request.contextPath}/student/notice/download?fileNum=${vo.file_id}">
                                    ${vo.original_filename}
                                </a>
                                <c:if test="${!status.last}"> | </c:if>
                            </c:forEach>
                        </div>
                    </div>
                </c:if>

				<div class="mt-4 border-top pt-3">
				    <div class="d-flex justify-content-between">
				        <div>
				            <strong>이전글:</strong>
				            <c:choose>
				                <c:when test="${not empty prevDto}">
				                    <a href="${pageContext.request.contextPath}/student/notice/article?notice_id=${prevDto.notice_id}&${query}" class="text-decoration-none">
				                        ${prevDto.subject}
				                    </a>
				                </c:when>
				                <c:otherwise>이전글이 없습니다.</c:otherwise>
				            </c:choose>
				        </div>
				
				        <div>
				            <strong>다음글:</strong>
				            <c:choose>
				                <c:when test="${not empty nextDto}">
				                    <a href="${pageContext.request.contextPath}/student/notice/article?notice_id=${nextDto.notice_id}&${query}" class="text-decoration-none">
				                        ${nextDto.subject}
				                    </a>
				                </c:when>
				                <c:otherwise>다음글이 없습니다.</c:otherwise>
				            </c:choose>
				        </div>
				    </div>
				</div>

    
                    <a href="${pageContext.request.contextPath}/student/notice/list?${query}" class="btn btn-secondary">목록</a>
                </div>

            </div>
        </div>
</main>


</body>
</html>
