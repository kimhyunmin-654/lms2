<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>학생 수정</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/main2.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/mainPage.css">
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<body>
<header>
    <jsp:include page="/WEB-INF/views/layout/mainheader.jsp" />
</header>

<main>
    <jsp:include page="/WEB-INF/views/layout/student_mainsidebar.jsp" />
    
		<div class="container mt-5 ">
			<div style="margin-top: 100px;">
			<div class="main-wrapper">
            <div class="col-md-10 row">
				<table class="table" style="margin-bottom: 30px;">
					<tr>
						<td width="150px" align="left" class="page-title" style="border-bottom: 3px solid #CF1C31; border-top:none; ">학생 수정</td>
						<td align="left" style="border-bottom: 1px solid gray; border-top:none;">&nbsp;</td>
						<td align="right" style="border-bottom: 1px solid gray; border-top:none;">&nbsp;</td>
					</tr>
				</table>

	<form name="insertForm" method="post" enctype="multipart/form-data">

	
		<div class="d-flex align-items-center mb-4">
			<img src="${pageContext.request.contextPath}/dist/images/user.png" class="rounded-circle img-avatar" width="100" height="100">
			<div class="ms-3">
				<label for="selectFile" class="btn btn-primary me-2">
					사진 업로드
					<input type="file" name="selectFile" id="selectFile" hidden="" accept="image/png,image/jpeg">
				</label>
				<button type="button" class="btn btn-secondary">초기화</button>
				<div class="text-muted">이미지: JPG, PNG. 최대 800KB</div>
			</div>
		</div>

	
		<div class="row g-3">
<div class="col-md-6 wrap-member_id">
    <label class="form-label">아이디(학번)</label>
    <div class="input-group">
        <input type="text" name="member_id" id="member_id"  class="form-control" 
               value="${dto.member_id}" disabled >
    </div>
</div>
			<div class="col-md-6">
				<label class="form-label">이름</label>
				<input type="text" name="name" class="form-control" value="${dto.name}" disabled>
			</div>
			<div class="col-md-6">
				<label class="form-label">비밀번호</label>
				<input type="password" name="password" class="form-control">
			</div>
			<div class="col-md-6">
				<label class="form-label">비밀번호 확인</label>
				<input type="password" name="password2" class="form-control">
			</div>
			<div class="col-md-6">
				<label class="form-label">생년월일</label>
				<input type="date" name="birth" class="form-control" value="${dto.birth}">
			</div>
			<div class="col-md-6">
				<label class="form-label">전화번호</label>
				<input type="text" name="phone" class="form-control" value="${dto.phone}">
			</div>
		</div>

		
		<div class="row mt-3">
			<label class="col-md-2 form-label fw-medium">이메일</label>
			<div class="col-md-10">
				<div class="row g-2">
					<div class="col-md-3">
						<select name="selectEmail" class="form-select" onchange="changeEmail();">
							<option value="">선택</option>
							<option value="naver.com" ${dto.email2=="naver.com" ? "selected" : ""}>네이버</option>
							<option value="gmail.com" ${dto.email2=="gmail.com" ? "selected" : ""}>지메일</option>
							<option value="hanmail.net" ${dto.email2=="hanmail.net" ? "selected" : ""}>한메일</option>
							<option value="outlook.com" ${dto.email2=="outlook.com" ? "selected" : ""}>아웃룩</option>
							<option value="icloud.com" ${dto.email2=="icloud.com" ? "selected" : ""}>아이클라우드</option>
							<option value="direct">직접입력</option>
						</select>
					</div>
					<div class="col input-group">
						<input type="text" name="email1" class="form-control" value="${dto.email1}">
						<span class="input-group-text">@</span>
						<input type="text" name="email2" class="form-control" value="${dto.email2}" readonly>
					</div>
				</div>
			</div>
		</div>

		
		<div class="row mt-3">
			<label class="col-md-2 form-label">우편번호</label>
			<div class="col-md-10">
				<div class="row g-2">
					<div class="col-md-6">
						<input type="text" name="zip" id="zip" class="form-control" value="${dto.zip}" readonly>
					</div>
					<div class="col-md-6">
						<button type="button" class="btn btn-light" onclick="daumPostcode();">우편번호검색</button>
					</div>
				</div>
			</div>
			<label class="col-md-2 form-label mt-2">주소</label>
			<div class="col-md-10">
				<input type="text" name="addr1" id="addr1" class="form-control mb-2" value="${dto.addr1}" readonly placeholder="기본주소">
				<input type="text" name="addr2" id="addr2" class="form-control" value="${dto.addr2}" placeholder="상세주소">
			</div>
		</div>
	
		<div class="text-center mt-4">
			<button type="button" name="submitBtn" class="btn btn-primary" onclick="sendOk();">정보수정</button>
			<button type="button" class="btn btn-secondary" onclick="location.href='${pageContext.request.contextPath}/student/main/main'; ">취소</button>
			<input type="hidden" name="userIdValid" id="userIdValid" value="false">
    		<input type="hidden" name="avatar" value="${dto.avatar}">
    		<input type="hidden" name="member_id" value="${dto.member_id}">
		</div>

	</form>
</div>
</div>
</div>
</div>
</main>


