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
<link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/mainPage.css">
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
</head>
<body>

<header>
    <jsp:include page="/WEB-INF/views/layout/mainheader.jsp" />
</header>

<main>
    <jsp:include page="/WEB-INF/views/layout/admin_mainsidebar.jsp" />

    <div class="container mt-5">
	<div style="margin-top: 100px;">
	<div class="main-wrapper">
            <div class="col-md-15 row">
            <table class="table" style="margin-bottom: 30px;">
				<tr>
					<td width="200px" align="left" style="border-bottom: 3px solid #CF1C31; border-top:none; font-size: 30px; padding-bottom: 0px;">공지사항 등록</td>
					<td align="left" style="border-bottom: 1px solid gray; border-top:none;">&nbsp;</td>
					<td align="right" style="border-bottom: 1px solid gray; border-top:none;">&nbsp;</td>
				</tr>
			</table>

				<form name="noticeForm" action="" method="post" enctype="multipart/form-data"
				      class="border p-4 rounded bg-white shadow-sm">
				
				    <input type="hidden" name="size" value="${size}"/>
				
				    
				    <div class="row border-bottom">
				        <div class="col-sm-2 bg-light d-flex align-items-center fw-bold py-3">제목</div>
				        <div class="col-sm-10 bg-white py-3">
				            <input type="text" class="form-control" name="subject" value="${dto.subject}" required>
				        </div>
				    </div>
				
				   
				    <div class="row border-bottom">
				        <div class="col-sm-2 bg-light d-flex align-items-center fw-bold py-3">작성자</div>
				        <div class="col-sm-10 bg-white py-3">
				            <p class="form-control-plaintext mb-0">${sessionScope.member.name}</p>
				        </div>
				    </div>
				
				    
				    <div class="row border-bottom">
				        <div class="col-sm-2 bg-light d-flex align-items-center fw-bold py-3">내용</div>
				        <div class="col-sm-10 bg-white py-3">
				            <textarea class="form-control" id="ir1" name="content" rows="10" required>${dto.content}</textarea>
				        </div>
				    </div>
				
				   
				    <div class="row border-bottom">
				        <div class="col-sm-2 bg-light d-flex align-items-center fw-bold py-3">공지 등록</div>
				        <div class="col-sm-10 bg-white py-3">
				            <div class="form-check">
				                <input class="form-check-input" type="checkbox" id="is_notice" name="is_notice"
				                       value="1" ${dto.is_notice==1 ? "checked" : "" }>
				                <label class="form-check-label" for="is_notice">공지로 등록</label>
				            </div>
				        </div>
				    </div>
				
				    
				    <div class="row border-bottom">
				        <div class="col-sm-2 bg-light d-flex align-items-center fw-bold py-3">첨부파일</div>
				        <div class="col-sm-10 bg-white py-3">
				            <input type="file" class="form-control" name="upload" multiple>
				        </div>
				    </div>
				    
				    
				    
					<c:if test="${mode == 'update'}">
						<div class="row border-bottom">
						    <div class="col-sm-2 bg-light d-flex align-items-center fw-bold py-3">첨부된파일</div>
						    <div class="col-sm-10 bg-white py-3">
						        <c:forEach var="vo" items="${listFile}">
						            <div class="d-flex align-items-center mb-1">
						                <a href="#" class="text-danger me-2" onclick="deleteFile(${vo.file_id}); return false;">
						                    <i class="bi bi-trash"></i>
						                </a>
						                <span>${vo.original_filename}</span>
						            </div>
						        </c:forEach>
						    </div>
						</div>
					</c:if>		    
				
				   
				    <div class="row mt-4">
				        <div class="col text-end">
				            <button type="button" class="btn btn-primary me-2" onclick="submitContents(this.form);">
				                ${mode == 'update' ? '수정' : '등록'}
				            </button>
				            <button type="reset" class="btn btn-warning me-2">다시 입력</button>
				            <a href="${pageContext.request.contextPath}/admin/notice/list?page=${empty page ? '1' : page}&size=${empty size ? '10' : size}" class="btn btn-secondary">목록</a>
				
							<c:if test="${mode=='update'}">
							    <input type="hidden" name="notice_id" value="${dto.notice_id}">
								<input type="hidden" name="page" value="${empty page ? 1 : page}">
								<input type="hidden" name="size" value="${empty size ? 10 : size}">
							    
							    <c:forEach var="file" items="${listFile}">
							        <input type="hidden" name="saveFilename" value="${file.save_filename}">
							        <input type="hidden" name="originalFilename" value="${file.original_filename}">
							    </c:forEach>
							</c:if>
				        </div>
				    </div>
				
				</form>
            </div>
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
	
	f.action = '${pageContext.request.contextPath}/admin/notice/${mode}';
	
	return true;
	
	
}

<c:if test="${mode=='update'}">
function deleteFile(fileNum) {
	if(! confirm('파일을 삭제 하시겠습니까 ? ')) {
		return;
	}
	
	let params = 'notice_id=${dto.notice_id}&file_num=' + fileNum + '&page=${page}&size=${size}'
	let url = '${pageContext.request.contextPath}/admin/notice/deleteFile?' + params ;
	location.href = url;
}
</c:if>

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
