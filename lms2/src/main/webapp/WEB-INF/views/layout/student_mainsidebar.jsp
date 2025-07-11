<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/dist/css/main.css">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body>
	<div class="text-white p-3 position-fixed blackbox">
		<div class="text-center mb-4">
			<img src="dist/images/profile.png" alt="프로필" class="rounded-circle">
			<p class="mt-2">
				김쌍용(2026022929)<br> <span style="font-size: 12px;">컴퓨터공학과</span>
			</p>
		</div>

		<div class="accordion accordion-flush" id="accordionFlushExample">
			<div class="accordion-item">
				<h2 class="accordion-header">
					<button class="accordion-button collapsed" type="button"
						data-bs-toggle="collapse" data-bs-target="#flush-collapseOne"
						aria-expanded="false" aria-controls="flush-collapseOne">
						강의실</button>
				</h2>
				<div id="flush-collapseOne" class="accordion-collapse collapse"
					data-bs-parent="#accordionFlushExample">
					<div class="accordion-body">수강 과목</div>
					<div class="accordion-body">성적 관리</div>
					<div class="accordion-body">출석 관리</div>
					<div class="accordion-body">수업 일정</div>
				</div>
			</div>

			<div class="accordion-item">
				<h2 class="accordion-header">
					<button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#flush-collapseTwo" aria-expanded="false" aria-controls="flush-collapseTwo">
						학습활동
					</button>
				</h2>
				<div id="flush-collapseTwo" class="accordion-collapse collapse" data-bs-parent="#accordionFlushExample">
					<div class="accordion-body">
						과제
					</div>
					<div class="accordion-body">
						자료실
					</div>
				</div>
			</div>

			<div>학사 공지사항</div>
			<div>내 정보 관리</div>
			<div>로그아웃</div>
		</div>
	</div>
</body>
</html>