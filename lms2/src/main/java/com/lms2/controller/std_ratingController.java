package com.lms2.controller;

import java.io.IOException;
import java.util.List;

import com.lms2.dao.LectureDAO;
import com.lms2.dao.RatingDAO;
import com.lms2.model.LectureDTO;
import com.lms2.model.RatingDTO;
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
public class std_ratingController {
    @RequestMapping(value = "/student/study/rating", method = RequestMethod.GET)
    public ModelAndView list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ModelAndView mav = new ModelAndView("student/study/rating");
        RatingDAO dao = new RatingDAO();
        LectureDAO lectureDao = new LectureDAO();

        try {
            HttpSession session = req.getSession(false);
            SessionInfo info = (SessionInfo) session.getAttribute("member");

            String memberId = String.valueOf(info.getMember_id());

            String page = req.getParameter("page");
            int current_page = 1;
            if (page != null) {
                current_page = Integer.parseInt(page);
            }

            int size = 10;
            int offset = (current_page - 1) * size;

            List<LectureDTO> lectures = lectureDao.std_listsidebar(memberId);
			mav.addObject("lectureList", lectures);
            List<RatingDTO> list = dao.std_listrating(offset, size, memberId);
            mav.addObject("list", list);
            mav.addObject("page", current_page);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return mav;
    }
}

