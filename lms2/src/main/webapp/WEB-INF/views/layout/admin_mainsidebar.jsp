<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<div class="blackbox">
	<div class="text-center mb-4">
		<img src="${pageContext.request.contextPath}/dist/images/profile.png" alt="프로필" class="rounded-circle profile-img">
		<div class="username" style="font-size:18px;">김관리자</div>
		<div class="department" style="font-size:12px;">행정실</div>
	</div>

	<div class="accordion accordion-flush" id="sidebarAccordion">
		<div class="accordion-button custom-accordion-btn collapsed">메인페이지</div>

		<div class="accordion-item">
			<h2 class="accordion-header">
				<button class="accordion-button custom-accordion-btn collapsed" type="button"
					data-bs-toggle="collapse" data-bs-target="#collapseOne"
					aria-expanded="false" aria-controls="collapseOne">
					구성원 관리 <span class="ms-auto toggle-icon">+</span>
				</button>
			</h2>
			<div id="collapseOne" class="accordion-collapse collapse"
				data-bs-parent="#sidebarAccordion">
				<div class="accordion-body custom-submenu">학생 관리</div>
				<div class="accordion-body custom-submenu">교수 관리</div>
				<div class="accordion-body custom-submenu">관리자 관리</div>
			</div>
		</div>

		<div class="accordion-button custom-accordion-btn collapsed">강의 관리</div>
		<div class="accordion-button custom-accordion-btn collapsed">공지사항 관리</div>
		<div class="accordion-button custom-accordion-btn collapsed">내 정보 관리</div>
		<div class="accordion-button custom-accordion-btn collapsed">로그아웃</div>
	</div>
</div>