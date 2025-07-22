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

    <div class="container mt-5 ">
		<div style="margin-top: 100px;">
		<div class="main-wrapper">
            <div class="col-md-10">
            <table class="table" style="margin-bottom: 30px;">
				<tr>
					<td width="150px" align="left" class="page-title" style="border-bottom: 3px solid #CF1C31; border-top:none; ">강의 등록</td>
					<td align="left" style="border-bottom: 1px solid gray; border-top:none;">&nbsp;</td>
					<td align="right" style="border-bottom: 1px solid gray; border-top:none;">&nbsp;</td>
				</tr>
			</table>
				<form name="postForm"  method="post" enctype="multipart/form-data"
				      class="border p-4 rounded bg-white shadow-sm">
				
				    <input type="hidden" name="size" value="${size}"/>
				
				    
				    <div class="row border-bottom">
				        <div class="col-sm-2 bg-light d-flex align-items-center fw-bold py-3">강의코드</div>
				        <div class="col-sm-10 bg-white py-3">
				            <input type="text" class="form-control" name="lecture_code" value="${dto.lecture_code}" placeholder="예: CS24101 → 컴공과(24년도 1학기 01번 강의)"  required>
				            <p class="form-text text-muted small">영어영문학과: ELL, 컴퓨터공학과: CS, 영어교육과: ENGED, 국어국문학과: KLL, 역사학과: HIS, 철학과: PHIL, 심리학과: PSY, 사회학과: SOC, 경영학과:	BA, 경제학과: ECON, 행정학과: PAD, 정치외교학과: POL, 소프트웨어학과: SW, 전자공학과: EE, 
								기계공학과: ME, 화학공학과: CHE, 산업공학과: IE, 수학과: MA, 생명과학과: BIO, 물리학과: PH</p>
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
				            <input type="number" class="form-control" name="lecture_year" value="${dto.lecture_year}" placeholder="개설연도는 2000~2099 사이의 4자리 숫자" required>
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
				            <input type="number" class="form-control" name="capacity" value="${dto.capacity}" placeholder="수강인원은 1~999 사이여야 합니다." required>
				        </div>
				    </div>				    				    				    
				    
				    <div class="row border-bottom">
				        <div class="col-sm-2 bg-light d-flex align-items-center fw-bold py-3">학점</div>
				        <div class="col-sm-10 bg-white py-3">
				            <input type="number" class="form-control" name="credit" value="${dto.credit}" placeholder="학점은 0.5 단위로 1 ~ 6 사이여야 합니다. (예: 2, 3.5, 4)" required>
				        </div>
				    </div>					    
				    
				    <div class="row border-bottom">
				        <div class="col-sm-2 bg-light d-flex align-items-center fw-bold py-3">학과코드</div>
				        <div class="col-sm-10 bg-white py-3">
							<select name="department_id" class="form-select">
								<option value="">학과 코드를 선택하세요</option>
								<option value="ELL">영어영문학과(ELL)</option>
								<option value="CS">컴퓨터공학과(CS)</option>
								<option value="KLL">국어국문학과(KLL)</option>
								<option value="HIS">역사학과(HIS)</option>
								<option value="PHIL">철학과(PHIL)</option>
								<option value="PSY">심리학과(PSY)</option>
								<option value="SOC">사회학과(SOC)</option>
								<option value="BA">경영학과(BA)</option>
								<option value="ECON">경제학과(ECON)</option>
								<option value="PAD">행정학과(PAD)</option>
								<option value="POL">정치외교학과(POL)</option>
								<option value="SW">소프트웨어학과(SW)</option>
								<option value="EE">전자공학과(EE)</option>
								<option value="ME">기계공학과(ME)</option>
								<option value="CHE">화학공학과(CHE)</option>
								<option value="IE">산업공학과(IE)</option>
								<option value="MA">수학과(MA)</option>
								<option value="BIO">생명과학과(BIO)</option>
								<option value="PH">물리학과(PH)</option>
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
</div>
</main>

<script src="${pageContext.request.contextPath}/dist/js/sidebar-toggle.js"></script>
<script type="text/javascript">
function lectureSubmit(form) {
	const f = document.postForm;
	let str, p;
	
	p = /^[A-Z]{2,4}[0-9]{2}[1-3][0-9]{2}$/;
	str = f.lecture_code.value.trim();
	if( ! p.test(str) ) {
		alert('강의코드를 입력하세요. ');
		f.lecture_code.focus();
		return false;
	}
	
	str = f.subject.value.trim();
	if( ! str ) {
		alert('강의 명을 입력하세요. ');
		f.subject.focus();
		return false;
	}
	
	str = f.grade.value.trim();
	if( ! str ) {
		alert('학년을 등록하세요. ');
		f.grade.focus();
		return false;
	}	
	
	str = f.classroom.value.trim();
	if( ! str ) {
		alert('강의실을 입력하세요. ');
		f.classroom.focus();
		return false;
	}	
	
	str = f.division.value.trim();
	if( ! str ) {
		alert('분류(전공,교양)을 등록하세요. ');
		f.division.focus();
		return false;
	}	
	
	
	p = /^(20)[0-9]{2}$/;
	str = f.lecture_year.value.trim();
	if( ! p.test(str) ) {
		alert('개설연도는 2000~2099 사이의 4자리 숫자여야 합니다. ');
		f.lecture_year.focus();
		return false;
	}
	
	str = f.semester.value.trim();
	if( ! str ) {
		alert('학기 등록하세요. ');
		f.semester.focus();
		return false;
	}		
		
	
	p = /^[1-9][0-9]{0,2}$/;
	str = f.capacity.value.trim();
	if( ! p.test(str) ) {
		alert('수강인원은 1~999 사이여야 합니다. ');
		f.capacity.focus();
		return false;
	}
	
	p = /^(?:[1-5](?:\.5)?|6(?:\.0)?)$/;
	str = f.credit.value.trim();
	if( ! p.test(str) ) {
		alert('학점은 0.5 단위로 1 ~ 6 사이여야 합니다. (예: 2, 3.5, 4) ');
		f.credit.focus();
		return false;
	}
	
	str = f.department_id.value.trim();
	if( ! str ) {
		alert('학과코드를 입력하세요. ');
		f.department_id.focus();
		return false;
	}	
	
	str = f.member_id.value.trim();
	if( ! str ) {
		alert('담당교수를 입력하세요. ');
		f.member_id.focus();
		return false;
	}		
	
	f.action = '${pageContext.request.contextPath}/admin/lecture/${mode}';
	f.submit();
	
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
