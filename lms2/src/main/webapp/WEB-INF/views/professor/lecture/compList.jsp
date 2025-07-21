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
		<div class="body-container row justify-content-center bbs-header" style="margin: 100px;">
			<div style="font-size: 29px; text-align: center; margin-bottom: 30px;">
				<img src="${pageContext.request.contextPath}/dist/images/sangyong_logo2.png">
			</div>

			<div class="container mt-4">
				<h3 class="mb-4">강의 목록</h3>

				<c:choose>
					<c:when test="${not empty list}">
						<div class="row"> <!-- 추가됨: 카드 정렬 위한 row -->
							<c:forEach var="dto" items="${list}" varStatus="status">
								<div class="col-md-6 p-1">
									<div class="card h-100 shadow-sm">
										<div class="card-body">
											<div class="text-truncate px-2 subject-list">
												<h5>
													<a class="card-title"
														href="${pageContext.request.contextPath}/professor/lecture/main1?lecture_code=${dto.lecture_code} ">
														${dto.subject}
													</a>
												</h5>
												<p class="card-text mb-1">
													<span class="me-3">${dto.grade}학년 | ${dto.division}</span>
												</p>
												<p class="card-text">
													<span class="me-3">강의실 : ${dto.classroom}</span>
													<span class="me-3">수강인원 : ${dto.capacity}</span>
													<span class="me-3">학점 : <c:out value="${dto.credit}" default="정보없음"/></span>
												</p>
											</div>
										</div>
									</div>
								</div>
							</c:forEach>
						</div> <!-- row 닫기 -->
					</c:when>
					<c:otherwise>
						<div class="row">
							<div class="col-12">
								<div class="alert alert-warning text-center" role="alert">
									수강 중인 강의가 없습니다.
								</div>
							</div>
						</div>
					</c:otherwise>
				</c:choose>
			</div>

			<!-- 페이지 네비게이션 -->
			<div class="page-navigation">
				${dataCount == 0 ? "등록된 강의가 없습니다." : paging}
			</div>

			<!-- 하단 버튼 -->
			<div class="row board-list-footer">
				<div class="col">
					<button type="button" class="btn btn-light"
						onclick="location.href='${pageContext.request.contextPath}/professor/lecture/compList';">
						새로고침
					</button>
				</div>
				<div class="col text-end">
				</div>
			</div>
		</div>
	</div>
</main>
</body>
</html>