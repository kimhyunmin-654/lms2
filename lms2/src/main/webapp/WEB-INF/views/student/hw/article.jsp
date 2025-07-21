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
                            <tr>
                                <th>제목</th>
                                <td>${dto.subject}</td>
                            </tr>
                            <tr>
                                <th>내용</th>
                                <td>${dto.content}</td>
                            </tr>
                            <tr>
                                <th>마감일</th>
                                <td>${dto.deadline_date}</td>
                            </tr>
                            <c:if test="${not empty dto.save_filename}">
                                <tr>
                                    <th>첨부파일</th>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/student/hw/download?assign_id=${dto.member_id}">
                                            <i class="fa-solid fa-floppy-disk"></i>&nbsp;${dto.original_filename}
                                        </a>
                                    </td>
                                </tr>
                            </c:if>
                        </table>

                        <h5 class="mt-5">과제 제출</h5>
                        <form action="${pageContext.request.contextPath}/student/hw/submit" method="post" enctype="multipart/form-data">
                            <input type="hidden" name="assign_id" value="${dto.member_id}" />
                            <input type="hidden" name="lecture_code" value="${param.lecture_code}" />

                            <div class="mb-3">
                                <label for="submit_content" class="form-label">제출 내용</label>
                                <textarea class="form-control" id="submit_content" name="submit_content" rows="5" required></textarea>
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