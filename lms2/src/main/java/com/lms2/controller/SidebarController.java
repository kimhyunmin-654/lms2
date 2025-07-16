package com.lms2.controller;

import java.io.IOException;
import java.util.List;

import com.lms2.dao.LectureDAO;
import com.lms2.dao.SidebarDAO;
import com.lms2.model.LectureDTO;
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
public class SidebarController {

    @RequestMapping(value = "/sidebar/lectureList", method = RequestMethod.GET)
    public ModelAndView LectureListSidebar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    	HttpSession session = req.getSession();
        SessionInfo info = (SessionInfo) session.getAttribute("member");

        if (info == null) {
            return new ModelAndView("redirect:/member/login");
        }
    	
        LectureDAO LectureDAO = new LectureDAO();
        List<LectureDTO> lectureList = LectureDAO.listsidebar(info.getMember_id());

        ModelAndView mav = new ModelAndView("/sidebar/lectureListSidebar");
        mav.addObject("lectureList", lectureList);

        return mav;
    }
}