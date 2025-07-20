package com.lms2.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.lms2.dao.LectureDAO;
import com.lms2.dao.MemberDAO;
import com.lms2.dao.NoticeDAO;
import com.lms2.model.LectureDTO;
import com.lms2.model.MemberDTO;
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
		// 강의 리스트
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
			listUrl = cp + "/admin/lecture/list?" + query;
			articleUrl = cp + "/admin/lecture/article?page=" + current_page + "&" + query;
			
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
		
		try {
			MemberDAO dao = new MemberDAO(); 
			List<MemberDTO> professorList = dao.listProfessor(); 
			
			mav.addObject("professorList", professorList);
		} catch (SQLException e) {
			e.printStackTrace();
		}	
			
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
		String pathname = root + "uploads" + File.separator + "lecture";
		
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
			String lecture_code = req.getParameter("lecture_code");
			
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
			String lecture_code = req.getParameter("lecture_code");

			LectureDTO dto = dao.findById(lecture_code);
			if (dto == null) {
				return new ModelAndView("redirect:/admin/lecture/list?page=" + page + "&size=" + size);
			}

			// 파일
			List<LectureDTO> listFile = dao.listLectureFile(lecture_code);

			ModelAndView mav = new ModelAndView("admin/lecture/write");
			
			// 교수 리스트 가져오기
			MemberDAO memberDao = new MemberDAO();
		    List<MemberDTO> professorList = memberDao.listProfessor();
			
			mav.addObject("dto", dto);
			mav.addObject("listFile", listFile);
			mav.addObject("page", page);
			mav.addObject("size", size);
			 mav.addObject("professorList", professorList);
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
			dto.setDepartment_id(req.getParameter("department_id"));
			dto.setMember_id(req.getParameter("member_id"));
			
						
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
			String lecture_code = req.getParameter("lecture_code");
			long file_num = Long.parseLong(req.getParameter("file_num"));
			LectureDTO dto = dao.findByFileId(file_num);
			if (dto != null) {
				// 파일삭제
				fileManager.doFiledelete(pathname, dto.getSave_filename());
				
				// 테이블 파일 정보 삭제
				dao.deleteLectureFile(file_num);
			}

			// 다시 수정 화면으로
			return new ModelAndView("redirect:/admin/lecture/update?lecture_code=" + lecture_code + "&page=" + page + "&size=" + size);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/admin/lecture/list?page=" + page + "&size=" + size);
	}
	
	@RequestMapping(value = "/admin/lecture/delete", method = RequestMethod.GET)
	public ModelAndView delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 삭제
		LectureDAO dao = new LectureDAO();
		MyUtil util = new MyUtil();
		FileManager fileManager = new FileManager();
		
		HttpSession session = req.getSession();
		
		// 파일 저장 경로
		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "lecture";

		String page = req.getParameter("page");
		String size = req.getParameter("size");
		String query = "page=" + page + "&size=" + size;

		try {
			String lecture_code = req.getParameter("lecture_code");
			
			String schType = req.getParameter("schType");
			String kwd = req.getParameter("kwd");
			if (schType == null) {
				schType = "all";
				kwd = "";
			}
			kwd = util.decodeUrl(kwd);

			if (kwd.length() != 0) {
				query += "&schType=" + schType + "&kwd=" + util.encodeUrl(kwd);
			}

			LectureDTO dto = dao.findById(lecture_code);
			if (dto == null) {
				return new ModelAndView("redirect:/admin/lecture/list?" + query);
			}

			// 파일삭제
			List<LectureDTO> listFile = dao.listLectureFile(lecture_code);
			for (LectureDTO vo : listFile) {
				fileManager.doFiledelete(pathname, vo.getSave_filename());
			}
			dao.deleteLectureFile(lecture_code);

			// 게시글 삭제
			dao.deleteLecture(lecture_code);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/admin/lecture/list?" + query);
	}
	
	@RequestMapping(value = "/admin/lecture/download", method = RequestMethod.GET)
	public void download(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 파일 다운로드
		// 넘어온 파라미터 : 파일번호
		LectureDAO dao = new LectureDAO();
		FileManager fileManager = new FileManager();
		
		HttpSession session = req.getSession();
		
		// 파일 저장 경로
		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "lecture";
		
		boolean b = false;

		try {
			long file_num = Long.parseLong(req.getParameter("file_num"));

			LectureDTO dto = dao.findByFileId(file_num);
			if (dto != null) {
				b = fileManager.doFiledownload(dto.getSave_filename(), dto.getOriginal_filename(), pathname, resp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if ( ! b ) {
			resp.setContentType("text/html;charset=utf-8");
			PrintWriter out = resp.getWriter();
			out.print("<script>alert('파일다운로드가 실패 했습니다.');history.back();</script>");
		}
	}
	
	@RequestMapping(value = "/admin/lecture/deleteList", method = RequestMethod.POST)
	public ModelAndView deleteList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 게시글다중삭제
		MyUtil util = new MyUtil();
		FileManager fileManager = new FileManager();
		
		HttpSession session = req.getSession();
		
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
		String query = "size=" + size + "&page=" + page;

		try {
			String schType = req.getParameter("schType");
			String kwd = req.getParameter("kwd");
			if (schType == null) {
				schType = "all";
				kwd = "";
			}
			kwd = util.decodeUrl(kwd);
			if (kwd.length() != 0) {
				query += "&schType=" + schType + "&kwd=" + util.encodeUrl(kwd);
			}

	        String[] codes = req.getParameterValues("nums");

	        LectureDAO dao = new LectureDAO();

	        for (String code : codes) {
	            List<LectureDTO> listFile = dao.listLectureFile(code);
	            for (LectureDTO vo : listFile) {
	                fileManager.doFiledelete(pathname, vo.getSave_filename());
	            }
	            dao.deleteLectureFile(code);
	        }

	        dao.deleteLecture(codes);

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

		return new ModelAndView("redirect:/admin/lecture/list?" + query);
	}

}
