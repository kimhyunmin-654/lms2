<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>출석관리</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/dist/css/main2.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/dist/css/mainPage.css">
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
</head>
<body>
	<header>
		<jsp:include page="/WEB-INF/views/layout/menuheader.jsp" />
	</header>
	<main>
		<jsp:include page="/WEB-INF/views/layout/prof_menusidebar.jsp" />
		<div class="container mt-5 ">
			<div style="margin-top: 100px;">
				<div class="main-wrapper">
					<form method="post"
						action="${pageContext.request.contextPath}/professor/attendance/write"
						name="submitForm">
						<input type="hidden" name="lecture_code" value="${lecture_code}" />

						<label for="week">주차 선택</label> <select class="form-select"
							name="week" id="week-select">
							<c:forEach var="i" begin="1" end="15">
								<option value="${i}" ${i == selectedWeek ? "selected" : ""}>${i}주차</option>
							</c:forEach>
						</select>

						<table class="table table-bordered align-middle text-center mt-3">
							<thead class="table-light">
								<tr>
									<th>학번</th>
									<th>이름</th>
									<th>출석 상태</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="dto" items="${list}">
									<tr>
										<td>${dto.member_id} <input type="hidden"
											name="course_id" value="${dto.course_id}" />
										</td>
										<td>${dto.name}</td>
										<td>
											<div class="d-flex justify-content-center gap-3">
												<div class="form-check">
													<input class="form-check-input" type="radio"
														name="status_${dto.course_id}"
														id="present_${dto.course_id}" value="1"
														${dto.status == 1 ? "checked" : ""}> <label
														class="form-check-label" for="present_${dto.course_id}">
														출석 </label>
												</div>
												<div class="form-check">
													<input class="form-check-input" type="radio"
														name="status_${dto.course_id}"
														id="absent_${dto.course_id}" value="0"
														${dto.status == 0 ? "checked" : ""}> <label
														class="form-check-label" for="absent_${dto.course_id}">
														결석 </label>
												</div>
												<div class="form-check">
													<input class="form-check-input" type="radio"
														name="status_${dto.course_id}" id="late_${dto.course_id}"
														value="2" ${dto.status == 2 ? "checked" : ""}> <label
														class="form-check-label" for="late_${dto.course_id}">
														지각 </label>
												</div>
											</div>
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>


						<div class="text-end mt-2">
							<button type="submit" class="btn btn-light" onclick="okSend()">출석
								등록</button>
						</div>
					</form>
				</div>
			</div>
		</div>
	</main>

	<script type="text/javascript">
		// 출석하기
		function okSend() {
			const f = document.submitForm;

			if (!confirm('출석 처리 하시겠습니까?')) {
				return false;
			}

			f.action = '${pageContext.request.contextPath}/professor/attendance/write';
			f.submit();
		}
	</script>
</body>
</html>
