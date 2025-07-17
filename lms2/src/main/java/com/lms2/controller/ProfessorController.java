package com.lms2.controller;

import java.io.IOException;
import java.util.List;

import com.lms2.dao.LectureDAO;
import com.lms2.dao.NoticeDAO;
import com.lms2.dao.ProfessorDAO;
import com.lms2.dao.StudentDAO;
import com.lms2.model.Course_ApplicationDTO;
import com.lms2.model.LectureDTO;
import com.lms2.model.NoticeDTO;
import com.lms2.model.ProfessorDTO;
import com.lms2.model.SessionInfo;
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
public class ProfessorController {

	@RequestMapping(value = "/admin/professor/list", method = RequestMethod.GET)
	public ModelAndView list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("admin/professor/list");
		return mav;
	}

	@RequestMapping(value = "/admin/professor/write", method = RequestMethod.GET)
	public ModelAndView wirteForm(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("admin/professor/write");
		mav.addObject("mode", "write");
		return mav;
	}

	@RequestMapping(value = "/admin/professor/write", method = RequestMethod.POST)
	public ModelAndView writeSubmit(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
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
	
	@RequestMapping(value = "/professor/main/main", method = RequestMethod.GET)
	public ModelAndView main(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		ProfessorDAO dao = new ProfessorDAO();
		ModelAndView mav = new ModelAndView("professor/main/main");

		try {
			// 강의목록
			List<LectureDTO> list = dao.plistLecture(info.getMember_id());
			mav.addObject("list", list);
			

		} catch (Exception e) {
			e.printStackTrace();
		}

		return mav;
	}
	
	@RequestMapping(value = "/professor/lecture/compList", method = RequestMethod.GET)
	public ModelAndView studentCompList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		ProfessorDAO dao = new ProfessorDAO();
		ModelAndView mav = new ModelAndView("professor/lecture/compList");
		LectureDAO lectureDao = new LectureDAO();
		
		try {
			
			if (info != null) {
	            String memberId = String.valueOf(info.getMember_id());
	            List<LectureDTO> lectures = lectureDao.listsidebar(memberId);
	            mav.addObject("lectureList", lectures);
	        }

			// 강의목록
			List<LectureDTO> list = dao.plistLecture(info.getMember_id());
			mav.addObject("list", list);
			

		} catch (Exception e) {
			e.printStackTrace();
		}

		return mav;
	}
	
	// 강의 상세 페이지
	@RequestMapping(value = "/professor/lecture/main1", method = RequestMethod.GET)
	public ModelAndView lectureDetail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    ProfessorDAO dao = new ProfessorDAO();
	    HttpSession session = req.getSession();
	    SessionInfo info = (SessionInfo) session.getAttribute("member");
	    LectureDAO lectureDao = new LectureDAO();
	    
	    String page = req.getParameter("page");
		String size = req.getParameter("size");
		String query = "page=" + page + "&size=" + size;

	    try {
	    	    	
	        String lecture_code = req.getParameter("lecture_code");

	        ModelAndView mav = new ModelAndView("professor/lecture/main1");
	        
	        LectureDTO dto = dao.findById1(lecture_code);
	        
	        if (info != null) {
	            String memberId = String.valueOf(info.getMember_id());
	            List<LectureDTO> lectures = lectureDao.listsidebar(memberId);
	            mav.addObject("lectureList", lectures);
	        }
	        
	        mav.addObject("dto", dto);
			mav.addObject("query", query);
			mav.addObject("page", page);
			mav.addObject("size", size);
			
			return mav;
	       
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return new ModelAndView("redirect:/professor/lecture/compList?" + query);
	}
	
	
	

	
	
}
