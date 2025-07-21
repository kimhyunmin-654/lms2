<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>성적관리</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/main2.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/mainPage.css">
<link rel="icon" href="data:;base64,iVBORw0KGgo=">

<link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/board.css">
<style type="text/css">
.bbs-header img {
	height: 50px;
	margin-bottom: 10px;
}
</style>
</head>
<body>
	<header>
		<jsp:include page="/WEB-INF/views/layout/menuheader.jsp" />
	</header>
	<main>
		<jsp:include page="/WEB-INF/views/layout/prof_menusidebar.jsp" />

		<div class="container" style="margin-left: 220px; padding: 30px;">

			<div class="body-container row justify-content-center bbs-header"
				style="margin: 100px;">
				<div
					style="font-size: 29px; text-align: center; margin-bottom: 30px;">
					<img
						src="${pageContext.request.contextPath}/dist/images/sungjuk.png">
				</div>

				<form id="weekForm" method="get" action="${pageContext.request.contextPath}/professor/rating/write">
				    <input type="hidden" name="lecture_code" value="${lecture_code}" />
				
				    <div class="d-flex justify-content-end mb-3 gap-2">
				        <button type="button" class="btn btn-outline-secondary"
				            onclick="location.href='${pageContext.request.contextPath}/professor/rating/list?lecture_code=${lecture_code}'">
				            새로고침
				        </button>
				        <button type="button" class="btn btn-danger"
				            onclick="location.href='${pageContext.request.contextPath}/professor/rating/write?lecture_code=${lecture_code}'">
				            성적관리
				        </button>
				    </div>
				</form>

				<table class="table table-hover board-list mt-1">
					<thead class="table-light">
						<tr>
							<th>학번</th>
							<th>이름</th>
							<th>중간</th>
							<th>기말</th>
							<th>출석</th>
							<th>과제</th>
							<th>등급</th>
						</tr>
					</thead>
					<tbody>
						 <c:choose>
            <c:when test="${empty list}">
                <tr>
                    <td colspan="8" class="text-center text-muted">
                        성적관리 리스트에 추가할 데이터가 없습니다. 수강 학생이 없습니다.
                    </td>
                </tr>
            </c:when>
            <c:otherwise>
                <c:forEach var="dto" items="${list}">
                    <tr>
                        <td>${dto.member_id}</td>
                        <td>${dto.name}</td>
                        <td>${dto.middletest_rating}</td>
                        <td>${dto.finaltest_rating}</td>
                        <td>${dto.attendance_rating}</td>
                        <td>${dto.homework_rating}</td>
                        <td>${dto.rating}</td>
                    </tr>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </tbody>
</table>
			</div>



		</div>

	</main>
</body>
</html>