<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>학생 페이지</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/main2.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/mainPage.css">
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
</head>
<header>
	<jsp:include page="/WEB-INF/views/layout/menuheader.jsp" />
</header>
<body>
	<main>
		<jsp:include page="/WEB-INF/views/layout/student_menusidebar.jsp" />

		<div class="container mt-5 ">
			<div style="margin-top: 100px;">
				<div class="main-wrapper">
					<table class="table" style="margin-bottom: 30px;">
						<tr>
							<td width="400px" align="left" class="page-title" style="border-bottom: 3px solid #CF1C31; border-top:none; ">학생 강의 정보페이지</td>
							<td align="left" style="border-bottom: 1px solid gray; border-top:none;">&nbsp;</td>
							<td align="right" style="border-bottom: 1px solid gray; border-top:none;">&nbsp;</td>
						</tr>
					</table>

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
												<a
													href="${pageContext.request.contextPath}/student/bbs/article?data_id=${dto.data_id}"
													class="text-decoration-none text-dark d-block"> <i
													class="bi bi-file-earmark-text me-2 text-secondary"></i>
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
										<c:forEach var="n" begin="${assignmentList.size() + 1}"
											end="5">
											<div class="border rounded p-2 bg-white">&nbsp;</div>
										</c:forEach>
									</div>
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