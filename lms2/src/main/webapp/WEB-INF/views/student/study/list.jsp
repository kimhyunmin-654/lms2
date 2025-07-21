<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>학생 페이지</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/dist/css/main2.css">
</head>
<header>
	<jsp:include page="/WEB-INF/views/layout/menuheader.jsp" />
</header>
<body>
	<main>
		<jsp:include page="/WEB-INF/views/layout/student_menusidebar.jsp" />

		<div class="container" style="margin-left: 220px; padding: 30px;">

			<div class="body-container row justify-content-center"
				style="margin: 100px;">
				<h3 style="font-size: 29px;">학생 강의 정보페이지</h3>

				<div class="container text-center">

					<!-- 강의자료 -->
					<div class="row my-3">
						<div class="col-md-12">
							<div class="card shadow-sm">
								<div class="card-header fw-bold bg-light">
									<i class="bi bi-folder2-open me-2 text-primary"></i> 강의자료
								</div>
								<div class="card-body d-flex flex-column gap-3 px-2">
									<c:forEach var="dto" items="${lectureFileList}">
										<div class="border rounded p-2 bg-white text-truncate">
											<a href="${pageContext.request.contextPath}/student/bbs/article?data_id=${dto.data_id}"
											   class="text-decoration-none text-dark d-block">
												<i class="bi bi-file-earmark-text me-2 text-secondary"></i>
												${dto.subject}
											</a>
										</div>
									</c:forEach>
									<c:forEach var="n" begin="${list.size() + 1}" end="5">
										<div class="border rounded p-2 bg-white">&nbsp;</div>
									</c:forEach>
								</div>
							</div>
						</div>
					</div>


					<!-- 과제 -->
					<div class="row my-3">
						<div class="col-md-12">
							<div class="card shadow-sm">
								<div class="card-header fw-bold bg-light">
									<i class="bi bi-pencil-square me-2 text-success"></i> 과제 목록
								</div>
								<div class="card-body d-flex flex-column gap-3 px-2">
									<c:forEach var="dto" items="${hwList}">
										<div class="border rounded p-2 bg-white text-truncate">
											<a
												href="${pageContext.request.contextPath}/student/hw/article?homework_id=${dto.homework_id}"
												class="text-decoration-none text-dark d-block"> <i
												class="bi bi-journal-text me-2 text-secondary"></i>
												${dto.subject}
											</a>
										</div>
									</c:forEach>
									<c:forEach var="n" begin="${assignmentList.size() + 1}" end="5">
										<div class="border rounded p-2 bg-white">&nbsp;</div>
									</c:forEach>
								</div>
							</div>
						</div>
					</div>




				</div>



			</div>
		</div>
	</main>
	<script
		src="${pageContext.request.contextPath}/dist/js/sidebar-toggle.js"></script>
</body>
</html>