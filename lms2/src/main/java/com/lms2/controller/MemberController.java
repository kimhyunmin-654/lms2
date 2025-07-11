package com.lms2.controller;

import java.io.IOException;
import java.rmi.ServerException;

import javax.mail.Session;

import com.lms2.dao.MemberDAO;
import com.lms2.model.MemberDTO;
import com.lms2.model.SessionInfo;
import com.lms2.mvc.annotation.Controller;
import com.lms2.mvc.annotation.RequestMapping;
import com.lms2.mvc.annotation.RequestMethod;
import com.lms2.mvc.view.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class MemberController {
	@RequestMapping(value="/member/login", method = RequestMethod.GET)
	public String loginForm(HttpServletRequest req, HttpServletResponse resp) throws ServerException, IOException {
		// 로그인 폼
		
		return "member/login";
	}
	@RequestMapping(value = "member/login", method = RequestMethod.POST)
	public ModelAndView loginSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServerException, IOException {
		// 로그인 처리
		MemberDAO dao = new MemberDAO();
		HttpSession session = req.getSession();
		
		String userId = req.getParameter("member_id");
		String userPwd = req.getParameter("password");
		
		MemberDTO dto = dao.loginMember(userId, userPwd);
		if(dto != null) {
			
			// 세션 유지시간 1시간
			session.setMaxInactiveInterval(60 * 60);
			
			SessionInfo info = new SessionInfo();
			info.setMember_id(dto.getMember_id());
			info.setName(dto.getName());
			info.setRole(dto.getRole());
			info.setAvatar(dto.getAvatar());
			
			session.setAttribute("member", info);
			
			String preLoginURI = (String)session.getAttribute("preLoginURI");
			session.removeAttribute("preLoginURI");
			if(preLoginURI != null) {
				return new ModelAndView(preLoginURI);
			}
			
			// 메인화면으로 리다이렉트
			return new ModelAndView("redirect:/");
		}
		// 로그인 실패한 경우 로그인 페이지
		ModelAndView mav = new ModelAndView("member/login");
				
		return mav;
	
	}
	
	
}
