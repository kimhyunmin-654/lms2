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
public class RatingController {
	@RequestMapping(value = "/professor/rating/list", method = RequestMethod.GET)
	public ModelAndView list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		RatingDAO dao = new RatingDAO();
		LectureDAO lectureDao = new LectureDAO();
		ModelAndView mav = new ModelAndView("professor/rating/list");
		
		try {
			HttpSession session = req.getSession(false);
			SessionInfo info = (SessionInfo) session.getAttribute("member");
			if (info != null) {
				String memberId = String.valueOf(info.getMember_id());
				List<LectureDTO> lectures = lectureDao.listsidebar(memberId);
				mav.addObject("lectureList", lectures);
			}
			
			String page = req.getParameter("page");
			int current_page = 1;
			if (page != null) {
				current_page = Integer.parseInt(page);
			}

			int size = 10;
			int offset = (current_page - 1) * size;

			String lecture_code = req.getParameter("lecture_code");
			if (lecture_code != null && !lecture_code.isEmpty()) {
				mav.addObject("lecture_code", lecture_code);

				// ⭐ 성적 리스트 가져오기
				List<RatingDTO> list = dao.listrating(offset, size, lecture_code);
				mav.addObject("list", list);
				mav.addObject("page", current_page);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return mav;
	}
	
	@RequestMapping(value = "/professor/rating/write", method = RequestMethod.GET)
	public ModelAndView wirteForm(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("professor/rating/write");
		RatingDAO dao = new RatingDAO();
		LectureDAO lectureDao = new LectureDAO();

		try {
			HttpSession session = req.getSession(false);
			SessionInfo info = (SessionInfo) session.getAttribute("member");
			if (info != null) {
				String memberId = String.valueOf(info.getMember_id());
				List<LectureDTO> lectures = lectureDao.listsidebar(memberId);
				mav.addObject("lectureList", lectures);
			}

			int offset = 0;
			int size = 10;

			String lecture_code = req.getParameter("lecture_code");

			List<RatingDTO> list = dao.listrating(offset, size, lecture_code);
			mav.addObject("list", list);
			mav.addObject("mode", "write");
			mav.addObject("lecture_code", lecture_code);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}
	
	@RequestMapping(value = "/professor/rating/write", method = RequestMethod.POST)
	public ModelAndView writeSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    String lecture_code = req.getParameter("lecture_code");
	    RatingDAO dao = new RatingDAO();

	    try {
	        String[] courseIds = req.getParameterValues("course_id");
	        String[] memberIds = req.getParameterValues("member_id");

	        String[] middleRatings = req.getParameterValues("middletest_rating");
	        String[] finalRatings = req.getParameterValues("finaltest_rating");
	        String[] attendanceRatings = req.getParameterValues("attendance_rating");
	        String[] homeworkRatings = req.getParameterValues("homework_rating");
	        String[] totalRatings = req.getParameterValues("total_rating");
	        String[] grades = req.getParameterValues("rating");

	        if (courseIds != null && memberIds != null) {
	            for (int i = 0; i < courseIds.length; i++) {
	                int courseId = Integer.parseInt(courseIds[i]);
	                String memberId = memberIds[i];

	                // 값이 비어있을 경우 넘어가기
	                if (memberId == null || memberId.trim().isEmpty()) continue;

	                RatingDTO dto = new RatingDTO();
	                dto.setCourse_id(courseId);
	                dto.setMember_id(memberId);
	                dto.setMiddletest_rating(parseIntSafe(middleRatings[i]));
	                dto.setFinaltest_rating(parseIntSafe(finalRatings[i]));
	                dto.setAttendance_rating(parseIntSafe(attendanceRatings[i]));
	                dto.setHomework_rating(parseIntSafe(homeworkRatings[i]));
	                dto.setTotal_rating(parseIntSafe(totalRatings[i]));
	                dto.setRating(grades[i]);

	                dao.insertRating(dto);
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return new ModelAndView("redirect:/professor/rating/list?lecture_code=" + lecture_code);
	}

	// 정수 파싱 오류 방지용 메서드
	private int parseIntSafe(String str) {
	    try {
	        return Integer.parseInt(str); // 문자열을 정수로 변환
	    } catch (Exception e) {
	        return 0; // 변환 실패 시 기본값 0 반환
	    }
	}

}