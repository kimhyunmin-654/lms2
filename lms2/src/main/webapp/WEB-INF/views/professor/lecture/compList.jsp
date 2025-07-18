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
				<div
					style="font-size: 29px; text-align: center; margin-bottom: 30px;">
					<img src="${pageContext.request.contextPath}/dist/images/sangyong_logo2.png">
				</div>

				<div class="container mt-4">
					<h3 class="mb-4">강의 목록</h3>
					<div class="col-md-6 p-1">
						<div>
							<div class="fw-semibold pt-2 pb-1">
								<i class="bi bi-book-half">강의목록</i>
							</div>
							<div class="border px-2">
								<c:forEach var="dto" items="${list}">
									<div class="text-truncate px-2 subject-list">
										<a href="${pageContext.request.contextPath}/professor/lecture/main1?lecture_code=${dto.lecture_code}">
											${dto.subject} </a> <span>${dto.grade}</span> <span>${dto.classroom}</span>
										<span>${dto.division}</span> <span>${dto.capacity}</span> <span>${dto.credit}</span>
									</div>
								</c:forEach>
								<c:forEach var="n" begin="${list.size() + 1}" end="5">
									<div class="text-truncate px-2 subject-list">&nbsp;</div>
								</c:forEach>
							</div>

						</div>
					</div>



					<div class="page-navigation">${dataCount == 0 ? "등록된 강의가 없습니다." : paging}
					</div>

					<div class="row board-list-footer">
						<div class="col">
							<button type="button" class="btn btn-light"
								onclick="location.href='${pageContext.request.contextPath}/professor/lecture/compList';">새로고침</button>
						</div>

						<div class="col text-end"></div>
					</div>
				</div>
			</div>
		</div>
	</main>
	<script
		src="${pageContext.request.contextPath}/dist/js/sidebar-toggle.js"></script>
	<script type="text/javascript">
		// 출석하기
		function okSend() {

			if (confirm('출석 처리 하시겠습니까?')) {
				return;
			}
			const f = document.submitForm;

			f.action = '${pageContext.request.contextPath}/professor/attendance/list?lecture_code=${dto.lecture_code}';
			f.submit();
		}
	</script>
</body>
</html>