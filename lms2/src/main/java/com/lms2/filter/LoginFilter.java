package com.lms2.filter;

import java.io.IOException;

import com.lms2.model.SessionInfo;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/*
 - Filter
   : 요청(request)과 응답(response)를 가로채어 전후처리를 할수 있는 
     재사용가능한 컴포넌트
   : 주로 공통기능을 처리하기 위해 사용
   : 요청(request) 전 - 인증, 권한검사, 인코딩, 로깅 등
   : 응답(response) 전 - 응답압축, 응답내용변경, 보안헤더추가등
 
 */
@WebFilter("/*")
public class LoginFilter implements Filter {
	
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// 필터클래스가 객체를 생성할때 한번 실행
	}
	
	
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 클라이언트가 요청이 있을때마다 실행
		
		// request 필터
    	
    	// 로그인 체크
    	HttpServletRequest req = (HttpServletRequest)request;
    	HttpServletResponse resp = (HttpServletResponse)response;
    	HttpSession session = req.getSession();
    	
    	String uri = req.getRequestURI();
    	String cp = req.getContextPath();
    	
    	// 세션에 저장된 로그인 정보
    	SessionInfo info = (SessionInfo)session.getAttribute("member");
    	
        // 로그인이 안 되어 있고 제외 URI가 아닐 경우 로그인 페이지로 이동
        if (info == null && !isExcludeUri(req)) {
            if (isAjaxRequest(req)) {
                resp.sendError(403);
            } else {
                // 로그인 전 요청 주소 저장
                if (uri.startsWith(cp)) {
                    uri = uri.substring(cp.length());
                }
                uri = "redirect:" + uri;
                String qs = req.getQueryString();
                if (qs != null) {
                    uri += "?" + qs;
                }
                session.setAttribute("preLoginURI", uri);

                // 로그인 페이지로 이동
                resp.sendRedirect(cp + "/home/main");
            }
            return;
        }

        // 관리자 페이지 접근 제한: role != 99 → 메인화면으로 리다이렉트
        if (info != null && uri.contains("/admin/")) {
            if (info.getRole() != 99) {
                resp.sendRedirect(cp + "/home/main_base");
                return;
            }
        }

        // 다음 필터 또는 서블릿 실행
        chain.doFilter(request, response);
    }
	@Override
	public void destroy() {
		// 필터 클래스의 객체가 파괴되기 직전 한번 실행
	}
	
	// 요청이 AJAX인지를 확인하는 메소드
	private boolean isAjaxRequest(HttpServletRequest req) {
		// AJAX 요청인 경우 헤더에 AJAX 라는 이름에 true를 실어서 보내도록 구현
		String h = req.getHeader("AJAX");
		
		return h != null && h.equals("true");
	}
	
	// 로그인 체크가 필요하지 않은지의 여부 판단
	private boolean isExcludeUri(HttpServletRequest req) {
		String uri = req.getRequestURI();
		String cp = req.getContextPath();
		uri = uri.substring(cp.length());
		
		// 로그인이 검사가 필요없는 uri
        String[] uris = {
              "/index.jsp", "/main",
              "/home/main", "/member/logout",
              "/member/account", "/member/userIdCheck", "/member/complete",
              "/notice/list",
              "/uploads/photo/**",
              "/dist/**"
        };
		
		if(uri.length() <= 1) {
			return true;
		}	
		
		for(String s : uris) {
			if(s.lastIndexOf("**") != -1) {
				s= s.substring(0, s.lastIndexOf("**"));
				if(uri.indexOf(s) == 0) {
					return true;
				}
			} else if(uri.equals(s)) {
				return true;
			}
		}
		
		return false;
	}

	
}
	

