<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>학생 페이지</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/dist/css/main2.css">
</head>
<header>
	<jsp:include page="/WEB-INF/views/layout/mainheader.jsp" />
</header>
<body>
	<main>
		<jsp:include page="/WEB-INF/views/layout/admin_mainsidebar.jsp" />

		<div class="container" style="margin-left: 220px; padding: 30px;">

			<div class="body-container row justify-content-center"
				style="margin: 100px;">
				<h3 style="font-size: 29px;">관리자 메인페이지</h3>
				
				<!-- 수강과목 -->
				<div class="col-md-6 p-1">
					<div>
						<div class="fw-semibold pt-2 pb-1">
							<i class="bi bi-book-half">수강과목</i>
						</div>
						<div class="border px-2">
						    <c:forEach var="dto" items="${list}">
						        <div class="text-truncate px-2 subject-list">
						            <a href="${pageContext.request.contextPath}/student/lecture/compList?lecture_code=${dto.lecture.lecture_code}">
						                ${dto.lecture.subject} (${dto.lecture.name})
						            </a>
						        </div>
						    </c:forEach>
						    <c:forEach var="n" begin="${list.size() + 1}" end="5">
						        <div class="text-truncate px-2 subject-list">&nbsp;</div>
						    </c:forEach>
						</div>
					</div>
				</div>

				<!-- 학기일정 -->
				<div class="col-md-6 p-1">
					<div>
						<div class="fw-semibold pt-2 pb-1">
							<i class="bi bi-book-half">학기일정</i>

						</div>
						<div class="border px-2">
							
						</div>
					</div>
				</div>
				
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


			</div>
		</div>
	</main>
	<script
		src="${pageContext.request.contextPath}/dist/js/sidebar-toggle.js"></script>
</body>
</html>