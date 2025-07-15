<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>학생 리스트</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/main1.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/main2.css">
</head>
<body>
	<header>
		<jsp:include page="/WEB-INF/views/layout/menuheader.jsp"/>
	</header>
	<main>
		<jsp:include page="/WEB-INF/views/layout/admin_mainsidebar.jsp" />

		<div class="container" style="margin-left: 220px; padding: 30px;">

			<div class="body-container row justify-content-center" style="margin: 100px;">
				<h3 style="font-size: 29px;">학생관리</h3>
			</div>
			
		   
			
			<div class="accordion-body custom-submenu"><a href="${pageContext.request.contextPath}/admin/student/account">학생 등록</a></div>
		</div>
	</main>
	<script src="${pageContext.request.contextPath}/dist/js/sidebar-toggle.js"></script>
</body>
</html>