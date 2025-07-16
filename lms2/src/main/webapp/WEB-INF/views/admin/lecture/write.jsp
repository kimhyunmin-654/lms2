<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="frm" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>강의실 등록</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
</head>
<body>
	<header>
		<jsp:include page="/WEB-INF/views/layout/mainheader.jsp" />
	</header>
	<main>
		<!--<jsp:include page="/WEB-INF/views/layout/admin_mainsidebar.jsp"/>-->
			<div class="container" style="margin-left: 240px; margin-top: 70px;">
				<h2>강의실 등록</h2>

				<div>
				<form name="postForm" method="post" action="/admin/lecutre/write" enctype="multipart/form-data">
					<table>
						<tr>
							<th>강의코드</th>
							<td><input type="text" name="lecture_code"></td>
						</tr>
						<tr>
							<th>강의 명</th>
							<td><input type="text" name = "subject"></td>
						</tr>
						<tr>
							<th>학년</th>
							<td>
								<select name="grade">
									<option value="1">1학년</option>
									<option value="2">2학년</option>
									<option value="3">3학년</option>
									<option value="4">4학년</option>
								</select>
							</td>
						</tr>
						<tr>
							<th>강의실</th>
							<td><input type="text" name = "classroom"></td>
						</tr>
						<tr>
							<th>분류</th>
							<td>
								<select name="division">
									<option value="전공">전공</option>
									<option value="교양">교양</option>
								</select>
							</td>
						</tr>
						<tr>
							<th>개설 년도</th>
							<td><input type="number" name="lecture_year"></td>
						</tr>
						<tr>
							<th>학기</th>
							<td>
								<select name="semester">
									<option value="1">1학기</option>
									<option value="2">2학기</option>
									<option value="3">계절학기</option>
								</select>
							</td>
						</tr>
						<tr>
							<th>수강인원</th>
							<td><input type="number" name="capacity"></td>
						</tr>
						<tr>
							<th>학점</th>
							<td><input type="number" name="credit"></td>
						</tr>
						<tr>
							<th>학과코드</th>
							<td><select name="department_id">
									<option value="CS01">영어영문학과</option>
									<option value="EN01">컴퓨터공학과</option>
								</select>
							</td>
						</tr>
						<tr>
							<th>담당교수</th>
							<td><input type="text" name="member_id"></td>
						</tr>
						<!-- 
						<c:if test="${mode=='update'}">
						<tr>
							<th>첨부</th>
							<td>
								<p class="form-control-plaintext">
									<c:if test="${not empty dto.save_filename}">
										<a href="javascript:deleteFile('${dto.file_id}');"><i class="bi bi-trash"></i></a>
										${dto.original_filename}
									</c:if>
									&nbsp;
								</p>
							</td>
						</tr>
						</c:if>
						 -->
					</table>
					
					<table>
						<tr><td><button type="button" onclick="lectureSubmit(this.form);">등록완료</button></td></tr>
					</table>
					</form>
				</div>
			</div>
	</main>
	
	<script type="text/javascript">
	
	function lectureSubmit() {
		const f = document.postForm;
		
		f.action = '${pageContext.request.contextPath}/admin/lecture/write';
		f.submit();
	}
	
	</script>

</body>
</html>