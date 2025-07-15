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
</head>
<body>
	<header>
		<jsp:include page="/WEB-INF/views/layout/header.jsp" />
	</header>

	<main>
		<div class="container">
			<div class="body-container row justify-content-center">
				<div class="col-md-10 my-3 p-3">
					<div class="body-title">
						<h3>
							<i class="bi bi-app"></i> 게시판
						</h3>
					</div>
					
					<div class="body-main">
						<table class="table board-article">
							<thead>
								<tr>
									<td colspan="2" align="center">${dto.subject}</td>
								</tr>
							</thead>
							
							<tbody>
								<tr>
									<td width="50%">이름 : ${dto.userName}</td>
									<td align="right">${dto.reg_date}|조회수 ${dto.hitCount}</td>
								</tr>
							
								<tr>
									<td colspan="2" class="text-center p-3">
										<button type="button" class="btn btn-outline-primary btnSendBoardLike" title="좋아요">
											<span id="boardLikeCount">좋아용!</span>
										</button>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</main>
</body>
</html>