package com.lms2.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.lms2.mvc.annotation.RequestMapping;
import com.lms2.mvc.annotation.RequestMethod;
import com.lms2.dao.StudentDAO;
import com.lms2.model.StudentDTO;
import com.lms2.mvc.annotation.Controller;
import com.lms2.mvc.view.ModelAndView;
import com.lms2.util.MyUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class AdminController {
	
	@RequestMapping(value = "/admin/student/list", method = RequestMethod.GET)
	public ModelAndView studentList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 학생 리스트
		StudentDAO dao = new StudentDAO();
		MyUtil util = new MyUtil();
		
		ModelAndView mav = new ModelAndView("admin/student/list");
		
		try {
			String page = req.getParameter("page");
			int current_page = 1;
			if(page != null) {
				current_page = Integer.parseInt(page);
			}
			
			int dataCount = dao.dataCount();
			
			int size = 10;
			int total_page = util.pageCount(dataCount, size);
			if(current_page > total_page) {
				current_page = total_page;
			}
			
			int offset = (current_page - 1) * size;
			if(offset < 0) offset = 0;
			
			List<StudentDTO> list = dao.listStudent(offset, size);
			
			String cp = req.getContextPath();
			String listUrl = cp + "/admin/student/list";
			String articleUrl = cp + "admin/article?page=" + current_page;
			
			String paging = util.paging(current_page, total_page, listUrl);
			
			mav.addObject("list", list);
			mav.addObject("dataCount", dataCount);
			mav.addObject("size", size);
			mav.addObject("page", page);
			mav.addObject("total_page", total_page);
			mav.addObject("articleUrl", articleUrl);
			mav.addObject("paging", paging);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}
	
	@RequestMapping(value = "/admin/student/account", method = RequestMethod.GET)
	public ModelAndView accountStudentForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 학생 등록 폼
		ModelAndView mav = new ModelAndView("admin/student/account");
		mav.addObject("mode", "account");
		
		return mav;
	}
	
	@RequestMapping(value = "admin/student/account", method = RequestMethod.POST)
	public ModelAndView accountStudentSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 학생 등록 폼
		StudentDAO dao = new StudentDAO();
		
		// 사진 파일은 나중에... -김하은
		
		HttpSession session = req.getSession();
		
		String message = "";
		
		try {
			StudentDTO dto = new StudentDTO();
			
			dto.setMember_id(req.getParameter("userId"));
			dto.setName(req.getParameter("name"));
			dto.setPassword(req.getParameter("password"));
			dto.setEmail(req.getParameter("email"));
			dto.setPhone(req.getParameter("phone"));
			dto.setBirth(req.getParameter("birth"));
			dto.setAddr1(req.getParameter("addr1"));
			dto.setAddr2(req.getParameter("addr2"));
			
			dto.setGrade(Integer.parseInt(req.getParameter("grade")));
			
			dao.insertStudent(dto);
			
			session.setAttribute("mode", "account");
			
			return new ModelAndView("redirect:/admin/student/list");
			
		} catch (SQLException e) {
			if(e.getErrorCode() == 1) {
				message = "이미 등록된 학번/사번 입니다.";
			} else if(e.getErrorCode() == 1840 || e.getErrorCode() == 1861) {
				message = "날짜 형식이 올바르지 않습니다.";
			} else {
				message = "학생 등록에 실패했습니다.";
			}
		} catch (Exception e) {
			message = "학생 등록에 실패했습니다.";
			e.printStackTrace();
		}
		
		ModelAndView mav = new ModelAndView("admin/student/account");
		mav.addObject("mode", "account");
		mav.addObject("message", message);
		
		return mav;
	}
	
	@RequestMapping(value = "/student/complete", method = RequestMethod.GET)
	public ModelAndView studentComplete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 학생 등록 완료 후
		HttpSession session = req.getSession();
		
		String mode = (String)session.getAttribute("mode");
		String userName = (String)session.getAttribute("name");
		
		session.removeAttribute("mode");
		session.removeAttribute("name");
		
		if(mode == null) {
			return new ModelAndView("redirect:/");
		}
		
		String title;
		String message = "<b>" + userName + "</b>님 ";
		
		if(mode.equals("account")) {
			title = "학생등록";
			message += "학생 등록이 완료 되었습니다.";
		} else {
			title = "학생 정보 수정";
			message += "학생 정보 수정이 완료되었습니다.";
		}
		
		ModelAndView mav = new ModelAndView("student/complete");
		
		mav.addObject("title", title);
		mav.addObject("message", message);
		
		return mav;
	}

	
}
