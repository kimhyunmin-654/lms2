<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>내 정보 관리</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/main2.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/pwd.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/pwd.css">
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<jsp:include page="/WEB-INF/views/layout/headerResources.jsp"/>
</head>
<body>

<header>
    <jsp:include page="/WEB-INF/views/layout/mainheader.jsp" />
</header>

<main>
    <jsp:include page="/WEB-INF/views/layout/prof_mainsidebar.jsp" />

	<div class="container mt-5">
	<div class="pwd-box">
		<div class="col-md-6 my-3 p-3">

                <div class="border mt-5 p-4">
                    <form name="pwdForm" method="post" class="row g-3">
                        <h3 class="text-center fw-bold">패스워드 재확인</h3>
                        
		                <div class="d-grid">
							<p class="form-control-plaintext text-center">패스워드를 다시 한 번 입력해주세요.</p>
		                </div>
                        
                        <div class="d-grid">
                            <input type="text" name="member_id" class="form-control form-control-lg" placeholder="아이디"
                            		value="${sessionScope.member.member_id}" 
                            		readonly>
                        </div>
                        <div class="d-grid">
                            <input type="password" name="password" class="form-control form-control-lg" autocomplete="off" placeholder="패스워드">
                        </div>
                        <div class="d-grid">
                            <button type="button" class="btn btn-lg btn-primary" onclick="sendOk();">확인 <i class="bi bi-check2"></i> </button>
                            <input type="hidden" name="mode" value="${mode}">
                        </div>
                    </form>
                    <c:if test="${not empty message}">
                        <div class="alert alert-danger text-center mt-3" role="alert">
                            ${message}
                        </div>
                    </c:if>
                </div>


                </div>
            </div>
        </div>
</main>

<script type="text/javascript">
function sendOk() {
	const f = document.pwdForm;

	let str = f.password.value;
	if(!str) {
		alert("패스워드를 입력하세요. ");
		f.password.focus();
		return;
	}

	f.action = "${pageContext.request.contextPath}/professor/information/pwd";
	f.submit();
}
</script>



</body>
</html>