<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<script src="${pageContext.request.contextPath}/dist/js/sidebar-toggle.js"></script>	
<script type="text/javascript">
window.addEventListener('DOMContentLoaded', ev => {
	let img = '${dto.avatar}';
	
	const avatarEL = document.querySelector('.img-avatar');
	const inputEL = document.querySelector('form[name=insertForm] input[name=selectFile]');
	const btnEL = document.querySelector('form[name=insertForm] .btn-secondary');
	
	let avatar;
	if( img ) {
		avatar = '${pageContext.request.contextPath}/uploads/member/' + img;
		avatarEL.src = avatar;
	}
	
	const maxSize = 800 * 1024;
	inputEL.addEventListener('change', ev => {
		let file = ev.target.files[0];
		if(! file) {
			if(img) {
				avatar = '${pageContext.request.contextPath}/uploads/member/' + img;
			} else {
				avatar = '${pageContext.request.contextPath}/dist/images/user.png';
			}
			avatarEL.src = avatar;
			
			return;
		}
		
		if(file.size > maxSize || ! file.type.match('image.*')) {
			inputEL.focus();
			return;
		}
		
		var reader = new FileReader();
		reader.onload = function(e) {
			avatarEL.src = e.target.result;
		}
		reader.readAsDataURL(file);	
		
	});
	
	btnEL.addEventListener('click', ev => {
		if( img ) {
			if(! confirm('등록된 이미지를 삭제하시겠습니까 ?')) {
				return false;
			}
			
			avatar = '${pageContext.request.contextPath}/uploads/member/' + img;
			
			// 등록 이미지 삭제
			let url = '${pageContext.request.contextPath}/student/student/deleteAvatar';
			$.post(url, {avatar: img}, function(data) {
				let state = data.state;
				
				if(state === 'true') {
					img = '';
					avatar = '${pageContext.request.contextPath}/dist/images/user.png';
					
					$('form input[name=avatar]').val('');
				}
				
				inputEL.value = '';
				avatarEL.src = avatar;
				
			}, 'json')
		} else {
			avatar = '${pageContext.request.contextPath}/dist/images/user.png';
			inputEL.value = '';
			avatarEL.src = avatar
		}
	});
	
});

function isValidDateString(dateString) {
	try {
		const date = new Date(dateString);
		const [year, month, day] = dateString.split("-").map(Number);
		
		return date instanceof Date && !isNaN(date) && date.getDate() === day;
	} catch(e) {
		return false;
	}
}

function sendOk() {
	const f = document.insertForm;
	let str, p;
	
	p =/^(?=.*[a-z])(?=.*[!@#$%^*+=-]|.*[0-9]).{5,10}$/i;
	str = f.password.value;
	if( ! (p.test(str) || str === "admin") ) { 
		alert('패스워드를 다시 입력 하세요. ');
		f.password.focus();
		return;
	}

	if( str !== f.password2.value ) {
        alert('패스워드가 일치하지 않습니다. ');
        f.password2.focus();
        return;
	}
	
    str = f.birth.value;
    if( ! isValidDateString(str) ) {
        alert('생년월일를 입력하세요. ');
        f.birth.focus();
        return;
    }
    
    str = f.email1.value.trim();
    if( ! str ) {
        alert('이메일을 입력하세요. ');
        f.email1.focus();
        return;
    }

    str = f.email2.value.trim();
    if( ! str ) {
        alert('이메일을 입력하세요. ');
        f.email2.focus();
        return;
    }
    
    p = /^(010)-?\d{4}-?\d{4}$/;    
    str = f.phone.value;
    if( ! p.test(str) ) {
        alert('전화번호를 입력하세요. ');
        f.phone.focus();
        return;
    }

	f.action = '${pageContext.request.contextPath}/student/student/update';
	f.submit();
}

function changeEmail() {
	const f = document.insertForm;
	
	let str = f.selectEmail.value;
	if( str !== 'direct') {
		f.email2.value = str;
		f.email2.readOnly = true;
		f.email1.focus();
	} else {
		f.email2.value = '';
		f.email2.readOnly = false;
		f.email1.focus();
	}
}


</script>

<script src="http://dmaps.daum.net/map_js_init/postcode.v2.js"></script>
<script>
    function daumPostcode() {
        new daum.Postcode({
            oncomplete: function(data) {
                // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

                // 각 주소의 노출 규칙에 따라 주소를 조합한다.
                // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
                var fullAddr = ''; // 최종 주소 변수
                var extraAddr = ''; // 조합형 주소 변수

                // 사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
                if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                    fullAddr = data.roadAddress;

                } else { // 사용자가 지번 주소를 선택했을 경우(J)
                    fullAddr = data.jibunAddress;
                }

                // 사용자가 선택한 주소가 도로명 타입일때 조합한다.
                if(data.userSelectedType === 'R'){
                    //법정동명이 있을 경우 추가한다.
                    if(data.bname !== ''){
                        extraAddr += data.bname;
                    }
                    // 건물명이 있을 경우 추가한다.
                    if(data.buildingName !== ''){
                        extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                    }
                    // 조합형주소의 유무에 따라 양쪽에 괄호를 추가하여 최종 주소를 만든다.
                    fullAddr += (extraAddr !== '' ? ' ('+ extraAddr +')' : '');
                }

                // 우편번호와 주소 정보를 해당 필드에 넣는다.
                document.getElementById('zip').value = data.zonecode; //5자리 새우편번호 사용
                document.getElementById('addr1').value = fullAddr;

                // 커서를 상세주소 필드로 이동한다.
                document.getElementById('addr2').focus();
            }
        }).open();
    }
</script>
	
	
</body>
</html>