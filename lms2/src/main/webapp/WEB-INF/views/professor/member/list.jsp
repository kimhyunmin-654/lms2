<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>자료실</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/main2.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/board.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/paginate.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" integrity="sha512-X0sP..." crossorigin="anonymous" referrerpolicy="no-referrer" />
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

			<div class="body-container row justify-content-center bbs-header" style="margin: 100px;">
				<div style="font-size: 29px; text-align: center; margin-bottom: 30px;">
					<img src="${pageContext.request.contextPath}/dist/images/sugangseng.png">
				</div>

				<table class="table table-hover board-list">
					<thead class="table-light">
						<tr>
							<th width="">학번</th>
							<th width="">학과</th>
							<th width="">이름</th>
							<th width="">역할</th>
							<th width="">휴대전화</th>
						</tr>
					</thead>

					<tbody>
						<c:forEach var="dto" items="${list}" varStatus="status">
							<tr>
							    <td>${dto.member_id}</td>
							    <td>${dto.department_name}</td>
							    <td>${dto.name}</td>
							    <td>
							        <c:choose>
							            <c:when test="${dto.role == 1}">학생</c:when>
							            <c:when test="${dto.role == 51}">교수</c:when>
							            <c:otherwise>관리자</c:otherwise>
							        </c:choose>
							    </td>
							    <td>${dto.phone}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>

				<div class="row board-list-footer">
				<c:set var="lecture_code" value="${lecture_code}" /> <!--controller에서 넘긴 lecture_code를 별도로 저장해둠   -->
					<div class="col">
						<button type="button" class="btn btn-light" onclick="location.href='${pageContext.request.contextPath}/professor/member/list?lecture_code=${lecture_code}';">새로고침</button>
					</div>
					<div class="col d-flex justify-content-center">
					        <form name="searchForm" class="d-flex align-items-center gap-2">
					            <select name="schType" class="form-select" style="width: 140px;">
					                <option value="all" ${schType=="number"?"selected":""}>학번</option>
					                <option value="name" ${schType=="name"?"selected":""}>이름</option>
					                <option value="department_id" ${schType=="department_id"?"selected":""}>학과코드</option>
					            </select>
					
					            <input type="text" name="kwd" value="${kwd}" class="form-control" placeholder="검색어 입력" style="width: 250px;">
					           	<input type="hidden" name="lecture_code" value="${lecture_code}">

					            <button type="button" class="btn btn-light border" onclick="searchList()">검색</button>
					        </form>
					    </div>		
				</div>
			</div>
		</div>

	</main>
	<script>
		src="${pageContext.request.contextPath}/dist/js/sidebar-toggle.js"></script>
	<script type="text/javascript">
		// 검색 키워드 입력란에서 엔터를 누른 경우 서버 전송 막기 
		window.addEventListener('DOMContentLoaded', () => {
			const inputEL = document.querySelector('form input[name=kwd]'); 
			inputEL.addEventListener('keydown', function (evt) {
			    if(evt.key === 'Enter') {
			    	evt.preventDefault();
			    	
			    	searchList();
			    }
			});
		});
		
		function searchList() {
			const f = document.searchForm;
			if(! f.kwd.value.trim()) {
				return;
			}
			const formData = new FormData(f);
			let params = new URLSearchParams(formData).toString();
			
			location.href = '${pageContext.request.contextPath}/professor/member/list?' + params;
		}
	</script>
</body>
</html>