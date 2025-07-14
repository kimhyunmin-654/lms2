package com.lms2.controller;

import java.io.IOException;
import com.lms2.dao.ProfessorDAO;
import com.lms2.model.ProfessorDTO;
import com.lms2.mvc.annotation.Controller;
import com.lms2.mvc.annotation.RequestMapping;
import com.lms2.mvc.annotation.RequestMethod;
import com.lms2.mvc.view.ModelAndView;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class ProfessorController {
	
	@RequestMapping(value = "/admin/professor/list", method = RequestMethod.GET)
	public ModelAndView list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    ModelAndView mav = new ModelAndView("admin/professor/list");
	    return mav;
	}
	
	@RequestMapping(value = "/admin/professor/write", method = RequestMethod.GET)
	public ModelAndView wirteForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("admin/professor/write");
		mav.addObject("mode", "write");
		return mav;
	}
	
	@RequestMapping(value = "/admin/professor/write", method = RequestMethod.POST)
	public ModelAndView writeSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    ProfessorDAO dao = new ProfessorDAO();
	    try {
	        ProfessorDTO dto = new ProfessorDTO();
	        
	        dto.setMember_id(req.getParameter("member_id"));
	        dto.setName(req.getParameter("name"));
	        dto.setPassword(req.getParameter("password"));
	        dto.setAvatar(req.getParameter("avatar"));
	        dto.setEmail(req.getParameter("email"));
	        dto.setPhone(req.getParameter("phone"));
	        dto.setBirth(req.getParameter("birth"));
	        dto.setAddr1(req.getParameter("addr1"));
	        dto.setAddr2(req.getParameter("addr2"));
	        
	        dto.setPosition(req.getParameter("position"));
	        dto.setDepartment_id(req.getParameter("department_id"));
	        
	        dao.insertProfessor(dto);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return new ModelAndView("redirect:/admin/professor/list");
	}
	
}
