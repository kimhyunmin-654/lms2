<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>과제 상세</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/main2.css">
</head>
<body>
    <header>
        <jsp:include page="/WEB-INF/views/layout/menuheader.jsp" />
    </header>

    <main>
        <jsp:include page="/WEB-INF/views/layout/student_menusidebar.jsp" />

        <div class="container" style="margin-left: 220px; padding: 30px;">
            <div class="body-container row justify-content-center" style="margin: 100px;">
                <div class="col-md-10 my-3 p-3">
                    <div class="body-title mb-4">
                        <h3>과제 상세</h3>
                    </div>

                    <div class="body-main">
                        <table class="table table-bordered">
							<tr style="border-top: 3px solid black;">
								<td width="150px" align="justify">제   목</td>
								<td align="left">${dto.subject}</td>
							</tr>
							<tr>
								<td width="150px" align="justify">조회수</td>
								<td align="left">${dto.hit_count}</td>
							</tr>
								<tr>
									<td colspan="2" valign="top" height="200" style="padding: 20px; min-height:200px;">${dto.content}</td>
								</tr>

                            <tr>
                                <th>마감일</th>
                                <td>${dto.deadline_date}</td>
                            </tr>
							<tr>
								<c:if test="${not empty dto.save_filename}">
									<td colspan="2" align="justify">파일명: <i class="fa-solid fa-floppy-disk"></i>&nbsp;&nbsp;&nbsp;<a href="${pageContext.request.contextPath}/lecture/download?num=${dto.homework_id}">${dto.original_filename}</a></td>
								</c:if>
							</tr>
                        </table>

						<h5 class="mt-5">과제 제출</h5>
						<form action="${pageContext.request.contextPath}/student/hw/submit" method="post" enctype="multipart/form-data">
						    <input type="hidden" name="homework_id" value="${dto.homework_id}" />
						    <input type="hidden" name="course_id" value="${dto.course_id}" />
						    <input type="hidden" name="lecture_code" value="${param.lecture_code}" />
						
						    <div class="mb-3">
						        <label for="assign_content" class="form-label">제출 내용</label>
						        <textarea class="form-control" id="assign_content" name="submit_content" rows="5" required></textarea>
						    </div>
						
						    <div class="mb-3">
						        <label for="upload" class="form-label">첨부파일</label>
						        <input type="file" class="form-control" id="upload" name="upload" />
						    </div>
						
						    <div class="text-center">
						        <button type="submit" class="btn btn-primary">제출하기</button>
						        <a href="${pageContext.request.contextPath}/student/hw/list?lecture_code=${param.lecture_code}" class="btn btn-secondary">목록</a>
						    </div>
						</form>

                    </div>
                </div>
            </div>
        </div>
    </main>
</body>
</html>