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
		<jsp:include page="/WEB-INF/views/layout/student_mainsidebar.jsp" />

		<div class="container" style="margin-left: 220px; padding: 30px;">

			<div class="body-container row justify-content-center" style="margin: 100px;">
								
				<h3 style="font-size: 29px;">학생 메인페이지</h3>
		
				<!-- 수강과목 -->
				<div class="row g-3">
					<div class="col-md-12">
						<div class="card shadow-sm">
							<div class="card-header fw-bold bg-light">
								<i class="bi bi-book-half me-2"></i> 수강과목 목록
							</div>
							<div class="card-body d-flex flex-column gap-3 px-2">
								<c:forEach var="dto" items="${list}">
									<div
										class="border rounded p-2 bg-white text-truncate subject-list">
										<a href="${pageContext.request.contextPath}/student/study/list?lecture_code=${dto.lecture.lecture_code}"
											class="text-decoration-none text-dark d-block">
											${dto.lecture.subject} (${dto.lecture.name}) </a>
									</div>
								</c:forEach>
								<c:forEach var="n" begin="${list.size() + 1}" end="5">
									<div class="border rounded p-2 bg-white">&nbsp;</div>
								</c:forEach>
							</div>
						</div>
						<div class="text-end pt-2 pe-2">
								<a
									href="${pageContext.request.contextPath}/student/lecture/compList"
									class="text-decoration-none"> 더보기 </a>
							</div>
					</div>
				</div>

				<!-- 최근 공지사항 -->
				<div class="row my-3">
					<div class="col-md-12">
						<div class="card shadow-sm">
							<div class="card-header fw-bold bg-light">
								<i class="bi bi-megaphone text-danger me-2"></i> 최근 공지사항 목록
							</div>
							<div class="card-body d-flex flex-column gap-3">
								<c:forEach var="dto" items="${listNotice}">
									<div class="border rounded p-2 bg-white">
										<a href="${pageContext.request.contextPath}/student/notice/article?notice_id=${dto.notice_id}"
											class="text-decoration-none text-dark text-truncate d-block">
											<i class="bi bi-pin-angle-fill me-2 text-secondary"></i>
											${dto.subject}
										</a>
									</div>
								</c:forEach>
								<c:forEach var="n" begin="${listNotice.size() + 1}" end="5">
									<div class="border rounded p-2 bg-white">&nbsp;</div>
								</c:forEach>
							</div>
						</div>
						<div class="text-end pt-2">
							<a href="${pageContext.request.contextPath}/student/notice/list"
								class="text-decoration-none">더보기</a>
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