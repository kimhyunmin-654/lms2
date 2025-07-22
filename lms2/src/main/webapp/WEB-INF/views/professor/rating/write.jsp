<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>성적관리</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/dist/css/main2.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/dist/css/board.css">
<style type="text/css">
.bbs-header img {
	height: 50px;
	margin-bottom: 10px;
}
</style>
</head>
<body>
	<header>
		<jsp:include page="/WEB-INF/views/layout/menuheader.jsp" />
	</header>
	<main>
		<jsp:include page="/WEB-INF/views/layout/prof_menusidebar.jsp" />
		
		<div class="container" style="margin-left: 220px; padding: 30px;">
		<div class="body-container row justify-content-center bbs-header"
				style="margin: 100px;">
				<div style="font-size: 29px; text-align: center; margin-bottom: 30px;">
					<img src="${pageContext.request.contextPath}/dist/images/sungjuk.png">
				</div>
					
			<form method="post" action="${pageContext.request.contextPath}/professor/rating/write" name="submitForm">
				<input type="hidden" name="lecture_code" value="${lecture_code}" />
				<table class="table table-hover board-list mt-4">
					<thead class="table-light">
						<tr>
						<th>학번</th>
		                <th>이름</th>
		                <th>중간</th>
		                <th>기말</th>
		                <th>출석</th>
		                <th>과제</th>
		                <th>총점</th>
                		<th>등급</th>
					</tr>
					<c:forEach var="dto" items="${list}">
					    <tr>
					        <td>
							    ${dto.member_id}
							    <input type="hidden" name="member_id" value="${dto.member_id}" />
							    <input type="hidden" name="course_id" value="${dto.course_id}" />
							</td>
					        <td>${dto.name}</td>
					        <td><input type="number" name="middletest_rating" value="${dto.middletest_rating}" min="0" max="100" required /></td>
							<td><input type="number" name="finaltest_rating" value="${dto.finaltest_rating}" min="0" max="100" required /></td>
							<td><input type="number" name="attendance_rating" value="${dto.attendance_rating}" min="0" max="100" required /></td>
							<td><input type="number" name="homework_rating" value="${dto.homework_rating}" min="0" max="100" required /></td>
					        <td><input type="number" name="total_rating" readonly /></td>
					        <td><input type="text" name="rating" readonly /></td>
					    </tr>
					</c:forEach>
				</table>
				
				<table class="table table-borderless">
					<tr>
						<td class="text-center">
					<button type="submit" class="btn btn-danger" onclick="okSend()">성적 등록</button>
					</tr>
				</table>
			</form>
		</div>
	</div>
</main>

	<script type="text/javascript">
	function okSend() {
		const f = document.submitForm;

		if (! confirm('성적 처리 하시겠습니까?')) {
			return false;
		}

		f.action = '${pageContext.request.contextPath}/professor/rating/write';
		f.submit();
	}
	
	// 총점 및 등급 계산 함수
	function calculateScores() {
		const rows = document.querySelectorAll("table tr");

		rows.forEach(row => {
			const midInput = row.querySelector('input[name="middletest_rating"]');
			const finInput = row.querySelector('input[name="finaltest_rating"]');
			const attInput = row.querySelector('input[name="attendance_rating"]');
			const hwInput = row.querySelector('input[name="homework_rating"]');
			const totalInput = row.querySelector('input[name="total_rating"]');
			const gradeInput = row.querySelector('input[name="rating"]');

			if (!midInput || !finInput || !attInput || !hwInput || !totalInput || !gradeInput) return;

			const mid = parseFloat(midInput.value) || 0;
			const fin = parseFloat(finInput.value) || 0;
			const att = parseFloat(attInput.value) || 0;
			const hw = parseFloat(hwInput.value) || 0;

			const total = (mid + fin + att + hw) / 4;
			totalInput.value = total.toFixed(2); // 소수 둘째 자리까지

			let grade = "F";
			if (total === 100) grade = "A+";
			else if (total >= 95) grade = "A";
			else if (total >= 90) grade = "A-";
			else if (total >= 85) grade = "B+";
			else if (total >= 80) grade = "B";
			else if (total >= 75) grade = "B-";
			else if (total >= 70) grade = "C+";
			else if (total >= 65) grade = "C";
			else if (total >= 60) grade = "C-";
			else if (total >= 50) grade = "D";

			gradeInput.value = grade;
		});
	}

	// 입력 변경 시 계산
	document.querySelectorAll('input[name="middletest_rating"], input[name="finaltest_rating"], input[name="attendance_rating"], input[name="homework_rating"]').forEach(input => {
		input.addEventListener('input', calculateScores);
	});

	// 페이지 로드 시 자동 계산
	window.addEventListener('load', calculateScores);
	
	function okSend() {
		const f = document.submitForm;

		// 성적 입력값 확인
		const inputs = f.querySelectorAll('input[name="middletest_rating"], input[name="finaltest_rating"], input[name="attendance_rating"], input[name="homework_rating"]');
		for (let input of inputs) {
			const value = input.value.trim();

			if (value === "") {
				alert("모든 성적 항목을 입력해주세요.");
				input.focus();
				return false;
			}
			const num = parseFloat(value);
			if (isNaN(num) || num < 0 || num > 100) {
				alert("성적은 0부터 100 사이의 숫자여야 합니다.");
				input.focus();
				return false;
			}
		}

		if (!confirm('성적 처리 하시겠습니까?')) {
			return false;
		}

		f.submit();
	}
	</script>
</body>
</html>
