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
<style type="text/css">
.bbs-header img {
	height: 50px;
	margin-bottom: 10px;
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
					<h3 class="mb-4">${dto.subject}</h3>
                </div>
				<hr>
				
				<!-- 공지사항 -->
				<div class="col-md-12 p-1">
					<div>
						<div class="fw-semibold pt-2 pb-1">
							<i class="bi bi-book-half">공지사항</i>
						</div>
						<div class="border px-2">
							<c:forEach var="dto" items="${listNotice}" varStatus="status">
								<div class="text-truncate px-2 subject-list">
									<a href="${pageContext.request.contextPath}/admin/notice/article?notice_id=${dto.notice_id}">
										${dto.subject}
									</a>
								</div>
							</c:forEach>
							
							<c:forEach var="n" begin="${listNotice.size() + 1}" end="5">
								<div class="text-truncate px-2 subject-list">&nbsp;</div>
							</c:forEach>
						</div>
						<div class="pt-2 text-end">
							<a href="${pageContext.request.contextPath}/admin/notice/list"
								class="text-reset">더보기</a>
						</div>
					</div>
				</div>
				
				<!-- 자료실 -->
				<div class="col-md-12 p-1">
				    <div>
				        <div class="fw-semibold pt-2 pb-1">
				            <i class="bi bi-book-half">자료실</i>
				        </div>
				        <div class="border px-2">
				            <c:forEach var="dto" items="${listData}">
				                <div class="text-truncate px-2 subject-list">
				                    <a href="${pageContext.request.contextPath}/professor/lecture/article?data_id=${dto.data_id}">
				                        ${dto.subject}
				                    </a>
				                </div>
				            </c:forEach>
				
				           <c:forEach var="n" begin="${listData.size() + 1}" end="5">
								<div class="text-truncate px-2 subject-list">&nbsp;</div>
							</c:forEach>
				        </div>
				        <div class="pt-2 text-end">
				            <div class="accordion-body custom-submenu">
				                <a href="${pageContext.request.contextPath}/professor/bbs/list?lecture_code=${dto.lecture_code}">더보기</a>
				            </div>
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