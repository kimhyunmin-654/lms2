<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>과제</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/main2.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/board.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/paginate.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" integrity="sha512-X0sP..." crossorigin="anonymous" referrerpolicy="no-referrer" />
<style type="text/css">
.hw-header img {
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
		<jsp:include page="/WEB-INF/views/layout/student_menusidebar.jsp" />

		<div class="container" style="margin-left: 220px; padding: 30px;">

			<div class="body-container row justify-content-center hw-header" style="margin: 100px;">
				<div style="font-size: 29px; text-align: center; margin-bottom: 30px;">
					<img src="${pageContext.request.contextPath}/dist/images/sangyong_logo_hw3..png">
				</div>

				<table class="table table-hover board-list">
					<thead class="table-light">
						<tr>
							<th width="60">번호</th>
							<th>제목</th>
							<th width="200">등록일</th>
							<th width="200">마감일</th>
							<th width="100">첨부파일</th>
						</tr>
					</thead>

					<tbody>
						<c:forEach var="dto" items="${list}" varStatus="status">
						<tr>
							<td>${dataCount - (page - 1) * size - status.index}</td>
							<td class="left">
								<div class="text-wrap">
									<a href="${articleUrl}&num=${dto.homework_id}" class="text-reset">${dto.subject}</a>
								</div>
							</td>
							<td>${dto.reg_date}</td>
							<td>${dto.deadline_date}</td>
							<td>
								<c:if test="${not empty dto.save_filename}">
									<div>
										<a href="${pageContext.request.contextPath}/lecture/download?num=${dto.homework_id}"><i class="fa-solid fa-floppy-disk"></i></a>
									</div>
								</c:if>
							</td>
						</tr>
						</c:forEach>
					</tbody>
				</table>

				<div class="page-navigation">${dataCount == 0 ? "" : paging}
				</div>

				<div class="row board-list-footer">
					<div class="col">
						<button type="button" class="btn btn-light" onclick="location.href='${pageContext.request.contextPath}/student/hw/list?lecture_code=${lecture_code}';">새로고침</button>
					</div>
					<div class="col-6 d-flex justify-content-center">
						<form class="row" name="searchForm">
							<input type="hidden" name="lecture_code" value="${lecture_code}"> 

							<div class="col-auto p-1">
								<select name="schType" class="form-select">
									<option value="all" ${schType=="all"?"selected":""}>제목+내용</option>
									<option value="userName" ${schType=="userName"?"selected":""}>작성자</option>
									<option value="reg_date" ${schType=="reg_date"?"selected":""}>등록일</option>
									<option value="subject" ${schType=="subject"?"selected":""}>제목</option>
									<option value="content" ${schType=="content"?"selected":""}>내용</option>
								</select>
							</div>
							<div class="col-auto p-1">
								<input type="text" name="kwd" value="${kwd}" class="form-control">
							</div>
							<div class="col-auto p-1">
								<button type="button" class="btn btn-light" onclick="searchList()">검색</button>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>

	</main>
	<script src="${pageContext.request.contextPath}/dist/js/sidebar-toggle.js"></script>
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
			
			let url = '${pageContext.request.contextPath}/student/hw/list';
			location.href = url + '?' + params;
		}
	</script>
</body>
</html>