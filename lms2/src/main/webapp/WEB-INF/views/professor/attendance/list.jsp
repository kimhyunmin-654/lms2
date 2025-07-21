<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>출석관리</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/main2.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/board.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/paginate.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" integrity="sha512-X0sP..." crossorigin="anonymous" referrerpolicy="no-referrer" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/mainPage.css">
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
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

		<div class="container mt-5 ">
		<div style="margin-top: 100px;">
		<div class="main-wrapper bbs-header">

				<div style="font-size: 29px; text-align: center; margin-bottom: 30px;">
					<img src="${pageContext.request.contextPath}/dist/images/chulsuk.png">
				</div>

				<form id="weekForm" method="get" action="${pageContext.request.contextPath}/professor/attendance/write">
					<input type="hidden" name="lecture_code" value="${lecture_code}" />
					
					<div class="col text-end mt-2">
						<button type="button" class="btn btn-light"
							onclick="location.href='${pageContext.request.contextPath}/professor/attendance/list?lecture_code=${lecture_code}'">새로고침</button>
					</div>
					<div class="col text-end mt-2">
						<button type="submit" class="btn btn-light" onclick="goToWrite()">출석관리</button>
					</div>

					<select class="form-select" name="week" id="week-select" onchange="changeWeek()">
						<c:forEach var="i" begin="1" end="15">
							<option value="${i}" ${i == selectedWeek ? "selected" : ""}>${i}주차</option>
						</c:forEach>
					</select>

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
												style="width: 50px; height: 20px; background-color: pink; border-radius: 3px;">미체크</div>
										</c:otherwise>
									</c:choose></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	</main>
	<script
		src="${pageContext.request.contextPath}/dist/js/sidebar-toggle.js"></script>
	<script type="text/javascript">
	function changeWeek() {
	    const selectedWeek = document.getElementById('week-select').value;
	    location.href = '${pageContext.request.contextPath}/professor/attendance/list?lecture_code=${lecture_code}&week=' + selectedWeek;
	}
	
	function goToWrite() {
	    const selectedWeek = document.getElementById('week-select').value;
	    location.href = '${pageContext.request.contextPath}/professor/attendance/write?lecture_code=${lecture_code}&week=' + selectedWeek;
	}
	</script>
</body>
</html>