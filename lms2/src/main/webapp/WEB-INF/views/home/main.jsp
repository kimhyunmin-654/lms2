<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
</head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>쌍용대학교 통합 로그인</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/main.css">
</head>
<body style="background: url('${pageContext.request.contextPath}/dist/images/background.jpg') no-repeat center center fixed; background-size: cover;">
	<div class="overlay"></div>
	<div class="content-wrapper">
		<div class="content">
			<div class="login-header">
				<img src="${pageContext.request.contextPath}/dist/images/sangyong_logo.png">
				<h2>통합 로그인</h2>
				<p style="color: white; font-size: 14px;">
					서비스 이용을 끝낸 후에는 개인정보 보호를 위해여 꼭 로그아웃을 해주시길 바랍니다.<br> 아이디는 학생은
					학번, 교수/직원은 포털 아이디(이메일 아이디) 또는 직번입니다.
				</p>
			</div>

			<div class="login-container">
				<form action="${pageContext.request.contextPath}/" method="post"
					class="login-form">
					<label for="userId">아이디/학번</label> 
					<input type="text" id="userId" name="userId"> 
					<label for="password">비밀번호</label> 
					<input type="password" id="password" name="password">

					<div class="find-links">
						<a href="${pageContext.request.contextPath}/">아이디 찾기</a> | 
						<a href="${pageContext.request.contextPath}/">비밀번호 찾기</a>
					</div>

					<button type="submit" class="login-btn">로그인</button>
				</form>
			</div>

			<div style="margin-top: 20px; text-align: center; font-size: 14px; color: #ffcc00; font-weight: bold;">
				<p>초기 비밀번호는 회원 수정에서 반드시 변경 후 사용하여 주시길 바랍니다.</p>
			</div>
		</div>

		<div class="footer-wrapper">
			<div class="login-footer">
				<p>개인정보 처리 방침 | 이메일 무단 수집 거부</p>
				<p>
					쌍용대학교 서울특별시 마포구 월드컵북로 21<br> Tel.02-336-8546~8 Fax.02-334-5405
				</p>
			</div>
		</div>

	</div>
</body>
</html>