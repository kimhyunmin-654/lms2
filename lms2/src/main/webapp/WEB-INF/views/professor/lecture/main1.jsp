<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>강의목록</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/dist/css/main2.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/dist/css/board.css">
	
<link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/layout.css">
	
<style type="text/css">
.bbs-header img {
	height: 50px;
	margin-bottom: 10px;
}
 .subject-title {
            font-size: 1.75rem;
            font-weight: bold;
            text-align: center;
 }
</style>
</head>
<body>
	<header>
		<jsp:include page="/WEB-INF/views/layout/menuheader.jsp" />
	</header>
	<main>
		<jsp:include page="/WEB-INF/views/layout/prof_menusidebar.jsp" />

		<div class="container" style="margin-left: 220px; padding: 30px;">

			<div class="body-container row justify-content-center bbs-header"
				style="margin: 100px;">
				<div
					style="font-size: 29px; text-align: center; margin-bottom: 30px;">
					<img
						src="${pageContext.request.contextPath}/dist/images/sangyong_logo_bbs.png">
				</div>

					<div class="container mt-4">
						<div class="subject-title mt-3">| ${dto.subject} |</div>
	                </div>
				
				
				<!-- 공지사항 -->
				<div class="row my-3">
				    <div class="col-md-12">
				        <div class="card shadow-sm">
				            <div class="card-header fw-bold bg-light">
				                <i class="bi bi-book-half me-2"></i> 공지사항
				            </div>
				            <div class="card-body d-flex flex-column gap-3 px-2">
				                <c:if test="${not empty listNotice}">
				                    <c:set var="dto" value="${listNotice[0]}" />
				                    <div class="border rounded p-2 bg-white d-flex align-items-center">
				                        <div class="text-truncate">
				                            <c:if test="${dto.is_notice == 1}">
				                                <span class="badge bg-warning text-dark me-2">공지</span>
				                            </c:if>
				                            <a href="${pageContext.request.contextPath}/professor/notice/article?notice_id=${dto.notice_id}"
				                               class="text-decoration-none text-dark">
				                                ${dto.subject}
				                            </a>
				                        </div>
				                    </div>
				                </c:if>
				            </div>
				        </div>
				        <div class="text-end pt-2">
				            <a href="${pageContext.request.contextPath}/professor/notice/list"
				               class="text-decoration-none">더보기</a>
				        </div>
				    </div>
				</div>
				
				<!-- 자료실 -->
				<div class="row my-3">
				    <div class="col-md-12">
						<div class="card shadow-sm">
							<div class="card-header fw-bold bg-light">
								<i class="bi bi-megaphone text-danger me-2"></i> 자료실
							</div>
				        <div class="card-body d-flex flex-column gap-3">
				            <c:forEach var="dto" items="${listData}">
				                <div class="border rounded p-2 bg-white">
				                    <a href="${pageContext.request.contextPath}/professor/bbs/article?page=${page}&lecture_code=${dto.lecture_code}&num=${dto.data_id}"
									   class="text-decoration-none text-dark text-truncate d-block">
									    ${dto.subject}
									</a>
				                </div>
				            </c:forEach>
				           <c:forEach var="n" begin="${listData.size() + 4}" end="5">
								<div class="text-truncate px-2 subject-list">&nbsp;</div>
							</c:forEach>
				        </div>
				      </div>  
				       <div class="text-end pt-2">
							<a href="${pageContext.request.contextPath}/professor/bbs/list?lecture_code=${dto.lecture_code}"
								class="text-decoration-none">더보기</a>
						</div>
					</div>
				</div>
				
				<div class="row board-list-footer">
					<div class="col">
						<button type="button" class="btn btn-light"
							onclick="location.href='${pageContext.request.contextPath}/professor/lecture/compList';">강의목록</button>
					</div>

					<div class="col text-end"></div>
				</div>
			</div>
		</div>

	</main>
	<script
		src="${pageContext.request.contextPath}/dist/js/sidebar-toggle.js"></script>
	<script type="text/javascript">
		
	</script>
</body>
</html>