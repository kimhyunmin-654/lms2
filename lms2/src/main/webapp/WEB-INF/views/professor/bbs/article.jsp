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
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" integrity="sha512-X0sP..." crossorigin="anonymous" referrerpolicy="no-referrer" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/main2.css">
</head>
<body>
	<header>
		<jsp:include page="/WEB-INF/views/layout/menuheader.jsp" />
	</header>
	<main>
		<jsp:include page="/WEB-INF/views/layout/prof_menusidebar.jsp" />
		
		<div class="container">
			<div class="body-container row justify-content-center" style="margin: 100px;">
				<div class="col-md-10 my-3 p-3">
					<div class="body-title">
						
						<table class="table" style="margin-bottom: 30px;">
							<tr>
								<td width="100px;" align="left" style="border-bottom: 3px solid #CF1C31; border-top:none; font-size: 30px; padding-bottom: 0px;">자료실</td>
								<td align="left" style="border-bottom: 1px solid gray; border-top:none;">&nbsp;</td>
								<td align="right" style="border-bottom: 1px solid gray; border-top:none;">&nbsp;</td>
							</tr>
						</table>
					</div>

					<div class="body-main">
						<table class="table board-article">
							<thead>
								<tr style="border-top: 3px solid black;">
									<td width="150px" align="justify">제   목</td>
									<td align="left">${dto.subject}</td>
								</tr>
							</thead>

							<tbody>
								<tr>
									<td width="150px" align="justify">강의 과목</td>
									<td align="left">${dto.lecture_subject}</td>
								</tr>
								<tr>
									<td width="150px" align="justify">조회수</td>
									<td align="left">${dto.hit_count}</td>
								</tr>

								<tr>
									<td colspan="2" valign="top" height="200" style="padding: 20px; min-height:200px;">${dto.content}</td>
								</tr>

								<tr>
								<c:if test="${not empty dto.save_filename}">
									<td colspan="2" align="justify"><i class="fa-solid fa-floppy-disk"></i>&nbsp;&nbsp;&nbsp;<a href="${pageContext.request.contextPath}/lecture/download?num=${dto.data_id}">${dto.original_filename}</a>
									</td>
									</c:if>
								
								</tr>

								<tr>
									<td colspan="2">이전글: 
										<c:if test="${not empty prevDto}">
											<a href="${pageContext.request.contextPath}/professor/bbs/article?num=${prevDto.data_id}&${query}">${prevDto.subject}</a>
										</c:if>
									</td>
								</tr>
								<tr style="border-bottom: 2px solid gray;">
									<td colspan="2">다음글: 
										<c:if test="${not empty nextDto}">
											<a href="${pageContext.request.contextPath}/professor/bbs/article?num=${nextDto.data_id}&${query}">${nextDto.subject}</a>
										</c:if>
									</td>
								</tr>
							</tbody>
						</table>

						<table class="table table-borderless mb-2">
							<tr>
								<td width="50%"><button type="button" class="btn btn-light" onclick="location.href='${pageContext.request.contextPath}/professor/bbs/update?num=${dto.data_id}&page=${page}';">수정</button>
								<td class="text-end">
									<button type="button" class="btn btn-light" onclick="location.href='${pageContext.request.contextPath}/professor/bbs/list?${query}';">리스트</button>
								</td>
							</tr>
						</table>
					</div>
				</div>
			</div>
		</div>
	</main>
	
	<script type="text/javascript">
		function login() {
			location.href = '${pageContext.request.contextPath}/member/login';
		}
		
		function sendAjaxRequest(url, method, params, responseType, fn, file = false) {
			const settings = {
				type: method,
				data: params,
				dataType: responseType,
				success: function(data) {
					fn(data);	
				},
				beforeSend: function(xhr) {
					xhr.setRequestHeader('AJAX', true);
				},
				complete: function() {
				},
				error: function(xhr) {
					if (xhr.status === 403) {
						login();
						return false;
					} else if (xhr.status === 406) {
						alert('요청 처리가 실패했습니다.');
						return false;
					}
					
					console.log(xhr.responseText);
				}
			};
			
			if (file) {
				settings.processData = false;
				settings.contentType = false;
			}
			
			$.ajax(url, settings);
	
		}
	</script>
</body>
</html>