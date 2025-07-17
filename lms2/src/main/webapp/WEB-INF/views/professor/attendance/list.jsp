<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>출석관리</title>
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
					출석 관리
				</div>

				<!-- 주차 선택 폼 -->
				<form id="weekForm" method="get"
					action="${pageContext.request.contextPath}/professor/attendance/write">
					<select class="form-select" name="week" id="week-select">
						<c:forEach var="i" begin="1" end="15">
						    <option value="${i}" ${i == 1 ? "selected" : ""}>${i}주차</option>
						</c:forEach>
					</select>
					<div class="col text-end mt-2">
						<button type="button" class="btn btn-light"
							onclick="location.href='${pageContext.request.contextPath}/professor/attendance/list';">새로고침</button>
					</div>
					<div class="col text-end mt-2">
						<button type="submit" class="btn btn-light">출석관리</button>
					</div>
				</form>

				<table class="table table-hover board-list mt-4">
					<thead class="table-light">
						<tr>
							<th>학번</th>
							<th>이름</th>
							<th>출결상태</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="dto" items="${list}">
							<tr>
								<td>${dto.member_id}</td>
								<td>${dto.name}</td>
								<td><c:choose>
										<c:when test="${dto.status == 0}">
											<div
												style="width: 50px; height: 30px; background-color: red; border-radius: 3px;">결석</div>
										</c:when>
										<c:when test="${dto.status == 1}">
											<div
												style="width: 50px; height: 20px; background-color: green; border-radius: 3px;">출석</div>
										</c:when>
										<c:when test="${dto.status == 2}">
											<div
												style="width: 50px; height: 20px; background-color: yellow; border-radius: 3px;">지각</div>
										</c:when>
										<c:otherwise>
											<div
												style="width: 50px; height: 20px; background-color: black; border-radius: 3px;">미체크</div>
										</c:otherwise>
									</c:choose></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>



		</div>

	</main>
	<script
		src="${pageContext.request.contextPath}/dist/js/sidebar-toggle.js"></script>
	<script type="text/javascript">
		
	</script>
</body>
</html>