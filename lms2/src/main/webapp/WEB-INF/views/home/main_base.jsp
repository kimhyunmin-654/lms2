<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>관리자 메인</title>
	<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/main.css">
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body>
	<header>
		<jsp:include page="/WEB-INF/views/layout/mainheader.jsp" />
	</header>

	<jsp:include page="/WEB-INF/views/layout/admin_mainsidebar.jsp" />

	<main class="content" style="margin-left: 220px; padding: 40px;">
		<div class="container">
			<h2 class="text-white">메인 페이지 콘텐츠</h2>
			<p class="text-white">여기에 콘텐츠를 작성합니다.</p>
		</div>
	</main>
</body>
</html>