<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/main.css">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body>
	<div class="blackbox">
		<div class="text-center mb-4">
		
			<img src="${pageContext.request.contextPath}/dist/images/profile.png"
				alt="프로필" class="rounded-circle profile-img">
			<div class="username" style="font-size: 18px;">${sessionScope.member.name}(${sessionScope.member.member_id})</div>
			<div class="department" style="font-size: 12px;">>>학과자리</div>
		</div>

		<div class="accordion accordion-flush" id="sidebarAccordion">
			<div class="accordion-item">
				<h2 class="accordion-header">
					<button class="accordion-button custom-accordion-btn collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#collapseOne" aria-expanded="false" aria-controls="collapseOne">
						강의실 <span class="ms-auto toggle-icon">+</span>
					</button>
				</h2>
				<div id="collapseOne" class="accordion-collapse collapse"
					data-bs-parent="#sidebarAccordion">
					<div class="accordion-body custom-submenu"><a href="${pageContext.request.contextPath}/student/study/list">수강 과목</a></div>
					<div class="accordion-body custom-submenu"><a href="${pageContext.request.contextPath}/student/study/rating">성적 관리</a></div>
					<div class="accordion-body custom-submenu"><a href="${pageContext.request.contextPath}/student/study/attendance">출석 관리</a></div>
					<div class="accordion-body custom-submenu"><a href="${pageContext.request.contextPath}/student/study/schedule">수업 일정</a></div>
					<div class="accordion-body custom-submenu"><a href="${pageContext.request.contextPath}/student/lecture/list">수강신청</a></div>
				</div>
			</div>

			<div class="accordion-item">
				<h2 class="accordion-header">
					<button class="accordion-button custom-accordion-btn collapsed" type="button" data-bs-toggle="collapse"	data-bs-target="#collapseTwo" aria-expanded="false" aria-controls="collapseTwo">
						학습활동 <span class="ms-auto toggle-icon">+</span>
					</button>
				</h2>
				<div id="collapseTwo" class="accordion-collapse collapse"
					data-bs-parent="#sidebarAccordion">
					<div class="accordion-body custom-submenu"><a href="${pageContext.request.contextPath}/student/bbs/homework">과제</a></div>
					<div class="accordion-body custom-submenu"><a href="${pageContext.request.contextPath}/student/bbs/list">자료실</a></div>
				</div>
			</div>

			<div class="accordion-button custom-accordion-btn collapsed"><a href="${pageContext.request.contextPath}/student/notice/list">학사 공지사항</a></div>
			<div class="accordion-button custom-accordion-btn collapsed">내 정보 관리</div>
			<div class="accordion-button custom-accordion-btn collapsed"><a href="${pageContext.request.contextPath}/home/logout">로그아웃</a></div>
		</div>
	</div>
</body>
</html>