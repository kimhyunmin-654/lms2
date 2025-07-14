package com.lms2.controller;

import java.io.IOException;
import java.rmi.ServerException;

import com.lms2.dao.MemberDAO;
import com.lms2.model.MemberDTO;
import com.lms2.model.SessionInfo;
import com.lms2.mvc.annotation.Controller;
import com.lms2.mvc.annotation.RequestMapping;
import com.lms2.mvc.annotation.RequestMethod;
import com.lms2.mvc.view.ModelAndView;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class MemberController {
	@RequestMapping(value = "/home/main", method = RequestMethod.GET)
	public String loginForm(HttpServletRequest req, HttpServletResponse resp) {
	    return "home/main"; // 로그인 JSP
	}
	

	@RequestMapping(value = "/home/main", method = RequestMethod.POST)
	public ModelAndView loginSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServerException, IOException {
		// 로그인 처리
		MemberDAO dao = new MemberDAO();
		HttpSession session = req.getSession();
		
		String userId = req.getParameter("userId");
		String password = req.getParameter("password");
		
		
		
		MemberDTO dto = dao.loginMember(userId, password);
		if(dto != null) {
			
			// 세션 유지시간 1시간
			session.setMaxInactiveInterval(60 * 60);
			
			SessionInfo info = new SessionInfo();
			info.setMember_id(dto.getMember_id());
			info.setName(dto.getName());
			info.setRole(dto.getRole());
			info.setAvatar(dto.getAvatar());
			
			session.setAttribute("member", info);
			
			String preLoginURI = (String) session.getAttribute("preLoginURI");
			session.removeAttribute("preLoginURI");
			if (preLoginURI != null) {
			    if (preLoginURI.startsWith("redirect:")) {
			        return new ModelAndView(preLoginURI);
			    } else {
			        return new ModelAndView("redirect:" + preLoginURI);
			    }
			}
			

            // 역할에 따른 리다이렉트 처리
            int role = dto.getRole();

            if (role == 99) {
                // 관리자
                return new ModelAndView("redirect:/admin/home/frame");
            } else if (role == 51) {
                // 교수
                return new ModelAndView("redirect:/home/main_base");
            } else if (role == 1) {
                // 학생
                return new ModelAndView("redirect:/home/main_base");
            } else {
                // 알 수 없는 역할
                ModelAndView mav = new ModelAndView("home/main");
                mav.addObject("message", "알 수 없는 사용자 권한입니다.");
                return mav;
            }
        }

        // 로그인 실패
        ModelAndView mav = new ModelAndView("home/main");
        mav.addObject("message", "아이디 또는 비밀번호가 일치하지 않습니다.");
        return mav;
    }
	
	@RequestMapping(value = "/admin/home/frame", method = RequestMethod.GET)
	public String adminMain(HttpServletRequest req, HttpServletResponse resp) {
	    return "admin/home/frame"; // JSP 뷰 경로
	}
	
	@RequestMapping(value = "/home/main_base", method = RequestMethod.GET)
	public String studentMain(HttpServletRequest req, HttpServletResponse resp) {
	    return "home/main_base"; // JSP 뷰 경로
	}
	

	
	@RequestMapping(value = "/home/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 로그아웃
		
		HttpSession session = req.getSession();
		
		// member라는 이름으로 세션에 저장된 속성 삭제
		session.removeAttribute("member");
		
		// 세션에 저장된 모든 속성을 지우고, 세션을 초기화
		session.invalidate();
		
		// 메인화면으로 리다이렉트
		return "redirect:/home/main";
	}
	
	
}
