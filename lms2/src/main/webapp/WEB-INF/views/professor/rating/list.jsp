<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>성적관리</title>
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
						src="${pageContext.request.contextPath}/dist/images/sangyong_logo2.png">
				</div>

				<form id="weekForm" method="get" action="${pageContext.request.contextPath}/professor/rating/write">
					<input type="hidden" name="lecture_code" value="${lecture_code}" />
					
					<div class="col text-end mt-2">
						<button type="button" class="btn btn-light"
							onclick="location.href='${pageContext.request.contextPath}/professor/rating/list?lecture_code=${lecture_code}'">새로고침</button>
					</div>
					<div class="col text-end mt-2">
						<button type="button" class="btn btn-light"
    onclick="location.href='${pageContext.request.contextPath}/professor/rating/write?lecture_code=${lecture_code}'">성적관리</button>
					</div>
				</form>

				<table class="table table-hover board-list mt-4">
					<thead class="table-light">
						<tr>
							<th>학번</th>
							<th>이름</th>
							<th>중간</th>
							<th>기말</th>
							<th>출석</th>
							<th>과제</th>
							<th>토탈</th>
							<th>등급</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="dto" items="${list}">
							<tr>
								<td>${dto.member_id}</td>
								<td>${dto.name}</td>
								<td>${dto.middletest_rating}</td>
								<td>${dto.finaltest_rating}</td>
								<td>${dto.attendance_rating}</td>
								<td>${dto.homework_rating}</td>
								<td>${dto.total_rating}</td>								
								<td>${dto.rating}</td>								
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>



		</div>

	</main>
</body>
</html>