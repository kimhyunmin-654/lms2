package com.lms2.controller;

import java.util.List;

import com.lms2.dao.LectureDAO;

import com.lms2.model.LectureDTO;
import com.lms2.model.SessionInfo;
import com.lms2.mvc.annotation.Controller;
import com.lms2.mvc.annotation.RequestMapping;
import com.lms2.mvc.annotation.RequestMethod;
import com.lms2.mvc.view.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class SidebarController {

	@RequestMapping(value = "/layout/prof_menusidebar", method = RequestMethod.GET)
	public ModelAndView profMenuSidebar(HttpServletRequest req) {
	    HttpSession session = req.getSession();
	    SessionInfo info = (SessionInfo) session.getAttribute("member");

	    LectureDAO dao = new LectureDAO(); 
	    ModelAndView mav = new ModelAndView("layout/prof_menusidebar");

	    try {
	        if (info != null) {
	            String memberId = String.valueOf(info.getMember_id());
	            List<LectureDTO> lectureList = dao.listsidebar(memberId); 
	            mav.addObject("lectureList", lectureList);
	        } else {
	            System.out.println("세션에 사용자 정보 없음");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return mav;
	}
}