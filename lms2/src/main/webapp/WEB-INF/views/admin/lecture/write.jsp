<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="frm" uri="jakarta.tags.fmt" %>
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
		<jsp:include page="/WEB-INF/views/layout/admin_mainsidebar.jsp"/>
			<div class="container" style="margin-left: 240px; margin-top: 70px;">
				<h2>강의실 등록</h2>

				<div>
					<table>
						<tr>
							<th>강의코드</th>
							<td><input type="text" name="lecture_code"></td>
						</tr>
						<tr>
							<th>과목</th>
							<td><input type="text"></td>
						</tr>
						<tr>
							<th>학년</th>
							<td>
								<select name="GRADE">
									<option value="1">1학년</option>
									<option value="2">2학년</option>
									<option value="3">3학년</option>
									<option value="4">4학년</option>
								</select>
							</td>
						</tr>
						<tr>
							<th>강의실</th>
							<td><input type="text"></td>
						</tr>
						<tr>
							<th>분류</th>
							<td>
								<select name="CLASSIFICATION">
									<option value="major">전공</option>
									<option value="liberal">교양</option>
								</select>
							</td>
						</tr>
						<tr>
							<th>개설 년도</th>
							<td><input type="date"></td>
						</tr>
						<tr>
							<th>학기</th>
							<td>
								<select name="SEASONAL">
									<option value="1">1학기</option>
									<option value="2">2학기</option>
									<option value="3">계절학기</option>
								</select>
							</td>
						</tr>
						<tr>
							<th>수강인원</th>
							<td><input type="number"></td>
						</tr>
						<tr>
							<th>학점</th>
							<td><input type="number"></td>
						</tr>
						<tr>
							<th>학과코드</th>
							<td><select name="DEPARTMENT">
									<option value="CS01">영어영문학과</option>
									<option value="EN01">컴퓨터공학과</option>
								</select>
							</td>
						</tr>
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
					</table>
					
					<table>
						<tr><td><button type="button" onclick="lectureSubmit(this.form);">등록완료</button></td></tr>
					</table>
				</div>
			</div>
	</main>
	
	<script type="text/javascript">
	
	function lectureSubmit() {
		
	}
	
	</script>

</body>
</html>