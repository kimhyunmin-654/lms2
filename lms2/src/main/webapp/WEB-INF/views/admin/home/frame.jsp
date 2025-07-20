<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>학생 페이지</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/dist/css/main2.css">
</head>
<header>
	<jsp:include page="/WEB-INF/views/layout/mainheader.jsp" />
</header>
<body>
	<main>
		<jsp:include page="/WEB-INF/views/layout/admin_mainsidebar.jsp" />

		<div class="container" style="margin-left: 220px; padding: 30px;">

			<div class="body-container row justify-content-center"
				style="margin: 100px;">
				<h3 style="font-size: 29px;">관리자 메인페이지</h3>
				
				<div class="row my-4">
				<!-- 전체 구성원 -->
				<div class="col-md-6">
				    <div class="card shadow-sm">
				        <div class="card-header fw-bold bg-light">
				             전체 구성원
				        </div>
				        <div class="card-body d-flex flex-column gap-3">
					    <div class="border rounded p-2 bg-white">
					        <a href="${pageContext.request.contextPath}/admin/student/list" class="text-decoration-none text-dark fw-bold">
					            <i class="bi bi-people-fill me-2 text-primary"></i>총 등록 학생 수 : ${dataCount}명
					        </a>
					    </div>
					    <div class="border rounded p-2 bg-white">
					        <a href="${pageContext.request.contextPath}/admin/professor/list" class="text-decoration-none text-dark fw-bold">
					            <i class="bi bi-person-badge-fill me-2 text-success"></i>총 등록 교수 수 : ${dataCount2}명
					        </a>
					    </div>
					    <div class="border rounded p-2 bg-white">
					        <a href="${pageContext.request.contextPath}/admin/lecture/list" class="text-decoration-none text-dark fw-bold">
					            <i class="bi bi-journal-bookmark-fill me-2 text-warning"></i>강의 개설 수 : ${dataCount3}명
					        </a>
					    </div>
				        </div>
				    </div>
				</div>
				    <!-- 학기일정 -->
				    <div class="col-md-6">
				        <div class="card shadow-sm">
				            <div class="card-header fw-bold">
				                 학기일정
				            </div>
				            <div class="card-body">
				                <p>학기 일정 정보를 여기에 추가하세요.</p>
				            </div>
				        </div>
				    </div>
				</div>
				
				<!-- 최근 공지사항 -->
				<div class="row my-3">
				    <div class="col-md-12">
				        <div class="card shadow-sm">
				            <div class="card-header fw-bold bg-light">
				                <i class="bi bi-megaphone text-danger me-2"></i> 최근 공지사항
				            </div>
				            <div class="card-body d-flex flex-column gap-3">				               
				                <c:forEach var="dto" items="${listNotice}">
				                    <div class="border rounded p-2 bg-white">
				                        <a href="${pageContext.request.contextPath}/admin/notice/article?notice_id=${dto.notice_id}"
				                           class="text-decoration-none text-dark text-truncate d-block">
				                        <i class="bi bi-pin-angle-fill me-2 text-secondary"></i> ${dto.subject}
				                         </a>
				                     </div>
				                 </c:forEach>
				                 <c:forEach var="n" begin="${listNotice.size() + 1}" end="5">
				                    <div class="border rounded p-2 bg-white">&nbsp;</div>
				                 </c:forEach>				             
				            </div>
				        </div>
        				<div class="text-end pt-2">
            				<a href="${pageContext.request.contextPath}/admin/notice/list" class="text-decoration-none">더보기</a>
        				</div>
    			</div>
			</div>
			</div>
		</div>
	</main>
	<script
		src="${pageContext.request.contextPath}/dist/js/sidebar-toggle.js"></script>
</body>
</html>