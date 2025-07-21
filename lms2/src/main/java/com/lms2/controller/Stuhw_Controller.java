package com.lms2.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.lms2.dao.Stuhw_DAO;
import com.lms2.model.SessionInfo;
import com.lms2.model.Std_hwDTO;
import com.lms2.mvc.annotation.Controller;
import com.lms2.mvc.annotation.RequestMapping;
import com.lms2.mvc.annotation.RequestMethod;
import com.lms2.mvc.view.ModelAndView;
import com.lms2.util.FileManager;
import com.lms2.util.MyMultipartFile;
import com.lms2.util.MyUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;


@Controller
public class Stuhw_Controller {

	@RequestMapping(value = "/student/hw/list", method = RequestMethod.GET)
	public ModelAndView list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    Stuhw_DAO dao = new Stuhw_DAO();
	    MyUtil util = new MyUtil();

	    HttpSession session = req.getSession(false);

	    if (session == null) {
	        return new ModelAndView("redirect:/login");
	    }

	    SessionInfo info = (SessionInfo) session.getAttribute("student");
	    if (info == null) {
	        return new ModelAndView("redirect:/login");
	    }
	    ModelAndView mav = new ModelAndView("/student/hw/list");

	    try {
	        String page = req.getParameter("page");
	        int current_page = 1;
	        if (page != null) {
	            current_page = Integer.parseInt(page);
	        }

	        String schType = req.getParameter("schType");
	        String kwd = req.getParameter("kwd");
	        if (schType == null) {
	            schType = "all";
	            kwd = "";
	        }
	        kwd = util.decodeUrl(kwd);

	        int dataCount;
	        if (kwd.length() == 0) {
	            dataCount = dao.dataCount(info.getMember_id());
	        } else {
	            dataCount = dao.dataCount(info.getMember_id(), schType, kwd);
	        }

	        int size = 10;
	        int total_page = util.pageCount(dataCount, size);
	        if (current_page > total_page) {
	            current_page = total_page;
	        }

	        int offset = (current_page - 1) * size;
	        if (offset < 0) offset = 0;

	        List<Std_hwDTO> list;
	        if (kwd.length() == 0) {
	            list = dao.listAssignment(info.getMember_id(), offset, size);
	        } else {
	            list = dao.listAssignment(info.getMember_id(), offset, size, schType, kwd);
	        }

	        String query = "";
	        if (kwd.length() != 0) {
	            query = "schType=" + schType + "&kwd=" + util.encodeUrl(kwd);
	        }

	        String lectureCode = req.getParameter("lecture_code");
	        String cp = req.getContextPath();
	        String listUrl = cp + "/student/hw/list";
	        String articleUrl = cp + "/student/hw/list?page=" + current_page + "&lecture_code=" + lectureCode;

	        if (query.length() != 0) {
	            listUrl += "?" + query;
	            articleUrl += "&" + query;
	        }

	        String paging = util.paging(current_page, total_page, listUrl);

	        mav.addObject("list", list);
	        mav.addObject("dataCount", dataCount);
	        mav.addObject("size", size);
	        mav.addObject("page", current_page);
	        mav.addObject("total_page", total_page);
	        mav.addObject("articleUrl", articleUrl);
	        mav.addObject("paging", paging);
	        mav.addObject("schType", schType);
	        mav.addObject("kwd", kwd);
	        mav.addObject("lecture_code", lectureCode);

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return mav;
	}
    @RequestMapping(value = "/student/hw/submit", method = RequestMethod.POST)
    public ModelAndView submitAssignment(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        SessionInfo info = (SessionInfo) session.getAttribute("student");

        String root = session.getServletContext().getRealPath("/");
        String pathname = root + "uploads" + File.separator + "student_assignment";

        Stuhw_DAO dao = new Stuhw_DAO();

        try {
            int assignId = Integer.parseInt(req.getParameter("assign_id"));
            String content = req.getParameter("submit_content");

            Std_hwDTO dto = new Std_hwDTO();
            dto.setAssign_id(assignId);
            dto.setAssign_content(content);
            dto.setAssign_status(1);  

            int courseId = dao.findCourseId(info.getMember_id(), assignId); 
            dto.setCourse_id(courseId);

            // 파일 처리
            Part p = req.getPart("upload");
            if (p != null && p.getSize() > 0) {
                FileManager fileManager = new FileManager();
                MyMultipartFile multi = fileManager.doFileUpload(p, pathname);

                if (multi != null) {
                    dto.setSave_filename(multi.getSaveFilename());
                    dto.setOriginal_filename(multi.getOriginalFilename());
                    dto.setFile_size(multi.getSize());
                }
            }

            dao.insertAssignment(dto); 
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ModelAndView("redirect:/student/hw/list");
    }
}
