<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body>
	<nav style="height: 50px;" class="redbox navbar navbar-expand-lg navbar-dark fixed-top">
		<div style="margin-right: 12px;"></div>
		<div><img src="${pageContext.request.contextPath}/dist/images/home.png" class="icon"></div>
		<div class="container-fluid" style="font-size: 26px; color: white; font-weight: bold;">
			페이지제목
		</div>
		<div><img src="${pageContext.request.contextPath}/dist/images/profile.png" class="icon"></div>
		<div style="margin: 10px">&nbsp;</div>
		<div><img src="${pageContext.request.contextPath}/dist/images/logout.png" class="icon" style="margin-right: 28px"></div>
	</nav>
</body>
</html>
