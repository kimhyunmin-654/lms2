<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>강의 등록</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
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
                <h3 class="mb-4">강의 등록</h3>

				<form name="postForm"  method="post" enctype="multipart/form-data"
				      class="border p-4 rounded bg-white shadow-sm">
				
				    <input type="hidden" name="size" value="${size}"/>
				
				    
				    <div class="row border-bottom">
				        <div class="col-sm-2 bg-light d-flex align-items-center fw-bold py-3">강의코드</div>
				        <div class="col-sm-10 bg-white py-3">
				            <input type="text" class="form-control" name="lecture_code" value="${dto.lecture_code}" required>
				        </div>
				    </div>
				
				   
				    <div class="row border-bottom">
				        <div class="col-sm-2 bg-light d-flex align-items-center fw-bold py-3">강의 명</div>
				        <div class="col-sm-10 bg-white py-3">
				            <input type="text" class="form-control" name="subject" value="${dto.subject}" required>
				        </div>
				    </div>
				
				    
				    <div class="row border-bottom">
				        <div class="col-sm-2 bg-light d-flex align-items-center fw-bold py-3">학년</div>
				        <div class="col-sm-10 bg-white py-3">
				            <select name="grade">
								<option value="1">1학년</option>
								<option value="2">2학년</option>
								<option value="3">3학년</option>
								<option value="4">4학년</option>
							</select>
				        </div>
				    </div>
				
				   
				    <div class="row border-bottom">
				        <div class="col-sm-2 bg-light d-flex align-items-center fw-bold py-3">강의실</div>
				        <div class="col-sm-10 bg-white py-3">
				            <input type="text" class="form-control" name="classroom" value="${dto.classroom}" required>
				        </div>
				    </div>
				
				    
				    <div class="row border-bottom">
				        <div class="col-sm-2 bg-light d-flex align-items-center fw-bold py-3">분류(전공,교양)</div>
				        <div class="col-sm-10 bg-white py-3">
								<select name="division">
									<option value="전공">전공</option>
									<option value="교양">교양</option>
								</select>
				        </div>
				    </div>
				    
				    <div class="row border-bottom">
				        <div class="col-sm-2 bg-light d-flex align-items-center fw-bold py-3">개설 년도</div>
				        <div class="col-sm-10 bg-white py-3">
				            <input type="number" class="form-control" name="lecture_year" value="${dto.lecture_year}" required>
				        </div>
				    </div>
				    
				    <div class="row border-bottom">
				        <div class="col-sm-2 bg-light d-flex align-items-center fw-bold py-3">학기</div>
				        <div class="col-sm-10 bg-white py-3">
							<select name="semester">
								<option value="1">1학기</option>
								<option value="2">2학기</option>
								<option value="3">계절학기</option>
							</select>
				        </div>
				    </div>
				    
				    <div class="row border-bottom">
				        <div class="col-sm-2 bg-light d-flex align-items-center fw-bold py-3">수강인원</div>
				        <div class="col-sm-10 bg-white py-3">
				            <input type="number" class="form-control" name="capacity" value="${dto.capacity}" required>
				        </div>
				    </div>				    				    				    
				    
				    <div class="row border-bottom">
				        <div class="col-sm-2 bg-light d-flex align-items-center fw-bold py-3">학점</div>
				        <div class="col-sm-10 bg-white py-3">
				            <input type="number" class="form-control" name="credit" value="${dto.credit}" required>
				        </div>
				    </div>					    
				    
				    <div class="row border-bottom">
				        <div class="col-sm-2 bg-light d-flex align-items-center fw-bold py-3">학과코드</div>
				        <div class="col-sm-10 bg-white py-3">
							<select name="department_id">
								<option value="CS01">영어영문학과</option>
								<option value="EN01">컴퓨터공학과</option>
							</select>
				        </div>
				    </div>				    
				   
					<div class="row border-bottom">
					    <div class="col-sm-2 bg-light d-flex align-items-center fw-bold py-3">담당교수</div>
					    <div class="col-sm-10 bg-white py-3">
					        <select name="member_id" class="form-select" required>
					            <c:forEach var="prof" items="${professorList}">
					                <option value="${prof.member_id}"
					                    <c:if test="${dto.member_id == prof.member_id}">selected</c:if> >
					                    ${prof.name} (교번:${prof.member_id})
					                </option>
					            </c:forEach>
					        </select>
					    </div>
					</div>
									    
				    <div class="row border-bottom">
				        <div class="col-sm-2 bg-light d-flex align-items-center fw-bold py-3">강의계획서</div>
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
				            <button type="button" class="btn btn-primary me-2" onclick="lectureSubmit(this.form);">
				                ${mode == 'update' ? '수정' : '등록'}
				            </button>
				            <button type="reset" class="btn btn-warning me-2">다시 입력</button>
				            <a href="${pageContext.request.contextPath}/admin/lecture/list?page=${empty page ? '1' : page}&size=${empty size ? '10' : size}" class="btn btn-secondary">목록</a>
				
							<c:if test="${mode=='update'}">
							    <input type="hidden" name="lecture_code" value="${dto.lecture_code}">
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
</main>

<script src="${pageContext.request.contextPath}/dist/js/sidebar-toggle.js"></script>
<script type="text/javascript">
function lectureSubmit(form) {
	const f = document.postForm;
	let str;
	
	str = f.subject.value.trim();
	if( ! str ) {
		alert('제목을 입력하세요. ');
		f.subject.focus();
		return false;
	}


	
	f.action = '${pageContext.request.contextPath}/admin/lecture/${mode}';
	form.submit();
	
	return true;
	
	
}

<c:if test="${mode=='update'}">
function deleteFile(fileNum) {
	if(! confirm('파일을 삭제 하시겠습니까 ? ')) {
		return;
	}
	
	let params = 'lecture_code=${dto.lecture_code}&file_num=' + fileNum + '&page=${page}&size=${size}'
	let url = '${pageContext.request.contextPath}/admin/lecture/deleteFile?' + params ;
	location.href = url;
}
</c:if>

</script>




</body>
</html>
