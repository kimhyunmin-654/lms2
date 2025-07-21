<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>수강신청</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/main2.css">
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
		<jsp:include page="/WEB-INF/views/layout/student_menusidebar.jsp" />
		<c:if test="${not empty sessionScope.error}">
		    <script>
		        alert('${sessionScope.error}');
		    </script>
		    <c:remove var="error" scope="session"/>
		</c:if>
				

		<div class="container mt-5 ">
			<div style="margin-top: 100px;">
			<div class="main-wrapper bbs-header">
				<div style="font-size: 29px; text-align: center; margin-bottom: 30px;">
					<img src="${pageContext.request.contextPath}/dist/images/sugang.png">
				</div>
				
				<form method="post" name="insertForm">
					<input type="hidden" name="lecture_code" id="lecture_code" />
				</form>
				
					<table class="table table-hover board-list">
						<thead class="table-light">
							<tr>
								<th width="">번호</th>
								<th>강의명</th>
								<th width="">교수명</th>
								<th width="">학년</th>
								<th width="">강의실</th>
								<th width="">학점</th>
								<th width=""></th>
							</tr>
						</thead>

						<tbody>
							<c:forEach var="dto" items="${list}" varStatus="status">
								<tr>
									<td>${status.index + 1}</td>
									<td>${dto.subject}</td>
									<td>${dto.name}</td>
									<td>${dto.grade}</td>
									<td>${dto.classroom}</td>
									<td>${dto.credit}</td>
									<td>
										<button type="button" onclick="okSend('${dto.lecture_code}')">신청</button>
									</td>
								</tr>
							</c:forEach>
						</tbody>

					</table>
				

				<div class="page-navigation">${dataCount == 0 ? "등록된 강의가 없습니다." : ''}
				</div>

				<div class="row board-list-footer">
					<div class="col">
						<button type="button" class="btn btn-light"
							onclick="location.href='${pageContext.request.contextPath}/student/lecture/list';">새로고침</button>
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
		// 수강신청하기
		function okSend(lecture_code) {
		    if (confirm('수강 신청하시겠습니까?')) {
		        document.getElementById('lecture_code').value = lecture_code;
		        const form = document.insertForm;
		        form.action = '${pageContext.request.contextPath}/student/lecture/account';
		        form.submit();
		    }
		}


	</script>
</body>
</html>