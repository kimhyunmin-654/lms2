<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>관리자 정보</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/main2.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/mainPage.css">
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
</head>
<body>

<header>
    <jsp:include page="/WEB-INF/views/layout/mainheader.jsp" />
</header>

<main>
    <jsp:include page="/WEB-INF/views/layout/admin_mainsidebar.jsp" />

    <div class="container mt-5 ">
		<div style="margin-top: 100px;">
		<div class="main-wrapper">
            <div class="col-md-10 row">
				<table class="table" style="margin-bottom: 30px;">
					<tr>
						<td width="150px" align="left" style="border-bottom: 3px solid #CF1C31; border-top:none; font-size: 30px; padding-bottom: 0px;">강의 정보</td>
						<td align="left" style="border-bottom: 1px solid gray; border-top:none;">&nbsp;</td>
						<td align="right" style="border-bottom: 1px solid gray; border-top:none;">&nbsp;</td>
					</tr>
				</table>
                
                <div class="row align-items-center mb-4">

                    <div class="col-md-10">
                        <div class="row border-bottom py-2">
                            <div class="col-sm-3 fw-bold">강의 코드</div>
                            <div class="col-sm-9">${dto.lecture_code}</div>
                        </div>
                        <div class="row border-bottom py-2">
                            <div class="col-sm-3 fw-bold">과목</div>
                            <div class="col-sm-9">${dto.subject}</div>
                        </div>
                        <div class="row border-bottom py-2">
                            <div class="col-sm-3 fw-bold">학년</div>
                            <div class="col-sm-9">${dto.grade}</div>
                        </div>
                        <div class="row border-bottom py-2">
                            <div class="col-sm-3 fw-bold">강의실</div>
                            <div class="col-sm-9">${dto.classroom}</div>
                        </div>
                        <div class="row border-bottom py-2">
                            <div class="col-sm-3 fw-bold">분류(전공,교양)</div>
                            <div class="col-sm-9">${dto.division}</div>
                        </div>
                        <div class="row border-bottom py-2">
                            <div class="col-sm-3 fw-bold">개설 연도</div>
                            <div class="col-sm-9">${dto.lecture_year}</div>
                        </div>
                        <div class="row border-bottom py-2">
                            <div class="col-sm-3 fw-bold">학기</div>
                            <div class="col-sm-9">${dto.semester}</div>
                        </div>
                        <div class="row border-bottom py-2">
                            <div class="col-sm-3 fw-bold">수강 정원</div>
                            <div class="col-sm-9">${dto.capacity}</div>
                        </div>
                        <div class="row border-bottom py-2">
                            <div class="col-sm-3 fw-bold">학점</div>
                            <div class="col-sm-9">${dto.credit}</div>
                        </div>
                         <div class="row border-bottom py-2">
                            <div class="col-sm-3 fw-bold">학과</div>
                            <div class="col-sm-9">${dto.department_name}</div>
                        </div>
                        <div class="row border-bottom py-2">
                            <div class="col-sm-3 fw-bold">학과 코드</div>
                            <div class="col-sm-9">${dto.department_id}</div>
                        </div>
                        <div class="row border-bottom py-2">
                            <div class="col-sm-3 fw-bold">담당 교수</div>
                            <div class="col-sm-9">${dto.name}</div>
                        </div>                       
		                <!-- 첨부파일 -->
		                <c:if test="${not empty listFile}">
		                    <div class="row border-bottom py-2">
		                        <div class="col-sm-3 fw-bold">강의 계획서</div>
		                        <div class="col-sm-9">
		                            <c:forEach var="vo" items="${listFile}" varStatus="status">
		                                <a href="${pageContext.request.contextPath}/admin/lecture/download?file_num=${vo.file_id}">
		                                    <i class="bi bi-download me-1"></i>${vo.original_filename}
		                                </a>
		                                <c:if test="${!status.last}"> | </c:if>
		                            </c:forEach>
		                        </div>
		                    </div>
		                </c:if>
                        
                                                                                                               
                    </div>
                </div>
                
				<div class="text-end mt-4">
					<c:if test="${sessionScope.member.role == 99}">
					    <button type="button" class="btn btn-light"
					            onclick="location.href='${pageContext.request.contextPath}/admin/lecture/update?lecture_code=${dto.lecture_code}&page=${page}&size=${size}';">수정</button>
					    <button type="button" class="btn btn-outline-danger" onclick="deleteOk();">삭제</button>
					</c:if>
					<a href="${pageContext.request.contextPath}/admin/lecture/list?${query}" class="btn btn-secondary">목록</a>
				</div> 
            </div>
        </div>
    </div>
</div>
</main>

<script type="text/javascript">
function deleteOk() {
    if (confirm('강의를 삭제하시겠습니까?')) {
        let url = '${pageContext.request.contextPath}/admin/lecture/delete?member_id=${dto.member_id}&${query}';
        location.href = url;
    }
}
</script>

</body>
</html>
