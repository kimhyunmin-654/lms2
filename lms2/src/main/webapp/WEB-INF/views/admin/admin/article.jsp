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
</head>
<body>

<header>
    <jsp:include page="/WEB-INF/views/layout/mainheader.jsp" />
</header>

<main>
    <jsp:include page="/WEB-INF/views/layout/admin_mainsidebar.jsp" />

    <div class="container" style="margin-left: 220px; padding: 30px;">
        <div class="row justify-content-center">
            <div class="col-md-10">

                <h3 class="mb-4 mt-5">관리자 정보</h3>
                
                <div class="row align-items-center mb-4">
                    <div class="col-md-2 text-center">
                        <c:choose>
                            <c:when test="${not empty dto.avatar}">
                                <img src="${pageContext.request.contextPath}/uploads/member/${dto.avatar}" 
                                     alt="프로필 이미지" class="img-thumbnail rounded-circle" style="width:100px; height:100px;">
                            </c:when>
                            <c:otherwise>
                                <div class="text-muted">등록된 이미지 없음</div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="col-md-10">
                        <div class="row border-bottom py-2">
                            <div class="col-sm-3 fw-bold">사번</div>
                            <div class="col-sm-9">${dto.member_id}</div>
                        </div>
                        <div class="row border-bottom py-2">
                            <div class="col-sm-3 fw-bold">이름</div>
                            <div class="col-sm-9">${dto.name}</div>
                        </div>
                        <div class="row border-bottom py-2">
                            <div class="col-sm-3 fw-bold">생년월일</div>
                            <div class="col-sm-9">${dto.birth}</div>
                        </div>
                        <div class="row border-bottom py-2">
                            <div class="col-sm-3 fw-bold">전화번호</div>
                            <div class="col-sm-9">${dto.phone}</div>
                        </div>
                        <div class="row border-bottom py-2">
                            <div class="col-sm-3 fw-bold">우편번호</div>
                            <div class="col-sm-9">${dto.zip}</div>
                        </div>
                        <div class="row border-bottom py-2">
                            <div class="col-sm-3 fw-bold">주소</div>
                            <div class="col-sm-9">
                                ${dto.addr1}<br>
                                ${dto.addr2}
                            </div>
                        </div>
                        <div class="row border-bottom py-2">
                            <div class="col-sm-3 fw-bold">직책</div>
                            <div class="col-sm-9">${dto.position}</div>
                        </div>
                        <div class="row border-bottom py-2">
                            <div class="col-sm-3 fw-bold">부서</div>
                            <div class="col-sm-9">${dto.division}</div>
                        </div>
                    </div>
                </div>
                
				<div class="text-end mt-4">
				    <c:choose>
				        <c:when test="${sessionScope.member.member_id == 'admin'}">
				            <button type="button" class="btn btn-light"
				                    onclick="location.href='${pageContext.request.contextPath}/admin/admin/update?member_id=${dto.member_id}&page=${page}&size=${size}';">수정</button>
				            <button type="button" class="btn btn-outline-danger" onclick="deleteOk();">삭제</button>
				        </c:when>
				
				        <c:when test="${sessionScope.member.member_id == dto.member_id}">
				            <button type="button" class="btn btn-light"
				                    onclick="location.href='${pageContext.request.contextPath}/admin/admin/update?member_id=${dto.member_id}&page=${page}&size=${size}';">수정</button>
				        </c:when>			
				        <c:otherwise>
				        </c:otherwise>
				    </c:choose>				   
				    <a href="${pageContext.request.contextPath}/admin/admin/list?${query}" class="btn btn-secondary">목록</a>
				</div>
            </div>
        </div>
    </div>
</main>

<script type="text/javascript">
function deleteOk() {
    if (confirm('관리자를 삭제하시겠습니까?')) {
        let url = '${pageContext.request.contextPath}/admin/admin/delete?member_id=${dto.member_id}&${query}';
        location.href = url;
    }
}
</script>

</body>
</html>
