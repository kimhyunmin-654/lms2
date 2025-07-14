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
	@RequestMapping(value = "/student/bbs/list", method = RequestMethod.GET)
	public ModelAndView list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 게시물 리스트
		ModelAndView mav = new ModelAndView("/student/bbs/list");
		return mav;
	}
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ModelAndView writeForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글 읽기
		ModelAndView mav = new ModelAndView("");
		return mav;
	}
	
	
}
