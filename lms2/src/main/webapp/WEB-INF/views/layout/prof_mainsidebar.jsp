<%@ page contentType="text/html; charset=UTF-8"%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/layout.css">

	<div class="blackbox">
		<div class="text-center mb-4">
			<img src="${not empty sessionScope.member.avatar ? pageContext.request.contextPath.concat('/uploads/member/').concat(sessionScope.member.avatar) : pageContext.request.contextPath.concat('/dist/images/user.png')}" 
     alt="프로필" class="rounded-circle profile-img">
			<div class="username" style="font-size: 18px;">${sessionScope.member.name}</div>
			<!-- <div class="department" style="font-size: 12px;">학과명</div> -->
		</div>
		<div class="accordion-button custom-accordion-btn collapsed"><a href="${pageContext.request.contextPath}/professor/main/main">메인 페이지</a></div>
		<div class="accordion accordion-flush" id="sidebarAccordion">
			<div class="accordion-item">
				<h2 class="accordion-header">
					<button class="accordion-button custom-accordion-btn collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#collapseOne" aria-expanded="false" aria-controls="collapseOne">
						강의실 <span class="ms-auto toggle-icon">+</span>
					</button>
				</h2>
				<div id="collapseOne" class="accordion-collapse collapse"
					data-bs-parent="#sidebarAccordion">
					<div class="accordion-body custom-submenu"><a href="${pageContext.request.contextPath}/professor/lecture/compList">강의 목록</a></div>
					<div class="accordion-body custom-submenu">수업 일정</div>
				</div>
			</div>

			<div class="accordion-item">
				<h2 class="accordion-header">
					<button class="accordion-button custom-accordion-btn collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#collapseTwo" aria-expanded="false" aria-controls="collapseTwo">
						학습활동 <span class="ms-auto toggle-icon">+</span>
					</button>
				</h2>
				<div id="collapseTwo" class="accordion-collapse collapse"
					data-bs-parent="#sidebarAccordion">
					<div class="accordion-body custom-submenu"><a href="${pageContext.request.contextPath}/professor/hw/list">과제</a></div>
					<div class="accordion-body custom-submenu"><a href="${pageContext.request.contextPath}/professor/bbs/list">자료실</a></div>
					<div class="accordion-body custom-submenu"><a href="${pageContext.request.contextPath}/professor/member/list">수강생 관리</a></div>
					<div class="accordion-body custom-submenu"><a href="${pageContext.request.contextPath}/professor/rating/list">성적 관리</a></div>
					<div class="accordion-body custom-submenu"><a href="${pageContext.request.contextPath}/professor/attendance/list">출석 관리</a></div>
				</div>
			</div>
			
			<div class="accordion-item">
				<h2 class="accordion-header">
					<button class="accordion-button custom-accordion-btn collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#collapseThree" aria-expanded="false" aria-controls="collapseThree">
						기타 <span class="ms-auto toggle-icon">+</span>
					</button>
				</h2>
				<div id="collapseThree" class="accordion-collapse collapse" data-bs-parent="#sidebarAccordion">
					<div class="accordion-body custom-submenu"><a href="${pageContext.request.contextPath}/professor/notice/list">학사 공지사항</a></div>
					<div class="accordion-body custom-submenu"><a href="${pageContext.request.contextPath}/professor/information/pwd">내 정보 관리</a></div>
				</div>
			</div>
			<div class="accordion-button custom-accordion-btn collapsed"><a href="${pageContext.request.contextPath}/home/logout">로그아웃</a></div>
		</div>
	</div>
