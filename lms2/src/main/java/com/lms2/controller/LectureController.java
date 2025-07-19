package com.lms2.controller;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.lms2.dao.LectureDAO;
import com.lms2.dao.NoticeDAO;
import com.lms2.model.LectureDTO;
import com.lms2.model.NoticeDTO;
import com.lms2.model.SessionInfo;
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
public class LectureController {

	
	@RequestMapping(value = "/admin/lecture/list", method = RequestMethod.GET)
	public ModelAndView list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 공지사항 리스트
		LectureDAO dao = new LectureDAO();
		MyUtil util = new MyUtil();
		
		ModelAndView mav = new ModelAndView("admin/lecture/list");
		
		try {
			String page = req.getParameter("page");
			int current_page = 1;
			if (page != null && !page.equals("") && !page.equals("null")) {
			    current_page = Integer.parseInt(page);
			}
			page = String.valueOf(current_page);
			
			String schType = req.getParameter("schType");
			String kwd = req.getParameter("kwd");
			if(schType == null) {
				schType = "all";
				kwd = "";
			}
			kwd = util.decodeUrl(kwd);
			
			String pageSize = req.getParameter("size");
			int size = pageSize == null ? 10 : Integer.parseInt(pageSize);
			
			int dataCount,  total_page;
			
			if(kwd.length() != 0) {
				dataCount = dao.dataCount(schType, kwd);
			} else {
				dataCount = dao.dataCount();
			}
		
			
			total_page = util.pageCount(dataCount, size);
						
			
			if(current_page > total_page) {
				current_page = total_page;
			}
			
					
			int offset = (current_page - 1) * size;
			if(offset < 0) offset = 0;
			
			List<LectureDTO> list;
			if(kwd.length() != 0) {
				list = dao.listLecture(offset, size, schType, kwd);				
			} else {
				list = dao.listLecture(offset, size);
			}
			
			
			String cp = req.getContextPath();
			String query = "size=" + size;
			String listUrl;
			String articleUrl;
			
			if(kwd.length() != 0) {
				query += "&schType=" + schType + "&kwd=" + util.encodeUrl(kwd);
			}
			listUrl = cp + "/admin/notice/list?" + query;
			articleUrl = cp + "/admin/notice/article?page=" + current_page + "&" + query;
			
			String paging = util.paging(current_page, total_page, listUrl);
			
			// 포워딩 jsp에 전달할 데이터
			mav.addObject("list", list);
			mav.addObject("articleUrl", articleUrl);
			mav.addObject("dataCount", dataCount);
			mav.addObject("size", size);
			mav.addObject("page", current_page);
			mav.addObject("total_page", total_page);
			mav.addObject("paging", paging);
			mav.addObject("schType", schType);
			mav.addObject("kwd", kwd);
						
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return mav;
		
	}	
	
	@RequestMapping(value = "/admin/lecture/write", method = RequestMethod.GET)
	public ModelAndView wirteForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("admin/lecture/write");
		
		String size = req.getParameter("size");
		
		mav.addObject("mode", "write");
		mav.addObject("size", size);
		
