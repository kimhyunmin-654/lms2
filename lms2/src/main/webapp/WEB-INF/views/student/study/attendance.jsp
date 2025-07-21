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
		<jsp:include page="/WEB-INF/views/layout/student_menusidebar.jsp" />

		<div class="container" style="margin-left: 220px; padding: 30px;">

			<div class="body-container row justify-content-center bbs-header"
				style="margin: 100px;">
				<div
					style="font-size: 29px; text-align: center; margin-bottom: 30px;">
					<img
						src="${pageContext.request.contextPath}/dist/images/sangyong_logo2.png">
					출석 관리
				</div>

				<div class="col-3 p-3 mb-2">
					<div class="text-bg-success p-3 w-10"></div>
					출석(${present})
					<div class="text-bg-warning p-3 w-10"></div>
					지각(${late})
					<div class="text-bg-danger p-3 w-10"></div>
					결석(${absent})
					<div class="text-bg-dark p-3 w-10"></div>
					미체크(<c:out value="${15 - present - late - absent}" />)
				</div>

				<div class="col-sm-9 p-3">
					<div class="container text-center">
						<c:forEach begin="1" end="15" var="i">
							<c:set var="found" value="false" />

							<c:forEach var="dto" items="${list}">
								<c:if test="${dto.week == i}">
									<c:set var="found" value="true" />

									<c:choose>
										<c:when test="${dto.status == 1}">
											<div class="text-bg-success p-3 w-10">${i}주차출석</div>
										</c:when>
										<c:when test="${dto.status == 2}">
											<div class="text-bg-warning p-3 w-10">${i}주차지각</div>
										</c:when>
										<c:when test="${dto.status == 0}">
											<div class="text-bg-danger p-3 w-10">${i}주차결석</div>
										</c:when>
										<c:otherwise>
											<div class="text-bg-dark p-3 w-10">${i}주차미체크</div>
										</c:otherwise>
									</c:choose>
								</c:if>
							</c:forEach>

							<!-- 해당 주차의 데이터가 없을 경우 -->
							<c:if test="${!found}">
								<div class="text-bg-dark p-3 w-10">${i}주차미체크</div>
							</c:if>
						</c:forEach>

					</div>

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