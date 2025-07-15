package com.lms2.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.lms2.dao.AdminDAO;
import com.lms2.model.AdminDTO;
import com.lms2.mvc.annotation.Controller;
import com.lms2.mvc.annotation.RequestMapping;
import com.lms2.mvc.annotation.RequestMethod;
import com.lms2.mvc.view.ModelAndView;
import com.lms2.util.MyUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class AdminController {
	
	@RequestMapping(value = "/admin/admin/list", method = RequestMethod.GET)
	public ModelAndView adminList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 관리자 리스트
		AdminDAO dao = new AdminDAO();
		MyUtil util = new MyUtil();
		
		ModelAndView mav = new ModelAndView("admin/admin/list");
		
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
			
			List<AdminDTO> list = dao.listAdmin(offset, size);
			
			String cp = req.getContextPath();
			String listUrl = cp + "/admin/admin/list";
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
	
	@RequestMapping(value = "/admin/admin/account", method = RequestMethod.GET)
	public ModelAndView accountAdminForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 관리자 등록 폼
		ModelAndView mav = new ModelAndView("admin/admin/account");
		mav.addObject("mode", "account");
		
		return mav;
	}
	
	@RequestMapping(value = "/admin/admin/account", method = RequestMethod.POST)
	public ModelAndView accountAdminSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 관리자 등록
		AdminDAO dao = new AdminDAO();
		
		HttpSession session = req.getSession();
		
		String message = "";
		
		try {
			AdminDTO dto = new AdminDTO();
			
			dto.setMember_id(req.getParameter("userId"));
			dto.setName(req.getParameter("name"));
			dto.setPassword(req.getParameter("password"));
			String e1 = req.getParameter("email1");
			String e2 = req.getParameter("email2");
			dto.setEmail(e1 + "@" + e2);
			dto.setPhone(req.getParameter("phone"));
			dto.setBirth(req.getParameter("birth"));
			dto.setAddr1(req.getParameter("addr1"));
			dto.setAddr2(req.getParameter("addr2"));
			
			dto.setPosition(req.getParameter("position"));
			dto.setDivision(req.getParameter("division"));
			
			dao.insertAdmin(dto);
			
			session.setAttribute("mode", "account");
			
			return new ModelAndView("redirect:/admin/admin/list");
			
		} catch (SQLException e) {
			if(e.getErrorCode() == 1) {
				message = "이미 등록된 사번입니다.";
			} else if(e.getErrorCode() == 1840 || e.getErrorCode() == 1861) {
				message = "날짜 형식이 올바르지 않습니다.";
			} else {
				message = "관리자 등록에 실패했습니다.";
			}
		} catch (Exception e) {
			message = "관리자 등록에 실패했습니다.";
			e.printStackTrace();
		}
		
		ModelAndView mav = new ModelAndView("admin/admin/account");
		mav.addObject("mode", "account");
		mav.addObject("message", message);
		
		return mav;
	}
	
	
	

	
}
