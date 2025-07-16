<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
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
		<!--<jsp:include page="/WEB-INF/views/layout/student_mainsidebar.jsp"/>-->
			<div class="container" style="margin-left: 240px; margin-top: 70px;">
				<h2>출석 관리</h2>
		
				<div>
				 <form name="postForm" method="post" action="/professor/attendance/write" enctype="multipart/form-data">
					<table>
						<tr>
							<th>강의실 이름</th>
							<td><input type="text" name="course_id"></td>
						</tr>	
						<tr>
							<th>상태</th>
							<td><input type="radio" value="1"  name="status" checked>출석</td>
							<td><input type="radio" value="0"  name="status">결석</td>
						</tr>
					</table>
					
					<table>
						<tr><td><button type="button" onclick="attendanceSubmit(this.form);">등록하기</button></td></tr>
					</table>
					</form>
				</div>
			</div>
	</main>
	
	<script type="text/javascript">
	function attendanceSubmit() {
		const f = document.postForm;
		
		f.action = '${pageContext.request.contextPath}/professor/attendance/write';
		f.submit();
	}

	
	</script>
</body>
</html>