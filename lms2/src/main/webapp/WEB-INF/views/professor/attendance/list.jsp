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
				<!--  
				<form method="get" action="${pageContext.request.contextPath}/professor/attendance/list">
				    <label>강의 선택:</label>
				    <select name="lectureCode" onchange="this.form.submit()">
				        <option value="">-- 강의 선택 --</option>
				        <c:forEach var="lec" items="${lectureList}">
				            <option value="${lec.lecture_code}" ${lec.lecture_code == selectedLecture ? "selected" : ""}>
				                ${lec.subject}
				            </option>
				        </c:forEach>
				    </select>
				</form>
-->

				<form id="weekForm" method="get"
					action="${pageContext.request.contextPath}/professor/attendance/write">
					<select class="form-select" name="week" id="week-select">
						<option value="">-- 주차 선택 --</option>
						<c:forEach var="i" begin="1" end="15">
							<option value="${i}">${i}주차</option>
						</c:forEach>
					</select>
					<div class="col text-end mt-2">
						<button type="button" class="btn btn-light" onclick="location.href='${pageContext.request.contextPath}/professor/attendance/list';">새로고침</button>
					</div>
					<div class="col text-end mt-2">
						<button type="submit" class="btn btn-light">출석관리</button>
					</div>
				</form>


				<table class="table table-hover board-list">
					<thead class="table-light">
						<tr>
							<th>학번</th>
							<th>이름</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="dto" items="${list}">
							<tr>
								<td>${dto.member_id}</td>
								<td>${dto.name}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<!-- <div class="page-navigation">${dataCount == 0 ? "등록된 학생이 없습니다." : paging} -->
			</div>

			

		</div>

	</main>
	<script
		src="${pageContext.request.contextPath}/dist/js/sidebar-toggle.js"></script>
	<script type="text/javascript">
		
	</script>
</body>
</html>