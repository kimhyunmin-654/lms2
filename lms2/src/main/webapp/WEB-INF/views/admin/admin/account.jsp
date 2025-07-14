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
<body>
	<header>
		<jsp:include page="/WEB-INF/views/layout/mainheader.jsp" />
	</header>
	<main>
		<!-- <jsp:include page="/WEB-INF/views/layout/student_mainsidebar.jsp"/> -->
			<div class="container" style="margin-left: 240px; margin-top: 70px;">
				<h2>관리자 등록</h2>
				
				
				<div>
				<form name ="insertForm" method="post">
					<table>
						<tr>
							<td>사진</td>
						</tr>
						
						<tr>
							<th>사번</th>
							<td><input type="text" name="userId"></td>
						</tr>
						<tr>
							<th>패스워드</th>
							<td><input type="text" name="password"></td>
						</tr>
						<tr>
							<th>패스워드 확인</th>
							<td><input type="text" name="passwordConfirm"></td>
						</tr>
						<tr>
							<th>이름</th>
							<td><input type="text"  name="name"></td>
						</tr>
						<tr>
							<th>생년월일</th>
							<td><input type="date" name="birth"></td>
						</tr>
						<tr>
							<th>이메일</th>
							<td><input type="text" name="email"></td>
						</tr>
						<tr>
							<th>전화번호</th>
							<td><input type="text" name="phone"></td>
						</tr>
						<tr>
							<th>주소1</th>
							<td><input type="text" name="addr1"></td>
						</tr>
						<tr>
							<th>주소2</th>
							<td><input type="text" name="addr2"></td>
						</tr>
						<tr>
							<th>직책</th>
							<td><input type="text" name="position"></td>
						</tr>
						<tr>
							<th>부서</th>
							<td><input type="text" name="division"></td>
						</tr>			
					</table>
					
					<table>
						<tr><td><button type="button" onclick="sendOk();">등록 완료</button></td></tr>
					</table>
					
				</form>
				</div>
			</div>
	</main>
	
<script type="text/javascript">
function sendOk() {
	const f = document.insertForm;
	
	f.action = '${pageContext.request.contextPath}/admin/admin/${mode}';
	f.submit();
}
</script>
	
	
</body>
</html>