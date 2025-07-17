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
</head>
<body>
	<header>
		<jsp:include page="/WEB-INF/views/layout/menuheader.jsp" />
	</header>
	<main>
		<jsp:include page="/WEB-INF/views/layout/prof_menusidebar.jsp" />

		<div class="container" style="margin-left: 220px; padding: 30px;">
			<div class="container ">
				<div class="body-container row justify-content-center" style="margin: 100px;">
					<div class="col-md-10 my-3 p-3">
					<div class="body-title">
						<h3>자료실</h3>
					</div>
					
					<div class="body-main">
						<form name="bbsForm" action="${pageContext.request.contextPath}/professor/hw/write" method="post">
							<table class="table">
								<tr>
									<td>제목
									</td>
									<td>
										<input type="text" name="subject" maxlength="100" class="form-control" value="${dto.subject}">
									</td>
								</tr>
								
								<tr>
									<td>강의 과목
									</td>
									<td>
										<select name="lesson" id="lesson-select">
											<option value="CS01">a</option>
											<option value="CS02">b</option>
											<option value="CS03">c</option>
											<option value="CS04">d</option>
											<option value="CS05">e</option>
											<option value="CS06">f</option>
										</select>
									</td>
								</tr>
								
								<tr>
									<td>업로드파일
									</td>
									<td>
										<input type="text" disabled>
									</td>
								</tr>
								
								<tr>
									<td>내용
									</td>
									<td>
										<textarea rows="15" name="content" class="form-control" style="resize: none;">${dto.content}</textarea>
									</td>
								</tr>
							</table>
							
							<table class="table table-borderless">
								<tr>
									<td class="text-center">
										<button type="button" class="btn btn-dark" onclick="sendOk();">${mode=="update"?"수정완료":"등록완료"}</button>
										<button type="reset" class="btn btn-light">다시입력</button>
										<button type="button" class="btn btn-light" onclick="location.href='${pageContext.request.contextPath}/professor/hw/list';">${mode=="update"?"수정취소":"등록취소"}<i class="bi bi-x"></i></button>
										<c:if test="${mode=='update'}">
											<input type="hidden" name="num" value="${dto.num}">
											<input type="hidden" name="page" value="${page}">
										</c:if>
									</td>
								</tr>
							</table>
						</form>
					</div>
					</div>
				</div>
			</div>
		</div>
	</main>
	
	<script type="text/javascript">
		function sendOk() {
			const f = document.bbsForm;
			let str;
			
			str = f.subject.value.trim();
			if( ! str ) {
				alert('제목을 입력하세요. ');
				f.subject.focus();
				return;
			}
	
			str = f.content.value.trim();
			if( ! str ) {
				alert('내용을 입력하세요. ');
				f.content.focus();
				return;
			}
	
			f.action = '${pageContext.request.contextPath}/professor/hw/${mode}';
			f.submit();
		}
	</script>
</body>
</html>