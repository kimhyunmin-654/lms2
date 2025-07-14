package com.lms2.controller;

import java.io.File;
import java.io.IOException;

import com.lms2.dao.LectureDAO;
import com.lms2.model.LectureDTO;
import com.lms2.mvc.annotation.Controller;
import com.lms2.mvc.annotation.RequestMapping;
import com.lms2.mvc.annotation.RequestMethod;
import com.lms2.mvc.view.ModelAndView;
import com.lms2.util.FileManager;
import com.lms2.util.MyMultipartFile;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

@Controller
public class LectureController {

	@RequestMapping(value = "/admin/lecture/list", method = RequestMethod.GET)
	public ModelAndView list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    ModelAndView mav = new ModelAndView("admin/lecture/list");
	    return mav;
	}
	
	@RequestMapping(value = "/admin/lecture/write", method = RequestMethod.GET)
	public ModelAndView wirteForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("admin/lecture/write");
		mav.addObject("mode", "write");
		return mav;
	}
	
	@RequestMapping(value = "/admin/lecture/write", method = RequestMethod.POST)
	public ModelAndView writeSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    LectureDAO dao = new LectureDAO();
	    //FileManager fileManager = new FileManager();
	    
	    HttpSession session = req.getSession();
	    
	    String root = session.getServletContext().getRealPath("/");
		//String pathname = root + "uploads" + File.separator + "member";
		
	    try {
	        LectureDTO dto = new LectureDTO();
	        
	        dto.setLecture_code(req.getParameter("lecture_code"));
	        dto.setSubject(req.getParameter("subject"));
	        dto.setGrade(Integer.parseInt(req.getParameter("grade")));
	        dto.setClassroom(req.getParameter("classroom"));
	        dto.setDivision(req.getParameter("division"));
	        dto.setLecture_year(Integer.parseInt(req.getParameter("lecture_year")));
	        dto.setSemester(req.getParameter("semester"));
	        dto.setCapacity(Integer.parseInt(req.getParameter("capacity")));
	        dto.setCredit(req.getParameter("credit"));
	        dto.setDepartment_id(req.getParameter("department_id"));
	        dto.setMember_id(req.getParameter("member_id"));

	        /*
	        Part p = req.getPart("selectFile");
	        MyMultipartFile multiFile = fileManager.doFileUpload(p, pathname);
			if (multiFile != null) {
				String saveFilename = multiFile.getSaveFilename();
				String originalFilename = multiFile.getOriginalFilename();
				dto.setSava_filename(saveFilename);
				dto.setOriginal_filename(originalFilename);
			}
			*/
	        
	        dao.insertLecture(dto);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return new ModelAndView("redirect:/admin/lecture/list");
	}

}
