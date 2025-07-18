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
	<div class="text-white p-3 position-fixed blackbox2">
		<div class="accordion accordion-flush" id="sidebarAccordion">
			<c:out value="${lectureList}" />
			<form>
			    <select name="lesson" id="lesson-select">
			        <option value="">-- 강의 선택 --</option>
			
			        <c:if test="${empty lectureList}">
			            <option disabled>강의 없음</option>
			        </c:if>
			
			        <c:forEach var="lecture" items="${lectureList}">
			            <option value="${lecture.lecture.lecture_code}">
			                ${lecture.lecture.subject}
			            </option>
			        </c:forEach>
			    </select>
			</form>
			<div class="accordion-item">
				<h2 class="accordion-header">
					<button class="accordion-button custom-accordion-btn collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#collapseOne" aria-expanded="false" aria-controls="collapseOne">
						강의실 <span class="ms-auto toggle-icon">+</span>
					</button>
				</h2>
				<div id="collapseOne" class="accordion-collapse collapse"
					data-bs-parent="#sidebarAccordion">
					<div class="accordion-body custom-submenu"><a href="${pageContext.request.contextPath}/professor/lecture/compList?lecture_code=${dto.lecture_code}">강의 목록</a></div>
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
					<div class="accordion-body custom-submenu"><a href="${pageContext.request.contextPath}/professor/hw/list?lecture_code=${dto.lecture_code}">과제</a></div>
					<div class="accordion-body custom-submenu"><a href="${pageContext.request.contextPath}/professor/bbs/list?lecture_code=${dto.lecture_code}">자료실</a></div>
					<div class="accordion-body custom-submenu"><a href="${pageContext.request.contextPath}/professor/member/list?lecture_code=${dto.lecture_code}">수강생 관리</a></div>
					<div class="accordion-body custom-submenu"><a href="${pageContext.request.contextPath}">성적 관리</a></div>
					<div class="accordion-body custom-submenu"><a href="${pageContext.request.contextPath}/professor/attendance/list?lecture_code=${dto.lecture_code}">출석 관리</a></div>
				</div>
			</div>
			
			<div class="accordion-item">
				<h2 class="accordion-header">
					<button class="accordion-button custom-accordion-btn collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#collapseThree" aria-expanded="false" aria-controls="collapseThree">
						기타 <span class="ms-auto toggle-icon">+</span>
					</button>
				</h2>
				<div id="collapseThree" class="accordion-collapse collapse" data-bs-parent="#sidebarAccordion">
					<div class="accordion-body custom-submenu">학사 공지사항</div>
					<div class="accordion-body custom-submenu">내 정보 관리</div>
				</div>
			</div>
			<div class="accordion-button custom-accordion-btn collapsed"><a href="${pageContext.request.contextPath}/home/logout">로그아웃</a></div>
		</div>
	</div>
</body>


</html>