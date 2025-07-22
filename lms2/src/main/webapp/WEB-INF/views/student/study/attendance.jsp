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

			<div class="container mt-5 ">
			<div style="margin-top: 100px;">
			<div class="main-wrapper bbs-header">
				<div style="font-size: 29px; text-align: center; margin-bottom: 30px;">
					<img src="${pageContext.request.contextPath}/dist/images/chulsuk.png">
				</div>

				<div class="row text-center mb-3">
					<div class="col-auto">
						<span class="badge bg-success px-3 py-2">출석 (${present})</span>
					</div>
					<div class="col-auto">
						<span class="badge bg-warning text-dark px-3 py-2">지각
							(${late})</span>
					</div>
					<div class="col-auto">
						<span class="badge bg-danger px-3 py-2">결석 (${absent})</span>
					</div>
					<div class="col-auto">
						<span class="badge bg-dark px-3 py-2">미체크 (<c:out
								value="${15 - present - late - absent}" />)
						</span>
					</div>
				</div>

				<div class="row justify-content-center">
					<div class="col-md-4">
						<table class="table table-bordered text-center small align-middle">
							<thead class="table-light">
								<tr>
									<th>주차</th>
									<th>출석 상태</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach begin="1" end="15" var="i">
									<tr>
										<td>${i}주차</td>
										<td><c:set var="found" value="false" /> <c:forEach
												var="dto" items="${list}">
												<c:if test="${dto.week == i}">
													<c:set var="found" value="true" />
													<c:choose>
														<c:when test="${dto.status == 1}">
															<span class="badge bg-success">출석</span>
														</c:when>
														<c:when test="${dto.status == 2}">
															<span class="badge bg-warning text-dark">지각</span>
														</c:when>
														<c:when test="${dto.status == 0}">
															<span class="badge bg-danger">결석</span>
														</c:when>
														<c:otherwise>
															<span class="badge bg-dark">미체크</span>
														</c:otherwise>
													</c:choose>
												</c:if>
											</c:forEach> <c:if test="${!found}">
												<span class="badge bg-dark">미체크</span>
											</c:if></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
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