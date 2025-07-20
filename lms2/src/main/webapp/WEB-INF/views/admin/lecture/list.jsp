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
    <jsp:include page="/WEB-INF/views/layout/admin_mainsidebar.jsp" />

    <div class="container" style="margin-left: 220px; padding: 30px;">
        <div class="row">
            <div class="col">
                <h3 class="mb-4" style="margin-top: 50px;">강의 리스트</h3>

                <!-- 상단: 삭제/페이지 선택/글쓰기 -->
                <div class="d-flex justify-content-between mb-2 align-items-center">
                    <button class="btn btn-danger" id="btnDeleteList">삭제</button>
					강의수 ${dataCount} 개  ${page} / ${total_page} 페이지
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

                        <a href="${pageContext.request.contextPath}/admin/lecture/write" class="btn btn-primary">
                            <i class="bi bi-pencil-square"></i> 등록
                        </a>
                    </div>
                </div>

                <!-- 공지사항 목록 -->
                <form id="noticeListForm" name="listForm" method="post">
                    <table class="table table-bordered table-hover text-center align-middle" style="width:100%;">
                        <thead class="table-light">
                            <tr>
                                <th width="4%">
                                    <input type="checkbox" id="chkAll" class="form-check-input">
                                </th>
                                <th width="9%">강의코드</th>
                                <th width="12%">강의 명</th>
                                <th width="6%">학년</th>
                                <th width="10%">강의실</th>
                                <th style="width: 10%; white-space: nowrap;">분류(전공,교양)</th>
                                <th width="8%">개설 연도</th>
                                <th width="6%">학기</th>
                                <th width="8%">수강 정원</th>
                                <th width="6%">학점</th>
                                <th width="11%">학과</th>
                                <th width="10%">학과코드</th>
                                <th style="width: 8%; white-space: nowrap;">담당 교수</th>
                                
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="dto" items="${list}" varStatus="status">
                                <tr>
                                    <td><input type="checkbox" name="nums" value="${dto.lecture_code}" class="row-check"></td>
                                    <td>                                 
                                        <a href="${articleUrl}&lecture_code=${dto.lecture_code}" class="text-reset">${dto.lecture_code}</a>
                                    </td>
                                    <td class="text-start">
                                        <a href="${articleUrl}&lecture_code=${dto.lecture_code}" class="text-reset">${dto.subject}</a>
                                    </td>
                                    <td>${dto.grade}</td>
                                    <td>${dto.classroom}</td>
                                    <td style="white-space: nowrap;">${dto.division}</td>
                                    <td>${dto.lecture_year}</td>
                                    <td>${dto.semester}</td>
                                    <td>${dto.capacity}</td>
                                    <td>${dto.credit}</td>
                                    <td>${dto.department_name}</td>
                                    <td>${dto.department_id}</td>
                                    <td style="white-space: nowrap;">${dto.name}</td>
                                </tr>
                            </c:forEach>

                            <c:if test="${empty list}">
                                <tr><td colspan="11">등록된 강의가 없습니다.</td></tr>
                            </c:if>
                        </tbody>
                    </table>
                </form>

                <!-- 페이징 -->
                <div class="d-flex justify-content-center my-4">
                    ${dataCount == 0 ? "등록된 강의가 없습니다." : paging}
                </div>

                <!-- 검색 -->
					<div class="row board-list-footer justify-content-between align-items-center mt-3">
					    <div class="col-auto">
					        <button type="button" class="btn btn-light"
					            onclick="location.href='${pageContext.request.contextPath}/admin/lecture/list';"
					            title="새로고침">새로고침</button>
					    </div>
					
					    <div class="col d-flex justify-content-center">
					        <form name="searchForm" class="d-flex align-items-center gap-2">
					            <select name="schType" class="form-select" style="width: 140px;">
					                <option value="all" ${schType=="all"?"selected":""}>과목+분류</option>
					                <option value="division" ${schType=="division"?"selected":""}>분류(전공,교양)</option>
					                <option value="lecture_year" ${schType=="lecture_year"?"selected":""}>개설 연도</option>
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
    const btnDeleteEL = document.querySelector('button#btnDeleteList');
    const chkAllEL = document.querySelector('input#chkAll');
    const numsELS = document.querySelectorAll('form input[name=nums]');

    btnDeleteEL.addEventListener('click', () => {
        const f = document.listForm;
        const checkedELS = document.querySelectorAll('form input[name=nums]:checked');

        if (checkedELS.length === 0) {
            alert('삭제할 게시물을 먼저 선택하세요.');
            return;
        }

        if (confirm('선택한 게시물을 삭제하시겠습니까?')) {
			console.log(f)
;            f.action = '${pageContext.request.contextPath}/admin/lecture/deleteList';
            f.submit();
        }
    });

    chkAllEL.addEventListener('click', () => {
        numsELS.forEach(inputEL => inputEL.checked = chkAllEL.checked);
    });

    numsELS.forEach(el => {
        el.addEventListener('click', () => {
            const checked = document.querySelectorAll('form input[name=nums]:checked');
            chkAllEL.checked = numsELS.length === checked.length;
        });
    });

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
    location.href = '${pageContext.request.contextPath}/admin/lecture/list?' + params;
}

// 검색
function searchList() {
    const f = document.searchForm;
    if (!f.kwd.value.trim()) return;

    const formData = new FormData(f);
    const params = new URLSearchParams(formData).toString();
    location.href = '${pageContext.request.contextPath}/admin/lecture/list?' + params;
}
</script>

</body>
</html>
