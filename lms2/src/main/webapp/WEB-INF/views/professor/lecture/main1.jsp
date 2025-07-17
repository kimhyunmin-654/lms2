<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>강의목록</title>
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
						src="${pageContext.request.contextPath}/dist/images/sangyong_logo_bbs.png">
				</div>

				<div class="container mt-4">
					<h3 class="mb-4">강의 목록</h3>
					
					<div class="row border-bottom">
                    <div class="col-sm-2 bg-light d-flex align-items-center fw-bold py-3">제목</div>
                    <div class="col-sm-10 bg-white py-3">${dto.subject}</div>
                </div>

                <div class="row border-bottom">
                    <div class="col-sm-2 bg-light d-flex align-items-center fw-bold py-3">작성자</div>
                    <div class="col-sm-10 bg-white py-3">${dto.classroom}</div>
                </div>

                <div class="row border-bottom">
                    <div class="col-sm-2 bg-light d-flex align-items-center fw-bold py-3">등록일</div>
                    <div class="col-sm-10 bg-white py-3">${dto.division}</div>
                </div>

                <div class="row border-bottom">
                    <div class="col-sm-2 bg-light d-flex align-items-center fw-bold py-3">조회수</div>
                    <div class="col-sm-10 bg-white py-3">${dto.capacity}</div>
                </div>

                <div class="row border-bottom">
                    <div class="col-sm-2 bg-light d-flex align-items-center fw-bold py-3">내용</div>
                    <div class="col-sm-10 bg-white py-3" style="min-height:200px;">${dto.credit}</div>
                </div>
			</div>

				<div class="row board-list-footer">
					<div class="col">
						<button type="button" class="btn btn-light"
							onclick="location.href='${pageContext.request.contextPath}/professor/lecture/compList';">강의목록</button>
					</div>

					<div class="col text-end"></div>
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