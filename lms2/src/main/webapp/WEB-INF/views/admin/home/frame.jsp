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
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/dist/css/mainPage.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/dist/css/board.css">
</head>
<header>
	<jsp:include page="/WEB-INF/views/layout/mainheader.jsp" />
</header>
<body>
	<main>
		<jsp:include page="/WEB-INF/views/layout/admin_mainsidebar.jsp" />

		<div class="container mt-5" >
		<div style="margin-top: 100px;">
			<div class="main-wrapper body-container row justify-content-center">
				
				<!-- <h3 style="font-size: 29px;">관리자 메인페이지</h3> -->
				<table class="table" style="margin-bottom: 30px;">
					<tr>
						<td width="250px" align="left" style="border-bottom: 3px solid #CF1C31; border-top:none; font-size: 30px; padding-bottom: 0px;">관리자 메인페이지</td>
						<td align="left" style="border-bottom: 1px solid gray; border-top:none;">&nbsp;</td>
						<td align="right" style="border-bottom: 1px solid gray; border-top:none;">&nbsp;</td>
					</tr>
				</table>
				
				<div class="row my-4 gx-3">
				<div class="row mt-4">
				<!-- 전체 구성원 -->
				<div class="col-md-8 mb-4">
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
					        <a href="${pageContext.request.contextPath}/admin/admin/list" class="text-decoration-none text-dark fw-bold">
					            <i class="bi bi-journal-bookmark-fill me-2 text-warning"></i>총 등록 관리자 수 : ${dataCount3}명
					        </a>
					    </div>
				        </div>
				    </div>
				</div>
				<!-- 강의 목록 -->
				<div class="col-md-8">
				    <div class="card shadow-sm h-100">
				        <div class="card-header fw-bold bg-light">
				            <i class="bi bi-megaphone text-danger me-2"></i> 최근 강의 목록
				        </div>
				        <div class="card-body d-flex flex-column gap-3">
				            <c:forEach var="dto" items="${listLecture}">
				                <div class="border rounded p-2 bg-white">
				                        <div class="fw-bold text-truncate">
				                    		<a href="${pageContext.request.contextPath}/admin/lecture/article?lecture_code=${dto.lecture_code}"  class="text-decoration-none text-dark d-block">
				                            	<i class="bi bi-pin-angle-fill me-2 text-secondary"></i>강의명 : ${dto.subject}
				                    		</a>
				                        </div>
				                        <div class="small text-muted mt-1">
				                            <span class="badge bg-secondary me-1">${dto.division}</span>
				                            <span class="badge bg-info text-dark me-1">${dto.department_name}</span>
				                            <span class="badge bg-light text-dark">${dto.name}</span>
				                        </div>
				                </div>
				            </c:forEach>
				
				            <c:forEach var="n" begin="${listLecture.size() + 1}" end="5">
				                <div class="border rounded p-2 bg-white">&nbsp;</div>
				            </c:forEach>
				            
						    <div class="text-end pt-2 px-3">
						        <a href="${pageContext.request.contextPath}/admin/lecture/list" class="text-decoration-none">더보기</a>
						    </div>
				        </div> 
				    </div> 
				    
				</div>
				
				<!-- 최근 공지사항 -->
				    <div class="col-md-8 mt-4">
				        <div class="card shadow-sm">
				            <div class="card-header fw-bold bg-light">
				                <i class="bi bi-megaphone text-danger me-2"></i> 최근 공지사항 목록
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
				                 				             
		        				<div class="text-end pt-2">
		            				<a href="${pageContext.request.contextPath}/admin/notice/list" class="text-decoration-none">더보기</a>
		        				</div>
				            </div>
				        </div>
    				</div>
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