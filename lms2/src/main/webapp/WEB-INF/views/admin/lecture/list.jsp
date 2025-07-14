<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="frm" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
</head>
<body>
	<header>
		<jsp:include page="/WEB-INF/views/layout/mainheader.jsp" />
	</header>
	<main>
	<!--<jsp:include page="/WEB-INF/views/layout/admin_mainsidebar.jsp"/>-->
			<div class="container" style="margin-left: 240px; margin-top: 70px;">
				<h2>강의실 관리</h2>
				<p>내용</p>
			</div>
			<a href="write">
  				<button type="button">강의실 등록</button>
			</a>

	</main>
</body>
</html>