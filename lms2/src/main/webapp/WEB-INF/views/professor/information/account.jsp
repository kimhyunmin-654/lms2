<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<c:set var="mode" value="update" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>관리자 정보수정</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/main2.css">
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
</head>
<body>
<header>
    <jsp:include page="/WEB-INF/views/layout/mainheader.jsp" />
</header>

<main>
    <jsp:include page="/WEB-INF/views/layout/admin_mainsidebar.jsp" />
    
<div class="container mt-5" style="margin-left:240px;">
	<h3 class="mb-4"><i class="bi bi-person-circle"></i> 관리자 정보수정</h3>

	<form name="insertForm" method="post" enctype="multipart/form-data">

		<div class="d-flex align-items-center mb-4">
			<img src="${pageContext.request.contextPath}/uploads/member/${dto.avatar}?v=${now}" alt="프로필" class="rounded-circle profile-img">
			<div class="ms-3">
				<label for="selectFile" class="btn btn-primary me-2">
					사진 업로드
					<input type="file" name="selectFile" id="selectFile" hidden accept="image/png,image/jpeg">
				</label>
				<button type="button" class="btn btn-secondary">초기화</button>
				<div class="text-muted">이미지: JPG, PNG. 최대 800KB</div>
			</div>
		</div>

		<div class="row g-3">
			<div class="col-md-6 wrap-member_id">
				<label class="form-label">아이디(사번)</label>
				<div class="input-group">
					<input type="text" name="member_id" id="member_id" class="form-control" value="${dto.member_id}" readonly autofocus>
				</div>
			</div>
			<div class="col-md-6">
				<label class="form-label">이름</label>
				<input type="text" name="name" class="form-control" value="${dto.name}">
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
			<button type="button" class="btn btn-secondary" onclick="location.href='${pageContext.request.contextPath}/professor/main/main'; ">취소</button>
			<input type="hidden" name="avatar" value="${dto.avatar}">
		</div>

	</form>
</div>
</main>

<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<script src="${pageContext.request.contextPath}/dist/js/sidebar-toggle.js"></script>	


<script type="text/javascript">
let img = '${dto.avatar}';
const avatarEL = document.querySelector('.profile-img');
const inputEL = document.querySelector('form[name=insertForm] input[name=selectFile]');
const btnEL = document.querySelector('form[name=insertForm] .btn-secondary');

if (img) {
	avatarEL.src = '${pageContext.request.contextPath}/uploads/member/' + img;
}

const maxSize = 800 * 1024;

inputEL.addEventListener('change', ev => {
	let file = ev.target.files[0];
	if (!file) {
		avatarEL.src = img ? '${pageContext.request.contextPath}/uploads/member/' + img : '${pageContext.request.contextPath}/dist/images/user.png';
		return;
	}

	if (file.size > maxSize || !file.type.match('image.*')) {
		alert('이미지는 PNG 또는 JPG만 허용되며 800KB 이하만 가능합니다.');
		inputEL.focus();
		return;
	}

	let reader = new FileReader();
	reader.onload = function(e) {
		avatarEL.src = e.target.result;
	}
	reader.readAsDataURL(file);
});

btnEL.addEventListener('click', ev => {
	if (img) {
		if (!confirm('등록된 이미지를 삭제하시겠습니까 ?')) return false;

		let url = '${pageContext.request.contextPath}/professor/information/deleteAvatar';
		$.post(url, { avatar: img }, function(data) {
			if (data.state === 'true') {
				img = '';
				avatarEL.src = '${pageContext.request.contextPath}/dist/images/user.png';
				$('form input[name=avatar]').val('');
			}
			inputEL.value = '';
		}, 'json');
	} else {
		avatarEL.src = '${pageContext.request.contextPath}/dist/images/user.png';
		inputEL.value = '';
	}
});

function isValidDateString(dateString) {
	try {
		const date = new Date(dateString);
		const [year, month, day] = dateString.split("-").map(Number);
		return date instanceof Date && !isNaN(date) && date.getDate() === day;
	} catch (e) {
		return false;
	}
}

function sendOk() {
	const f = document.insertForm;
	let str, p;

	p = /^[가-힣]{2,5}$/;
	str = f.name.value;
	if (!p.test(str)) {
		alert('이름을 다시 입력하세요.');
		f.name.focus();
		return;
	}

	p = /^(?=.*[a-z])(?=.*[!@#$%^*+=-]|.*[0-9]).{5,10}$/i;
	str = f.password.value;
	if (!p.test(str)) {
		alert('패스워드를 다시 입력 하세요.');
		f.password.focus();
		return;
	}

	if (str !== f.password2.value) {
		alert('패스워드가 일치하지 않습니다.');
		f.password2.focus();
		return;
	}

	str = f.birth.value;
	if (!isValidDateString(str)) {
		alert('생년월일을 입력하세요.');
		f.birth.focus();
		return;
	}

	str = f.email1.value.trim();
	if (!str) {
		alert('이메일을 입력하세요.');
		f.email1.focus();
		return;
	}

	str = f.email2.value.trim();
	if (!str) {
		alert('이메일을 입력하세요.');
		f.email2.focus();
		return;
	}

	p = /^010-?\d{4}-?\d{4}$/;
	str = f.phone.value;
	if (!p.test(str)) {
		alert('전화번호를 입력하세요.');
		f.phone.focus();
		return;
	}

	console.log("Form submit triggered");
	console.log("avatar(hidden):", f.avatar.value);
	console.log("file:", f.selectFile.files[0]);

	f.action = '${pageContext.request.contextPath}/professor/information/update';
	f.submit();
}

function changeEmail() {
	const f = document.insertForm;
	let str = f.selectEmail.value;
	if (str !== 'direct') {
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
			let fullAddr = data.userSelectedType === 'R' ? data.roadAddress : data.jibunAddress;
			let extraAddr = '';
			if (data.userSelectedType === 'R') {
				if (data.bname !== '') extraAddr += data.bname;
				if (data.buildingName !== '') extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
				fullAddr += (extraAddr !== '' ? ' (' + extraAddr + ')' : '');
			}
			document.getElementById('zip').value = data.zonecode;
			document.getElementById('addr1').value = fullAddr;
			document.getElementById('addr2').focus();
		}
	}).open();
}
</script>

</body>
</html>