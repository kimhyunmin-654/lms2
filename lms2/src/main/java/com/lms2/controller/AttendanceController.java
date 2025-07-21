package com.lms2.controller;

import java.io.IOException;
import java.util.List;

import com.lms2.dao.AttendanceDAO;
import com.lms2.dao.LectureDAO;
import com.lms2.dao.StudentDAO;
import com.lms2.model.Attendance_recordDTO;
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
public class AttendanceController {

	@RequestMapping(value = "/professor/attendance/list", method = RequestMethod.GET)
	public ModelAndView list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		AttendanceDAO dao = new AttendanceDAO();
		LectureDAO lectureDao = new LectureDAO();
		ModelAndView mav = new ModelAndView("professor/attendance/list");

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

			String lecture_code = req.getParameter("lecture_code");
			if (lecture_code != null) {
				mav.addObject("lecture_code", lecture_code);
			}
			
			String weekStr = req.getParameter("week");
			int selectedWeek = 1;
			if(weekStr != null) {
				selectedWeek = Integer.parseInt(weekStr);
			}
			mav.addObject("selectedWeek", selectedWeek);

			List<Attendance_recordDTO> list = dao.listAttendanceByWeek(lecture_code, selectedWeek);

			mav.addObject("list", list);
			mav.addObject("page", current_page);
			mav.addObject("lecture_code", lecture_code);


		} catch (Exception e) {
			e.printStackTrace();
		}

		return mav;
	}

	@RequestMapping(value = "/professor/attendance/write", method = RequestMethod.GET)
	public ModelAndView wirteForm(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("professor/attendance/write");
		AttendanceDAO dao = new AttendanceDAO();
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
			
			String weekStr = req.getParameter("week");
			int selectedWeek = 1;
			if(weekStr != null) {
				selectedWeek = Integer.parseInt(weekStr);
			}
			mav.addObject("selectedWeek", selectedWeek);

			List<Attendance_recordDTO> list = dao.listapplication(offset, size, lecture_code);
			mav.addObject("list", list);
			mav.addObject("mode", "write");
			mav.addObject("lecture_code", lecture_code);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}

	@RequestMapping(value = "/professor/attendance/write", method = RequestMethod.POST)
	public ModelAndView writeSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		AttendanceDAO dao = new AttendanceDAO();
		String lecture_code = req.getParameter("lecture_code");
		String weekStr = req.getParameter("week");
		int week = 1;
		if(weekStr != null) {
			week = Integer.parseInt(weekStr);
		}
		
		try {
			String[] courseIds = req.getParameterValues("course_id");

			if (courseIds != null) {
				for (String courseIdStr : courseIds) {
					int courseId = Integer.parseInt(courseIdStr);

					// 개별 학생의 출석 상태 파라미터는 "status_수강번호"
					String paramName = "status_" + courseId;
					String statusStr = req.getParameter(paramName);

					if (statusStr == null)
						continue; // 선택 안 한 경우는 건너뛴다

					int status = Integer.parseInt(statusStr);

					Attendance_recordDTO dto = new Attendance_recordDTO();
					dto.setCourse_id(courseId);
					dto.setStatus(status);
					dto.setWeek(week);

					dao.insertAttendance(dto);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/professor/attendance/list?lecture_code=" + lecture_code + "&week=" + week);
	}
	
	@RequestMapping(value = "/student/attendance/list", method = RequestMethod.GET)
	public ModelAndView std_list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		AttendanceDAO dao = new AttendanceDAO();
		LectureDAO lectureDao = new LectureDAO();
		ModelAndView mav = new ModelAndView("student/attendance/list");

		try {
			HttpSession session = req.getSession(false);
			SessionInfo info = (SessionInfo) session.getAttribute("member");
			
			String memberId = String.valueOf(info.getMember_id());
			
			List<LectureDTO> lectures = lectureDao.std_listsidebar(memberId);
            mav.addObject("lectureList", lectures);
			
			String lecture_code = req.getParameter("lecture_code");
            if (lecture_code != null && !lecture_code.isEmpty()) {
                List<Attendance_recordDTO> list = dao.listAttendanceByLectureAndStudent(lecture_code, memberId);
                mav.addObject("lecture_code", lecture_code);
                mav.addObject("list", list);
            }
			

		} catch (Exception e) {
			e.printStackTrace();
		}

		return mav;
	}

	// 출석 (학생용)
	@RequestMapping(value = "/student/study/attendance", method = RequestMethod.GET)
	public ModelAndView studentAttendance(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 출석관리
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		StudentDAO dao = new StudentDAO();
		AttendanceDAO aDao = new AttendanceDAO();
		LectureDAO lectureDao = new LectureDAO();
		ModelAndView mav = new ModelAndView("student/study/attendance");

		try {
			String member_id = info.getMember_id();
			List<LectureDTO> lectures = lectureDao.std_listsidebar(member_id);
			mav.addObject("lectureList", lectures);

			String lecture_code = req.getParameter("lecture_code");

			List<Attendance_recordDTO> list;
			if (lecture_code != null && !lecture_code.isEmpty()) {
				list = aDao.listAttendanceByLectureAndStudent(lecture_code, member_id);
			} else {
				list = dao.listAttendance(member_id);
			}

			mav.addObject("list", list);

			int present = aDao.dataCountAll(member_id, 1); // 출석
			int absent = aDao.dataCountAll(member_id, 0); // 결석
			int late = aDao.dataCountAll(member_id, 2); // 지각
			
			mav.addObject("present", present);
			mav.addObject("absent", absent);
			mav.addObject("late", late);
			mav.addObject("lecture_code", lecture_code);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return mav;
	}

}