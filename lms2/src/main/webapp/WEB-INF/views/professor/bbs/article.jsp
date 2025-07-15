<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>자료실</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/dist/css/main2.css">
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
									<td width="80px;" align="justify">제   목</td>
									<td align="left">${dto.subject}</td>
								</tr>
							</thead>

							<tbody>
								<tr>
									<td width="80px;" align="justify">작 성 자</td>
									<td align="center">${dto.member_id}</td>
								</tr>
								<tr>
									<td width="80px;" align="justify">강의 과목</td>
									<td align="center">${dto.member_id}</td>
								</tr>
								<tr>
									<td width="80px;" align="justify">조회수</td>
									<td align="left">${dto.hit_count}</td>
								</tr>

								<tr>
									<td colspan="2" valign="top" height="200">${dto.content}</td>
								</tr>

								<tr>
									<td width="80px;" align="justify">파일명</td>
									<td align="left">자바의 이해.pdf</td>
								</tr>

								<tr>
									<td colspan="2">이전글:때문인가
									</td>
								</tr>
								<tr  style="border-bottom: 2px solid gray;">
									<td colspan="2">다음글:때문인가
									</td>
								</tr>
							</tbody>
						</table>

						<table class="table table-borderless mb-2">
							<tr>
								<td width="50%">수정버튼
								<td class="text-end">
									<button type="button" class="btn btn-light"
										onclick="location.href='${pageContext.request.contextPath}/professor/bbs/list?${query}';">리스트</button>
								</td>
							</tr>
						</table>

					</div>
				</div>
			</div>
		</div>
	</main>
</body>
</html>