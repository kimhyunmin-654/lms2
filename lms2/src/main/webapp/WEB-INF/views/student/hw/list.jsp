<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>과제 목록</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/main2.css">
</head>
<body>
	<header>
		<jsp:include page="/WEB-INF/views/layout/menuheader.jsp" />
	</header>
	<main>
		<jsp:include page="/WEB-INF/views/layout/student_menusidebar.jsp" />

		<div class="container" style="margin-left: 220px; padding: 30px;">
			<div class="body-container row justify-content-center" style="margin: 100px;">
				<div class="col-md-10 my-3 p-3">
					<div class="body-title">
						<h3>과제 목록</h3>
					</div>

					<div class="body-main">
						<table class="table table-bordered">
							<thead>
								<tr>
									<th>No</th>
									<th>과제명</th>
									<th>제출 상태</th>
									<th>마감일</th>
								</tr>
							</thead>
							<tbody>
								<c:choose>
									<c:when test="${not empty list}">
										<c:forEach var="dto" items="${list}" varStatus="status">
											<tr>
												<td>${dataCount - ((page - 1) * size) - status.index}</td>
												<td>
													<!-- 상세 페이지 이동 링크 (중요 수정) -->
													<a href="${articleUrl}&assign_id=${dto.assign_id}">
														${dto.assign_name}
													</a>
												</td>
												<td>
													<c:choose>
														<c:when test="${dto.assign_status == 1}">제출 완료</c:when>
														<c:otherwise>미제출</c:otherwise>
													</c:choose>
												</td>
												<td>${dto.submit_date}</td>
											</tr>
										</c:forEach>
									</c:when>
									<c:otherwise>
										<tr>
											<td colspan="4">등록된 과제가 없습니다.</td>
										</tr>
									</c:otherwise>
								</c:choose>
							</tbody>
						</table>

						<!-- 페이징 처리 -->
						<div class="text-center">
							${paging}
						</div>

					</div>
				</div>
			</div>
		</div>
	</main>
</body>
</html>