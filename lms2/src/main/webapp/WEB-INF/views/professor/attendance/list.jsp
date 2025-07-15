<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>출석관리</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
</head>
<body>
	<header>
		<jsp:include page="/WEB-INF/views/layout/mainheader.jsp" />
	</header>
	<main>
		<jsp:include page="/WEB-INF/views/layout/prof_mainsidebar.jsp"/>
			<div class="container" style="margin-left: 220px; padding: 30px;">

			<div class="body-container row justify-content-center" style="margin: 100px;">
				<h3 style="font-size: 29px;">출석 관리</h3>
			</div>
			
		   
			
			<div class="accordion-body custom-submenu"><a href="${pageContext.request.contextPath}/professor/attendance/write">교수 등록</a></div>
		</div>
	</main>
	<script src="${pageContext.request.contextPath}/dist/js/sidebar-toggle.js"></script>
</body>
</html>