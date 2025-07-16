package com.lms2.controller;

import java.io.IOException;
import java.util.List;

import com.lms2.dao.LectureDAO;
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

    @RequestMapping(value = "/layout/prof_menusidebar", method = RequestMethod.GET)
    public ModelAndView lectureListSidebar(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession();
        SessionInfo info = (SessionInfo) session.getAttribute("member");

        // 로그인 체크
        if (info == null) {
            return new ModelAndView("redirect:/member/login");
        }

        // member_id 추출 및 String으로 변환 (VARCHAR2 타입 대비)
        String memberIdStr = String.valueOf(info.getMember_id());
        System.out.println("교수 member_id: " + memberIdStr);

        // 강의 목록 조회
        LectureDAO lectureDAO = new LectureDAO();
        List<LectureDTO> lectureList = lectureDAO.listsidebar(memberIdStr);

        // 로그 출력
        System.out.println("강의 개수: " + lectureList.size());
        for (LectureDTO dto : lectureList) {
            System.out.println("강의명: " + dto.getSubject());
        }

        // JSP에 데이터 전달
        ModelAndView mav = new ModelAndView("/layout/prof_menusidebar");
        mav.addObject("lectureList", lectureList);

        return mav;
    }
}