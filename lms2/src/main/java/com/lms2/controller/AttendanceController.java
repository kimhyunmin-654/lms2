package com.lms2.controller;

import java.io.IOException;
import java.util.List;

import com.lms2.dao.AttendanceDAO;
import com.lms2.model.Attendance_recordDTO;
import com.lms2.mvc.annotation.Controller;
import com.lms2.mvc.annotation.RequestMapping;
import com.lms2.mvc.annotation.RequestMethod;
import com.lms2.mvc.view.ModelAndView;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class AttendanceController {

    @RequestMapping(value = "/professor/attendance/list", method = RequestMethod.GET)
    public ModelAndView list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AttendanceDAO dao = new AttendanceDAO();
        ModelAndView mav = new ModelAndView("professor/attendance/list");

        try {
            String page = req.getParameter("page");
            int current_page = 1;
            if (page != null) {
                current_page = Integer.parseInt(page);
            }

            int pageSize = 10; // 한 페이지에 보여줄 항목 수
            int offset = (current_page - 1) * pageSize;

            List<Attendance_recordDTO> list = dao.listapplication(offset, pageSize);

            mav.addObject("list", list);
            mav.addObject("page", current_page);  // 숫자 값으로 넘기기

        } catch (Exception e) {
            e.printStackTrace();
        }

        return mav;
    }

	
	@RequestMapping(value = "/professor/attendance/write", method = RequestMethod.GET)
	public ModelAndView wirteForm(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("professor/attendance/write");
		mav.addObject("mode", "write");
		return mav;
	}
	
	@RequestMapping(value = "/professor/attendance/write", method = RequestMethod.POST)
	public ModelAndView writeSubmit(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		AttendanceDAO dao = new AttendanceDAO();
		try {
			Attendance_recordDTO dto = new Attendance_recordDTO();

			dto.setStatus(Integer.parseInt(req.getParameter("status")));
			
			dto.setCourse_id(Integer.parseInt(req.getParameter("course_id")));
			
			dao.insertAttendance(dto);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/professor/attendance/list");
	}
	
	
	
}
