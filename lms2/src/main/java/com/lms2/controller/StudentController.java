package com.lms2.controller;

import java.io.IOException;

import com.lms2.mvc.annotation.Controller;
import com.lms2.mvc.annotation.RequestMapping;
import com.lms2.mvc.annotation.RequestMethod;
import com.lms2.mvc.view.ModelAndView;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class StudentController {
	
	@RequestMapping(value = "/member/login", method = RequestMethod.GET)
	public String loginForm(HttpServletRequest req, HttpServletRequest resp) throws ServletException, IOException {
		
		return "member/login";
	}
	
	@RequestMapping(value = "/member/login", method = RequestMethod.POST)
	public ModelAndView loginSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 로그인
		
		
		
		return null;
	}
	
	@RequestMapping(value = "/member/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 로그아웃
		
		return "redirect:/";
	}
	
	
	
	
}
