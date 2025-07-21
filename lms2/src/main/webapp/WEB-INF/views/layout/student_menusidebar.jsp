<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/layout.css">

	<div class="text-white p-3 position-fixed blackbox2">
	<%-- 	
		<form>
			<select name="lecture_code" id="lesson-select"
				onchange="location.href=this.value;">
				<option value="">-- 강의 선택 --</option>

				<c:if test="${empty lectureList}">
					<option disabled>강의 없음</option>
				</c:if>

				<c:set var="thisPage" value="${pageContext.request.requestURI}" />
				<c:forEach var="dto" items="${lectureList}">
				  <option value="${thisPage}?lecture_code=${dto.lecture_code}"
				    <c:if test="${dto.lecture_code == lecture_code}">selected</c:if>>
				    ${dto.subject}
				  </option>
				</c:forEach>

			</select>
		</form>
		 --%>

		<form>
			<select name="lecture_code" id="lesson-select"
				onchange="location.href=this.value;">
				<option value="">-- 강의 선택 --</option>

				<c:if test="${empty lectureList}">
					<option disabled>강의 없음</option>
				</c:if>

				<c:forEach var="dto" items="${lectureList}">
					<option
						value="${pageContext.request.contextPath}/student/study/list?lecture_code=${dto.lecture_code}"
						<c:if test="${dto.lecture_code == lecture_code}">selected</c:if>>
						${dto.subject}</option>
				</c:forEach>
			</select>

		</form>

		<div class="accordion accordion-flush" id="sidebarAccordion">
			<div class="accordion-item">
				<h2 class="accordion-header">
					<button class="accordion-button custom-accordion-btn collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#collapseOne" aria-expanded="false" aria-controls="collapseOne">
						강의실 <span class="ms-auto toggle-icon">+</span>
					</button>
				</h2>
				<div id="collapseOne" class="accordion-collapse collapse"
					data-bs-parent="#sidebarAccordion">
					<div class="accordion-body custom-submenu"><a href="${pageContext.request.contextPath}/student/study/list?lecture_code=${dto.lecture_code}">수강 과목</a></div>
					<div class="accordion-body custom-submenu"><a href="${pageContext.request.contextPath}/student/study/rating">성적 관리</a></div>
					<div class="accordion-body custom-submenu"><a href="${pageContext.request.contextPath}/student/study/attendance?lecture_code=${lecture_code}">출석 관리</a></div>
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
					<div class="accordion-body custom-submenu"><a href="${pageContext.request.contextPath}/student/hw/homework?lecture_code=${dto.lecture_code}">과제</a></div>
					<div class="accordion-body custom-submenu"><a href="${pageContext.request.contextPath}/student/bbs/list?lecture_code=${lecture_code}">자료실</a></div>
				</div>
			</div>

			<div class="accordion-button custom-accordion-btn collapsed"><a href="${pageContext.request.contextPath}/student/notice/list">학사 공지사항</a></div>
			<div class="accordion-button custom-accordion-btn collapsed"><a href="${pageContext.request.contextPath}/student/student/pwd">내 정보 관리</a></div>
			<div class="accordion-button custom-accordion-btn collapsed"><a href="${pageContext.request.contextPath}/home/logout">로그아웃</a></div>
		</div>
	</div>