		return mav;
	}
	
	@RequestMapping(value = "/admin/lecture/write", method = RequestMethod.POST)
	public ModelAndView writeSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    LectureDAO dao = new LectureDAO();
	    FileManager fileManager = new FileManager();
	    
	    HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
	    
	    String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "member";
		
		String size = req.getParameter("size");
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
	        dto.setCredit(Double.parseDouble(req.getParameter("credit")));
	        dto.setDepartment_id(req.getParameter("department_id"));
	        dto.setMember_id(req.getParameter("member_id"));

	        Collection<Part> parts = req.getParts();
	        List<Part> uploadParts = new ArrayList<Part>();
	        for(Part part: parts) {
	        	if(part.getName().equals("upload") && part.getSize() > 0) {
	        		uploadParts.add(part);
	        	}
	        }
	        
	        List<MyMultipartFile> listFile = fileManager.doFileUpload(uploadParts, pathname);
	        dto.setListFile(listFile);
	        
	        dao.insertLecture(dto);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return new ModelAndView("redirect:/admin/lecture/list");
	}
	
	@RequestMapping(value = "/admin/lecture/article", method = RequestMethod.GET)
	public ModelAndView article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글보기
		LectureDAO dao = new LectureDAO();
		MyUtil util = new MyUtil();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		String page = req.getParameter("page");
		String size = req.getParameter("size");
		String query = "page=" + page + "&size=" + size;
		
		try {
			long lecture_code = Long.parseLong(req.getParameter("lecture_code"));
			
			String schType = req.getParameter("schType");
			String kwd = req.getParameter("kwd");
			if(schType == null) {
				schType = "all";
				kwd = "";
			}
			kwd = util.decodeUrl(kwd);
			
			if(kwd.length() != 0) {
				query += "&schType=" + schType + "&kwd=" + util.encodeUrl(kwd);
			}
			
	        @SuppressWarnings("unchecked")
	        Set<Long> viewed = (Set<Long>) session.getAttribute("viewedNotice");
	        if (viewed == null) {
	            viewed = new HashSet<>();
	        }
			
			LectureDTO dto = dao.findById(lecture_code);
			if(dto == null) {
				return new ModelAndView("redirect:/admin/lecture/list?" + query);
				
			}
					
			List<LectureDTO> listFile = dao.listLectureFile(lecture_code);
			
			ModelAndView mav = new ModelAndView("admin/lecture/article");
			
			mav.addObject("dto", dto);
			mav.addObject("listFile", listFile);
			mav.addObject("query", query);
			mav.addObject("page", page);
			mav.addObject("size", size);
			
			return mav;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("redirect:/admin/lecture/list?" + query);
	
	}	
	
	@RequestMapping(value = "/admin/lecture/update", method = RequestMethod.GET)
	public ModelAndView updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 수정 폼
		LectureDAO dao = new LectureDAO();

		String page = req.getParameter("page");
		String size = req.getParameter("size");

		try {
			long lecture_code = Long.parseLong(req.getParameter("lecture_code"));

			LectureDTO dto = dao.findById(lecture_code);
			if (dto == null) {
				return new ModelAndView("redirect:/admin/notice/list?page=" + page + "&size=" + size);
			}

			// 파일
			List<LectureDTO> listFile = dao.listLectureFile(lecture_code);

			ModelAndView mav = new ModelAndView("admin/lecture/write");
			
			mav.addObject("dto", dto);
			mav.addObject("listFile", listFile);
			mav.addObject("page", page);
			mav.addObject("size", size);

			mav.addObject("mode", "update");

			return mav;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/admin/lecture/list?page=" + page + "&size=" + size);
	}
	
	
	@RequestMapping(value = "/admin/lecture/update", method = RequestMethod.POST)
	public ModelAndView updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 수정 완료
		LectureDAO dao = new LectureDAO();
		FileManager fileManager = new FileManager();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		if (info.getRole() < 51) {
			return new ModelAndView("redirect:/");
		}
		
		// 파일 저장 경로
		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "lecture";
		
		String page = req.getParameter("page");
		if (page == null || page.equals("") || page.equals("null")) {
			page = "1";
		}

		String size = req.getParameter("size");
		if (size == null || size.equals("") || size.equals("null")) {
			size = "10";
		}

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
			dto.setCredit(Double.parseDouble(req.getParameter("credit")));
			
						
			Collection<Part> parts = req.getParts();
			List<Part> uploadParts = new ArrayList<>();
			for (Part part : parts) {
			    if (part.getName().equals("upload") && part.getSize() > 0) {
			        uploadParts.add(part);
			    }
			}

			List<MyMultipartFile> listFile = fileManager.doFileUpload(uploadParts, pathname);
			dto.setListFile(listFile);
			
			dao.updateLecture(dto);
			

		} catch (Exception e) {
			e.printStackTrace();
		}

		String query = "page=" + page + "&size=" + size;
		return new ModelAndView("redirect:/admin/lecture/list?" + query);
	}
	
	/*
	@RequestMapping(value = "/admin/lecture/deleteFile")
	public ModelAndView deleteFile(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 수정에서 파일만 삭제
		LectureDAO dao = new LectureDAO();
		FileManager fileManager = new FileManager();
		
		HttpSession session = req.getSession();
		
		// 파일 저장 경로
		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "lecture";

		String page = req.getParameter("page");
		String size = req.getParameter("size");

		try {
			long num = Long.parseLong(req.getParameter("num"));
			long fileNum = Long.parseLong(req.getParameter("fileNum"));
			NoticeDTO dto = dao.findByFileId(fileNum);
			if (dto != null) {
				// 파일삭제
				fileManager.doFiledelete(pathname, dto.getSave_filename());
				
				// 테이블 파일 정보 삭제
				dao.deleteNoticeFile("one", fileNum);
			}

			// 다시 수정 화면으로
			return new ModelAndView("redirect:/admin/notice/update?num=" + num + "&page=" + page + "&size=" + size);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/admin/notice/list?page=" + page + "&size=" + size);
	}
	*/
	

}
