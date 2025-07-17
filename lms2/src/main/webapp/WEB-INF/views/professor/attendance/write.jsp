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
							<th>수강번호</th>
							<th>학생 ID</th>
							<th>이름</th>
							<th>출결 상태</th>
						</tr>	
						<c:forEach var="dto" items="${list}">
							<tr>
								<td>
									${dto.course_id}
									<input type="hidden" name="course_id" value="${dto.course_id}" />
								</td>
								<td>${dto.member_id}</td>
								<td>${dto.name}</td>
								<td>
									<label><input type="radio" name="status_${dto.course_id}" value="1" checked> 출석</label>
									<label><input type="radio" name="status_${dto.course_id}" value="0"> 결석</label>
									<label><input type="radio" name="status_${dto.course_id}" value="2"> 지각</label>
								</td>
								<td><button type="button" onclick="attendanceSubmit(this.form);">등록하기</button></td>
							</tr>
					</c:forEach>
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