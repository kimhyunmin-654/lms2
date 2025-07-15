<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>공지사항 등록</title>
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
        <div class="row justify-content-center mt-5">
            <div class="col-md-8">
                <h3 class="mb-4">공지사항 등록</h3>

                <form name = "noticeForm" action="${pageContext.request.contextPath}/admin/notice/account" method="post" enctype="multipart/form-data" class="border p-4 rounded bg-light">
                    <input type="hidden" name="size" value="${size}"/>

                    <div class="mb-3">
                        <label for="subject" class="form-label">제목</label>
                        <input type="text" class="form-control" id="subject" name="subject" required>
                    </div>

                    <div class="mb-3">
                        <label for="content" class="form-label">내용</label>
                        <textarea class="form-control" id="ir1" name="content" rows="7" required></textarea>
                    </div>

                    <div class="mb-3 form-check">
                        <input type="checkbox" class="form-check-input" id="notice" name="notice" value="1">
                        <label class="form-check-label" for="notice">공지 등록</label>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">첨부파일</label>
                        <input type="file" class="form-control" name="selectFile" multiple>
                    </div>

                    <div class="d-flex justify-content-between">
                        <button type="submit" class="btn btn-primary" onclick="submitContents(this.form);">등록</button>
                        <button type="reset" class="btn btn-warning ms-2">다시 입력</button>
                        <a href="${pageContext.request.contextPath}/admin/notice/list?size=${size}" class="btn btn-secondary">목록</a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</main>

<script src="${pageContext.request.contextPath}/dist/js/sidebar-toggle.js"></script>
<script type="text/javascript">
function check() {
	const f = document.noticeForm;
	let str;
	
	str = f.subject.value.trim();
	if( ! str ) {
		alert('제목을 입력하세요. ');
		f.subject.focus();
		return false;
	}

	str = f.content.value.trim();
	if( ! str || str === '<p><br></p>' ) {
		alert('내용을 입력하세요. ');
		return false;
	}
	
	return true;
}
</script>

<script type="text/javascript" src="${pageContext.request.contextPath}/dist/vendor/se2/js/service/HuskyEZCreator.js" charset="utf-8"></script>
<script type="text/javascript">
var oEditors = [];
nhn.husky.EZCreator.createInIFrame({
	oAppRef: oEditors,
	elPlaceHolder: 'ir1',
	sSkinURI: '${pageContext.request.contextPath}/dist/vendor/se2/SmartEditor2Skin.html',
	fCreator: 'createSEditor2',
	fOnAppLoad: function(){
		// 로딩 완료 후
		oEditors.getById['ir1'].setDefaultFont('돋움', 12);
	},
});

function submitContents(elClickedObj) {
	 oEditors.getById['ir1'].exec('UPDATE_CONTENTS_FIELD', []);
	 try {
		if(! check()) {
			return;
		}
		
		elClickedObj.submit();
		
	} catch(e) {
	}
}
</script>


</body>
</html>
