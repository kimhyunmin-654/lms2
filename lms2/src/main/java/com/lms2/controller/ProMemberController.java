package com.lms2.controller;

import java.io.IOException;
import java.util.List;

import com.lms2.dao.LectureDAO;
import com.lms2.dao.pro_memberDAO;
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
public class ProMemberController {
	
	@RequestMapping(value = "/professor/member/list", method = RequestMethod.GET)
	public ModelAndView list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    HttpSession session = req.getSession();
	    SessionInfo info = (SessionInfo) session.getAttribute("member");

	    ModelAndView mav = new ModelAndView("professor/member/list");
	    int page = 1;
        int size = 10;

	    pro_memberDAO dao = new pro_memberDAO();
	    LectureDAO lectureDao = new LectureDAO();
	    String lectureCode = req.getParameter("lectureCode");

	    try {
            page = Integer.parseInt(req.getParameter("page"));
        } catch (Exception e) {
            // 기본값 유지
        }

        int offset = (page - 1) * size;

        List<MemberDTO> list = dao.listmember(lectureCode, offset, size);

        mav.addObject("list", list);
        mav.addObject("lectureCode", lectureCode);
        mav.addObject("page", page);
        mav.addObject("size", size);

        return mav;
    }
}
