package com.lms2.controller;

import java.io.IOException;
import java.util.List;

import com.lms2.dao.SidebarDAO;
import com.lms2.model.LectureDTO;
import com.lms2.mvc.annotation.Controller;
import com.lms2.mvc.annotation.RequestMapping;
import com.lms2.mvc.annotation.RequestMethod;
import com.lms2.mvc.view.ModelAndView;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class SidebarController {

    @RequestMapping(value = "/sidebar/lectureList", method = RequestMethod.GET)
    public ModelAndView LectureListSidebar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        SidebarDAO sidebarDao = new SidebarDAO();
        List<LectureDTO> lectureList = sidebarDao.getAllLectureSubjects();

        ModelAndView mav = new ModelAndView("/sidebar/lectureListSidebar");
        mav.addObject("lectureList", lectureList);

        return mav;
    }
}