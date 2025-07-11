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

@WebFilter("/*")
public class LoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession();

        String uri = req.getRequestURI();
        String cp = req.getContextPath();

        SessionInfo info = (SessionInfo) session.getAttribute("member");

        System.out.println("[LoginFilter] URI: " + uri);
        System.out.println("[LoginFilter] SessionInfo: " + (info != null ? info.getMember_id() + ", role=" + info.getRole() : "null"));

        // 로그인 필요 없는 경로
        if (info == null && !isExcludeUri(req)) {
            if (isAjaxRequest(req)) {
                resp.sendError(403);
            } else {
                if (uri.startsWith(cp)) {
                    uri = uri.substring(cp.length());
                }
                String redirectURI = uri;
                String qs = req.getQueryString();
                if (qs != null) {
                    redirectURI += "?" + qs;
                }
                session.setAttribute("preLoginURI", redirectURI);
                resp.sendRedirect(cp + "/home/main");
            }
            return;
        }

        // 관리자 페이지 접근 제한
        if (info != null && uri.contains("/admin/") && info.getRole() != 99) {
            resp.sendRedirect(cp + "/home/main_base");
            return;
        }

        chain.doFilter(request, response);
    }


    @Override
    public void destroy() {}

    private boolean isAjaxRequest(HttpServletRequest req) {
        String h = req.getHeader("AJAX");
        return h != null && h.equals("true");
    }

    private boolean isExcludeUri(HttpServletRequest req) {
        String uri = req.getRequestURI();
        String cp = req.getContextPath();
        uri = uri.substring(cp.length());

        String[] uris = {
            "/index.jsp", "/main",
            "/home/main", "/home/logout",
            "/home/account", "/home/userIdCheck", "/home/complete",
            "/notice/list",
            "/uploads/photo/", "/dist/"
        };

        if (uri.length() <= 1) return true;

        for (String s : uris) {
            if (s.endsWith("/")) {
                if (uri.startsWith(s)) return true;
            } else if (uri.equals(s)) {
                return true;
            }
        }

        return false;
    }
}