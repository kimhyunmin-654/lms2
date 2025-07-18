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
		<jsp:include page="/WEB-INF/views/layout/prof_menusidebar.jsp" />
		<div class="container" style="margin-left: 240px; margin-top: 70px;">
			<form method="post" action="${pageContext.request.contextPath}/professor/attendance/write" name="submitForm">
				<input type="hidden" name="lecture_code" value="${lecture_code}" />
				
				<label for="week">주차 선택</label> 
				<select class="form-select" name="week" id="week-select">
					<c:forEach var="i" begin="1" end="15">
						<option value="${i}" ${i == selectedWeek ? "selected" : ""}>${i}주차</option>
					</c:forEach>
				</select>

				<table border="1">
					<tr>
						<th>학번</th>
						<th>이름</th>
						<th>출결 상태</th>
					</tr>

					<c:forEach var="dto" items="${list}">
						<tr>
							<td>${dto.member_id}
								<input type="hidden" name="course_id" value="${dto.course_id}" />
							</td>
							<td>${dto.name}</td>
							<td>
								<label><input type="radio" name="status_${dto.course_id}" value="1" ${dto.status == 1 ? "checked" : ""} checked> 출석</label> 
								<label><input type="radio" name="status_${dto.course_id}" value="0" ${dto.status == 0 ? "checked" : ""}> 결석</label>
								<label><input type="radio" name="status_${dto.course_id}" value="2" ${dto.status == 2 ? "checked" : ""}> 지각</label>
							</td>
						</tr>
					</c:forEach>
				</table>

				<div class="text-end mt-2">
					<button type="submit" class="btn btn-light" onclick="okSend()">출석 등록</button>
				</div>
			</form>

		</div>
	</main>

	<script type="text/javascript">
	// 출석하기
	function okSend() {
		const f = document.submitForm;
		
		
		
		if (! confirm('출석 처리 하시겠습니까?')) {
			return false;
		}

		f.action = '${pageContext.request.contextPath}/professor/attendance/write';
		f.submit();
	}
	</script>
</body>
</html>
