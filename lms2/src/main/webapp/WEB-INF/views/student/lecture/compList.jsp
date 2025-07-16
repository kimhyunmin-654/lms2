<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>수강신청</title>
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
					<h3 class="mb-4">수강 중인 강의</h3>

					<c:choose>
						<c:when test="${not empty list}">
							<div class="row">
								<c:forEach var="dto" items="${list}" varStatus="status">
									<div class="col-md-6">
										<div class="course-box">
											<div class="course-title">${dto.lecture.subject}</div>
											<div class="course-info">
												<span>${dto.lecture.grade}학년</span> <span>${dto.lecture.classroom}</span>
												<span>${dto.lecture.credit}학점</span>
											</div>
											<div class="course-info">
												<span>${dto.lecture.lecture_year}년
													${dto.lecture.semester}</span> <span>분반:
													${dto.lecture.division}</span>
											</div>
										</div>
									</div>
								</c:forEach>
							</div>
						</c:when>
						<c:otherwise>
							<div class="row">
								<div class="col-12">
									<div class="no-courses">수강 중인 강의가 없습니다.</div>
								</div>
							</div>
						</c:otherwise>
					</c:choose>
				</div>



				<div class="page-navigation">${dataCount == 0 ? "등록된 강의가 없습니다." : paging}
				</div>

				<div class="row board-list-footer">
					<div class="col">
						<button type="button" class="btn btn-light"
							onclick="location.href='${pageContext.request.contextPath}/student/lecture/compList';">새로고침</button>
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