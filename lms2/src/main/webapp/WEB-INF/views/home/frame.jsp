<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
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
		<jsp:include page="/WEB-INF/views/layout/student_mainsidebar.jsp"/>
			<div class="container" style="margin-left: 240px; margin-top: 70px;">
				<h2>메인페이지 콘텐츠</h2>
				<p>내용</p>
			</div>
	</main>
</body>
</html>