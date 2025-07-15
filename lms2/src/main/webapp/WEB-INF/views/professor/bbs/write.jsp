<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>자료실</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/main2.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/board.css">
</head>
<body>
	<header>
		<jsp:include page="/WEB-INF/views/layout/menuheader.jsp" />
	</header>
	<main>
		<jsp:include page="/WEB-INF/views/layout/student_menusidebar.jsp" />

		<div class="container" style="margin-left: 220px; padding: 30px;">
			<div class="container">
				<div class="body-container row justify-content-center">
					<div class="col-md-10 my-3 p-3">
					<div class="body-title">
						<h3>자료실</h3>
					</div>
					
					<div class="body-main">
						<form name="bbsForm">
							<table class="table">
								<tr>
									<td>제목
									</td>
									<td>
										<input type="text" name="subject" maxlength="100" class="form-control" value="">
									</td>
								</tr>
								
								<tr>
									<td>강의 과목
									</td>
									<td>
										<input type="text">
									</td>
								</tr>
								
								<tr>
									<td>업로드파일
									</td>
									<td>
										<input type="text">
									</td>
								</tr>
								
								<tr>
									<td>내용
									</td>
									<td>
										<textarea name="content" class="form-control"></textarea>
									</td>
								</tr>
							</table>
						</form>
					</div>
					</div>
				</div>
			</div>
		</div>
	</main>
</body>
</html>