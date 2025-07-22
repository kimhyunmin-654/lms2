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
						<table class="table" style="margin-bottom: 30px;">
							<tr>
								<td width="100px;" align="left"class="page-title" style="border-bottom: 3px solid #CF1C31; border-top:none; ">자료실</td>
								<td align="left" style="border-bottom: 1px solid gray; border-top:none;">&nbsp;</td>
								<td align="right" style="border-bottom: 1px solid gray; border-top:none;">&nbsp;</td>
							</tr>
						</table>
					<div class="body-main">
					
						<form name="bbsForm" action="${pageContext.request.contextPath}/professor/bbs/write" method="post" enctype="multipart/form-data">
							<input type="hidden" name="lecture_code" value="${lecture_code}" />
							
							<table class="table">
								<tr>
									<td>제목
									</td>
									<td>
										<input type="text" name="subject" maxlength="100" class="form-control" value="${dto.subject}">
									</td>
								</tr>
							<tr>
								<td>첨부 파일</td>
								<td> 
									<input type="file" name="selectFile" class="form-control">
									<c:if test="${mode=='update' && not empty dto.save_filename}">
										<p class="form-control-plaintext mt-2">
											현재 파일: <a href="${pageContext.request.contextPath}/lecture/download?num=${dto.data_id}">${dto.original_filename}</a>
										</p>
									</c:if>
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
										<button type="button" class="btn btn-light" onclick="location.href='${pageContext.request.contextPath}/professor/bbs/list?lecture_code=${lecture_code}';">${mode=="update"?"수정취소":"등록취소"}<i class="bi bi-x"></i></button>
										<c:if test="${mode=='update'}">
											<input type="hidden" name="num" value="${dto.data_id}">
											<input type="hidden" name="page" value="${page}">
											<input type="hidden" name="lecture_code" value="${lecture_code}">
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

			f.action = '${pageContext.request.contextPath}/professor/bbs/${mode}';
			f.submit();
		}
	</script>
</body>
</html>