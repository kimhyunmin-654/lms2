<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>공지사항</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/main2.css">
</head>
<body>

<header>
    <jsp:include page="/WEB-INF/views/layout/mainheader.jsp" />
</header>


<main>
    <jsp:include page="/WEB-INF/views/layout/student_menusidebar.jsp" />

    <div class="container" style="margin-left: 220px; padding: 30px;">
        <div class="row">
            <div class="col">
                <h3 class="mb-4" style="margin-top: 50px;">공지사항</h3>

                <!-- 상단: 삭제/페이지 선택/글쓰기 -->
                <div class="d-flex justify-content-between mb-2 align-items-center">

					일반글 ${dataCount} 개 / 공지글 ${dataCount2} 개( ${page} / ${total_page} 페이지)
                    <div class="d-flex align-items-center">
                        <c:if test="${dataCount != 0}">
                            <form name="pageSizeForm" class="me-2">
                                <select name="size" class="form-select" onchange="changeList();">
                                    <option value="5" ${size==5 ? "selected":""}>5개씩 출력</option>
                                    <option value="10" ${size==10 ? "selected":""}>10개씩 출력</option>
                                    <option value="20" ${size==20 ? "selected":""}>20개씩 출력</option>
                                    <option value="30" ${size==30 ? "selected":""}>30개씩 출력</option>
                                    <option value="50" ${size==50 ? "selected":""}>50개씩 출력</option>
                                </select>
                                <input type="hidden" name="page" value="${page}">
                                <input type="hidden" name="schType" value="${schType}">
                                <input type="hidden" name="kwd" value="${kwd}">
                            </form>
                        </c:if>

                    </div>
                </div>

                <!-- 공지사항 목록 -->
                <form id="noticeListForm" name="listForm" method="post">
                    <table class="table table-bordered table-hover text-center align-middle">
                        <thead class="table-light">
                            <tr>
                                <th width="10%">번호</th>
                                <th>제목</th>
                                <th width="15%">작성자</th>
                                <th width="15%">등록일</th>
                                <th width="10%">조회수</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="dto" items="${listNotice}">
                                <tr>
                                    <td>
                                        <c:if test="${dto.is_notice == 1}">
                                            <span class="badge bg-warning text-dark">공지</span>
                                        </c:if>
                                    </td>
                                    <td class="text-start">
                                        <a href="${articleUrl}&notice_id=${dto.notice_id}" class="text-reset">${dto.subject}</a>
                                    </td>
                                    <td>${dto.name}</td>
                                    <td>${dto.reg_date}</td>
                                    <td>${dto.hit_count}</td>
                                </tr>
                            </c:forEach>

                            <c:forEach var="dto" items="${list}" varStatus="status">
                                <tr>
                                    <td>${dataCount - (page-1) * size - status.index}</td>
                                    <td class="text-start">
                                        <a href="${articleUrl}&notice_id=${dto.notice_id}" class="text-reset">${dto.subject}</a>
                                    </td>
                                    <td>${dto.name}</td>
                                    <td>${dto.reg_date}</td>
                                    <td>${dto.hit_count}</td>
                                </tr>
                            </c:forEach>

                            <c:if test="${empty list and empty listNotice}">
                                <tr><td colspan="6">등록된 공지사항이 없습니다.</td></tr>
                            </c:if>
                        </tbody>
                    </table>
                </form>

                <!-- 페이징 -->
                <div class="d-flex justify-content-center my-4">
                    ${dataCount == 0 ? "등록된 게시물이 없습니다." : paging}
                </div>

                <!-- 검색 -->
					<div class="row board-list-footer justify-content-between align-items-center mt-3">
					    <div class="col-auto">
					        <button type="button" class="btn btn-light"
					            onclick="location.href='${pageContext.request.contextPath}/student/notice/list';"
					            title="새로고침">새로고침</button>
					    </div>
					
					    <div class="col d-flex justify-content-center">
					        <form name="searchForm" class="d-flex align-items-center gap-2">
					            <select name="schType" class="form-select" style="width: 140px;">
					                <option value="all" ${schType=="all"?"selected":""}>제목+내용</option>
					                <option value="name" ${schType=="name"?"selected":""}>작성자</option>
					                <option value="reg_date" ${schType=="reg_date"?"selected":""}>등록일</option>
					                <option value="subject" ${schType=="subject"?"selected":""}>제목</option>
					                <option value="content" ${schType=="content"?"selected":""}>내용</option>
					            </select>
					
					            <input type="text" name="kwd" value="${kwd}" class="form-control" placeholder="검색어 입력" style="width: 250px;">
					            <input type="hidden" name="size" value="${size}">
					            <input type="hidden" name="page" value="1">
					
					            <button type="button" class="btn btn-light border" onclick="searchList()">검색</button>
					        </form>
					    </div>					
					</div>
				</div>

            </div>
        </div>
</main>

<script src="${pageContext.request.contextPath}/dist/js/sidebar-toggle.js"></script>

<script type="text/javascript">
// 전체 선택 체크박스
window.addEventListener('DOMContentLoaded', () => {

    // 검색 input에서 enter 막기
    const kwdEL = document.querySelector('form[name=searchForm] input[name=kwd]');
    kwdEL.addEventListener('keydown', function(evt) {
        if (evt.key === 'Enter') {
            evt.preventDefault();
            searchList();
        }
    });
});

// 페이지 사이즈 변경
function changeList() {
    const f = document.pageSizeForm;
    const formData = new FormData(f);
    const params = new URLSearchParams(formData).toString();
    location.href = '${pageContext.request.contextPath}/student/notice/list?' + params;
}

// 검색
function searchList() {
    const f = document.searchForm;
    if (!f.kwd.value.trim()) return;

    const formData = new FormData(f);
    const params = new URLSearchParams(formData).toString();
    location.href = '${pageContext.request.contextPath}/student/notice/list?' + params;
}
</script>

</body>
</html>
