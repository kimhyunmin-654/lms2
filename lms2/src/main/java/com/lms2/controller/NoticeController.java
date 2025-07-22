package com.lms2.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
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
public class NoticeController {
	
	@RequestMapping(value = "/admin/notice/list", method = RequestMethod.GET)
	public ModelAndView list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 공지사항 리스트
		NoticeDAO dao = new NoticeDAO();
		MyUtil util = new MyUtil();
		
		ModelAndView mav = new ModelAndView("admin/notice/list");
		
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
			
			int dataCount,  dataCount2 , total_page;
			
			if(kwd.length() != 0) {
				dataCount = dao.dataCount(schType, kwd);
			} else {
				dataCount = dao.dataCount();
			}
			
			if(kwd.length() != 0) {
				dataCount2 = dao.dataCount2(schType, kwd);
			} else {
				dataCount2 = dao.dataCount2();
			}			
			
			total_page = util.pageCount(dataCount, size);
			
			
			
			
			if(current_page > total_page) {
				current_page = total_page;
			}
			
			
			
			
			int offset = (current_page - 1) * size;
			if(offset < 0) offset = 0;
			
			List<NoticeDTO> list;
			if(kwd.length() != 0) {
				list = dao.listNotice(offset, size, schType, kwd);
			} else {
				list = dao.listNotice(offset, size);
			}
			
			// 공지글
			List<NoticeDTO> listNotice = dao.listNotice();
			
			long gap;
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			LocalDateTime today = LocalDateTime.now();
			for (NoticeDTO dto : list) {
				LocalDateTime dateTime = LocalDateTime.parse(dto.getReg_date(), formatter);
				gap = dateTime.until(today, ChronoUnit.HOURS);
				dto.setGap(gap);

				dto.setReg_date(dto.getReg_date().substring(0, 10));
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
			mav.addObject("listNotice", listNotice);
			mav.addObject("articleUrl", articleUrl);
			mav.addObject("dataCount", dataCount);
			mav.addObject("dataCount2", dataCount2);
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
	
	@RequestMapping(value = "/admin/notice/account", method = RequestMethod.GET)
	public ModelAndView writeForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글쓰기 폼
		ModelAndView mav = new ModelAndView("admin/notice/account");
		
		String size = req.getParameter("size");
		
		mav.addObject("mode", "account");
		mav.addObject("size", size);
		
		return mav;
	}
	
	
	@RequestMapping(value = "/admin/notice/account", method = RequestMethod.POST)
	public ModelAndView writeSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글 저장
		NoticeDAO dao = new NoticeDAO();
		FileManager fileManager = new FileManager();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		// 파일 저장 경로
		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "notice";

		String size = req.getParameter("size");
		try {
			NoticeDTO dto = new NoticeDTO(); 
			
			dto.setMember_id(info.getMember_id());
			if(req.getParameter("is_notice") != null) {
				dto.setIs_notice(Integer.parseInt(req.getParameter("is_notice")));
			}
			
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));
			
			Collection<Part> parts = req.getParts();
			List<Part> uploadParts = new ArrayList<>();
			for (Part part : parts) {
			    if (part.getName().equals("upload") && part.getSize() > 0) {
			        uploadParts.add(part);
			    }
			}
			
			
			List<MyMultipartFile> listFile = fileManager.doFileUpload(uploadParts, pathname); 
			dto.setListFile(listFile);
			
			dao.insertNotice(dto);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ModelAndView("redirect:/admin/notice/list");
				
	}
	
	@RequestMapping(value = "/admin/notice/article", method = RequestMethod.GET)
	public ModelAndView article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 공지사항 상세보기
		NoticeDAO dao = new NoticeDAO();
		MyUtil util = new MyUtil();
		
		HttpSession session = req.getSession();
		
		String page = req.getParameter("page");
		String size = req.getParameter("size");
		String query = "page=" + page + "&size=" + size;
		
		try {
			long notice_id = Long.parseLong(req.getParameter("notice_id"));
			
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
	        
	        if (!viewed.contains(notice_id)) {
	            dao.updateHitCount(notice_id); // 조회수 증가
	            viewed.add(notice_id);         // 세션에 조회 기록 저장
	            session.setAttribute("viewedNotice", viewed); // 세션에 다시 저장
	        }
			
			NoticeDTO dto = dao.findById(notice_id);
			if(dto == null) {
				return new ModelAndView("redirect:/admin/notice/list?" + query);
				
			}
			
			NoticeDTO prevDto = dao.findByPrev(dto.getNotice_id(), schType, kwd);
			NoticeDTO nextDto = dao.findByNext(dto.getNotice_id(), schType, kwd);
			
			List<NoticeDTO> listFile = dao.listNoticeFile(notice_id);
			
			ModelAndView mav = new ModelAndView("admin/notice/article");
			
			mav.addObject("dto", dto);
			mav.addObject("prevDto", prevDto);
			mav.addObject("nextDto", nextDto);
			mav.addObject("listFile", listFile);
			mav.addObject("query", query);
			mav.addObject("page", page);
			mav.addObject("size", size);
			
			return mav;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("redirect:/admin/notice/list?" + query);
	
	}
	
	@RequestMapping(value = "/admin/notice/update", method = RequestMethod.GET)
	public ModelAndView updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 수정 폼
		NoticeDAO dao = new NoticeDAO();

		String page = req.getParameter("page");
		String size = req.getParameter("size");

		try {
			long num = Long.parseLong(req.getParameter("notice_id"));

			NoticeDTO dto = dao.findById(num);
			if (dto == null) {
				return new ModelAndView("redirect:/admin/notice/list?page=" + page + "&size=" + size);
			}

			// 파일
			List<NoticeDTO> listFile = dao.listNoticeFile(num);

			ModelAndView mav = new ModelAndView("admin/notice/account");
			
			mav.addObject("dto", dto);
			mav.addObject("listFile", listFile);
			mav.addObject("page", page);
			mav.addObject("size", size);

			mav.addObject("mode", "update");

			return mav;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/admin/notice/list?page=" + page + "&size=" + size);
	}
	
	@RequestMapping(value = "/admin/notice/update", method = RequestMethod.POST)
	public ModelAndView updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 수정 완료
		NoticeDAO dao = new NoticeDAO();
		FileManager fileManager = new FileManager();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		if (info.getRole() < 51) {
			return new ModelAndView("redirect:/");
		}
		
		// 파일 저장 경로
		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "notice";
		
		String page = req.getParameter("page");
		if (page == null || page.equals("") || page.equals("null")) {
			page = "1";
		}

		String size = req.getParameter("size");
		if (size == null || size.equals("") || size.equals("null")) {
			size = "10";
		}

		try {
			NoticeDTO dto = new NoticeDTO();
			dto.setNotice_id(Long.parseLong(req.getParameter("notice_id")));
			if(req.getParameter("is_notice") != null) {
				dto.setIs_notice(Integer.parseInt(req.getParameter("is_notice")));
			}
			
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));
			
			
			Collection<Part> parts = req.getParts();
			List<Part> uploadParts = new ArrayList<>();
			for (Part part : parts) {
			    if (part.getName().equals("upload") && part.getSize() > 0) {
			        uploadParts.add(part);
			    }
			}

			List<MyMultipartFile> listFile = fileManager.doFileUpload(uploadParts, pathname);
			dto.setListFile(listFile);
			
			dao.updateNotice(dto);
			

		} catch (Exception e) {
			e.printStackTrace();
		}

		String query = "page=" + page + "&size=" + size;
		return new ModelAndView("redirect:/admin/notice/list?" + query);
	}
	
	@RequestMapping(value = "/admin/notice/deleteFile")
	public ModelAndView deleteFile(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 수정에서 파일만 삭제
		NoticeDAO dao = new NoticeDAO();
		FileManager fileManager = new FileManager();
		
		HttpSession session = req.getSession();
		
		// 파일 저장 경로
		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "notice";

		String page = req.getParameter("page");
		String size = req.getParameter("size");

		try {
			long notice_id = Long.parseLong(req.getParameter("notice_id"));
			long file_num = Long.parseLong(req.getParameter("file_num"));
			NoticeDTO dto = dao.findByFileId(file_num);
			if (dto != null) {
				// 파일삭제
				fileManager.doFiledelete(pathname, dto.getSave_filename());
				
				// 테이블 파일 정보 삭제
				dao.deleteNoticeFile("one", file_num);
			}

			// 다시 수정 화면으로
			return new ModelAndView("redirect:/admin/notice/update?notice_id=" + notice_id + "&page=" + page + "&size=" + size);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/admin/notice/list?page=" + page + "&size=" + size);
	}
	
	@RequestMapping(value = "/admin/notice/delete", method = RequestMethod.GET)
	public ModelAndView delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 삭제
		// 넘어온 파라미터 : 글번호, 페이지번호, size [, 검색컬럼, 검색값]
		NoticeDAO dao = new NoticeDAO();
		MyUtil util = new MyUtil();
		FileManager fileManager = new FileManager();
		
		HttpSession session = req.getSession();
		
		// 파일 저장 경로
		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "notice";

		String page = req.getParameter("page");
		String size = req.getParameter("size");
		String query = "page=" + page + "&size=" + size;

		try {
			long num = Long.parseLong(req.getParameter("notice_id"));
			
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

			NoticeDTO dto = dao.findById(num);
			if (dto == null) {
				return new ModelAndView("redirect:/admin/notice/list?" + query);
			}

			// 파일삭제
			List<NoticeDTO> listFile = dao.listNoticeFile(num);
			for (NoticeDTO vo : listFile) {
				fileManager.doFiledelete(pathname, vo.getSave_filename());
			}
			dao.deleteNoticeFile("all", num);

			// 게시글 삭제
			dao.deleteNotice(num);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/admin/notice/list?" + query);
	}
	
	@RequestMapping(value = "/admin/notice/download", method = RequestMethod.GET)
	public void download(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 파일 다운로드
		// 넘어온 파라미터 : 파일번호
		NoticeDAO dao = new NoticeDAO();
		FileManager fileManager = new FileManager();
		
		HttpSession session = req.getSession();
		
		// 파일 저장 경로
		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "notice";
		
		boolean b = false;

		try {
			long fileNum = Long.parseLong(req.getParameter("fileNum"));

			NoticeDTO dto = dao.findByFileId(fileNum);
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
	
	@RequestMapping(value = "/admin/notice/deleteList", method = RequestMethod.POST)
	public ModelAndView deleteList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 게시글다중삭제
		MyUtil util = new MyUtil();
		FileManager fileManager = new FileManager();
		
		HttpSession session = req.getSession();
		
		// 파일 저장 경로
		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "notice";

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

			String[] nn = req.getParameterValues("nums");
			long nums[] = null;
			nums = new long[nn.length];
			for (int i = 0; i < nn.length; i++) {
				nums[i] = Long.parseLong(nn[i]);
			}

			NoticeDAO dao = new NoticeDAO();

			// 파일 삭제
			for (int i = 0; i < nums.length; i++) {
				List<NoticeDTO> listFile = dao.listNoticeFile(nums[i]);
				for (NoticeDTO vo : listFile) {
					fileManager.doFiledelete(pathname, vo.getSave_filename());
				}
				dao.deleteNoticeFile("all", nums[i]);
			}

			// 게시글 삭제
			dao.deleteNotice(nums);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/admin/notice/list?" + query);
	}
	
	
	
	@RequestMapping(value = "/student/notice/list", method = RequestMethod.GET)
	public ModelAndView studentNoticelist(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// (학생)공지사항 리스트
		NoticeDAO dao = new NoticeDAO();
		MyUtil util = new MyUtil();
		
		ModelAndView mav = new ModelAndView("student/notice/list");
		
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
			
			int dataCount,  dataCount2 , total_page;
			
			if(kwd.length() != 0) {
				dataCount = dao.dataCount(schType, kwd);
			} else {
				dataCount = dao.dataCount();
			}
			
			if(kwd.length() != 0) {
				dataCount2 = dao.dataCount2(schType, kwd);
			} else {
				dataCount2 = dao.dataCount2();
			}			
			
			total_page = util.pageCount(dataCount, size);
			
			if(current_page > total_page) {
				current_page = total_page;
			}
			
			int offset = (current_page - 1) * size;
			if(offset < 0) offset = 0;
			
			List<NoticeDTO> list;
			if(kwd.length() != 0) {
				list = dao.listNotice(offset, size, schType, kwd);
			} else {
				list = dao.listNotice(offset, size);
			}
			
			// 공지글
			List<NoticeDTO> listNotice = null;
			if(current_page == 1) {
				listNotice = dao.listNotice();
			}
			
			long gap;
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			LocalDateTime today = LocalDateTime.now();
			for (NoticeDTO dto : list) {
				LocalDateTime dateTime = LocalDateTime.parse(dto.getReg_date(), formatter);
				gap = dateTime.until(today, ChronoUnit.HOURS);
				dto.setGap(gap);

				dto.setReg_date(dto.getReg_date().substring(0, 10));
			}
			
			
			String cp = req.getContextPath();
			String query = "size=" + size;
			String listUrl;
			String articleUrl;
			
			if(kwd.length() != 0) {
				query += "&schType=" + schType + "&kwd=" + util.encodeUrl(kwd);
			}
			listUrl = cp + "/student/notice/list?" + query;
			articleUrl = cp + "/student/notice/article?page=" + current_page + "&" + query;
			
			String paging = util.paging(current_page, total_page, listUrl);
			
			// 포워딩 jsp에 전달할 데이터
			mav.addObject("list", list);
			mav.addObject("listNotice", listNotice);
			mav.addObject("articleUrl", articleUrl);
			mav.addObject("dataCount", dataCount);
			mav.addObject("dataCount2", dataCount2);
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
	
	@RequestMapping(value = "/student/notice/article", method = RequestMethod.GET)
	public ModelAndView studentNoticeArticle(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 학생 공지사항 상세보기
		NoticeDAO dao = new NoticeDAO();
		MyUtil util = new MyUtil();
		
		HttpSession session = req.getSession();
		
		String page = req.getParameter("page");
		String size = req.getParameter("size");
		String query = "page=" + page + "&size=" + size;
		
		try {
			long notice_id = Long.parseLong(req.getParameter("notice_id"));
			
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
	        
	        if (!viewed.contains(notice_id)) {
	            dao.updateHitCount(notice_id); // 조회수 증가
	            viewed.add(notice_id);         // 세션에 조회 기록 저장
	            session.setAttribute("viewedNotice", viewed); // 세션에 다시 저장
	        }
			
			NoticeDTO ndto = dao.findById(notice_id);
			if(ndto == null) {
				return new ModelAndView("redirect:/student/notice/list?" + query);
			}
			
			NoticeDTO prevDto = dao.findByPrev(ndto.getNotice_id(), schType, kwd);
			NoticeDTO nextDto = dao.findByNext(ndto.getNotice_id(), schType, kwd);
			
			List<NoticeDTO> listFile = dao.listNoticeFile(notice_id);
			
			ModelAndView mav = new ModelAndView("student/notice/article");
			
			mav.addObject("ndto", ndto);
			mav.addObject("prevDto", prevDto);
			mav.addObject("nextDto", nextDto);
			mav.addObject("listFile", listFile);
			mav.addObject("query", query);
			mav.addObject("page", page);
			mav.addObject("size", size);
			
			return mav;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("redirect:/student/notice/list?" + query);
	
	}
	
	@RequestMapping(value = "/student/notice/download", method = RequestMethod.GET)
	public void studentNoticDownload(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 파일 다운로드
		// 넘어온 파라미터 : 파일번호
		NoticeDAO dao = new NoticeDAO();
		FileManager fileManager = new FileManager();
		
		HttpSession session = req.getSession();
		
		// 파일 저장 경로
		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "notice";
		
		boolean b = false;

		try {
			long fileNum = Long.parseLong(req.getParameter("fileNum"));

			NoticeDTO dto = dao.findByFileId(fileNum);
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
	
	
	@RequestMapping(value = "/professor/notice/list", method = RequestMethod.GET)
	public ModelAndView profNoticeList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// (교수)공지사항 리스트
		NoticeDAO dao = new NoticeDAO();
		MyUtil util = new MyUtil();
		LectureDAO lectureDao = new LectureDAO();
		
		ModelAndView mav = new ModelAndView("professor/notice/list");
		
		try {
			HttpSession session = req.getSession(false);
			SessionInfo info = (SessionInfo) session.getAttribute("member");
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
			
			int dataCount,  dataCount2 , total_page;
			
			if(kwd.length() != 0) {
				dataCount = dao.dataCount(schType, kwd);
			} else {
				dataCount = dao.dataCount();
			}
			
			if(kwd.length() != 0) {
				dataCount2 = dao.dataCount2(schType, kwd);
			} else {
				dataCount2 = dao.dataCount2();
			}			
			
			total_page = util.pageCount(dataCount, size);
			
			if(current_page > total_page) {
				current_page = total_page;
			}
			
			int offset = (current_page - 1) * size;
			if(offset < 0) offset = 0;
			
			List<NoticeDTO> list;
			if(kwd.length() != 0) {
				list = dao.listNotice(offset, size, schType, kwd);
			} else {
				list = dao.listNotice(offset, size);
			}
			
			// 공지글
			List<NoticeDTO> listNotice = null;
			if(current_page == 1) {
				listNotice = dao.listNotice();
			}
			
			long gap;
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			LocalDateTime today = LocalDateTime.now();
			for (NoticeDTO dto : list) {
				LocalDateTime dateTime = LocalDateTime.parse(dto.getReg_date(), formatter);
				gap = dateTime.until(today, ChronoUnit.HOURS);
				dto.setGap(gap);

				dto.setReg_date(dto.getReg_date().substring(0, 10));
			}
			
			
			String cp = req.getContextPath();
			String query = "size=" + size;
			String listUrl;
			String articleUrl;
			
			if(kwd.length() != 0) {
				query += "&schType=" + schType + "&kwd=" + util.encodeUrl(kwd);
			}
			listUrl = cp + "/professor/notice/list?" + query;
			articleUrl = cp + "/professor/notice/article?page=" + current_page + "&" + query;
			
			String paging = util.paging(current_page, total_page, listUrl);
			
			if (info != null) {
                String memberId = String.valueOf(info.getMember_id());
                List<LectureDTO> lectures = lectureDao.listsidebar(memberId);
                mav.addObject("lectureList", lectures);
            }
			
			// 포워딩 jsp에 전달할 데이터
			mav.addObject("list", list);
			mav.addObject("listNotice", listNotice);
			mav.addObject("articleUrl", articleUrl);
			mav.addObject("dataCount", dataCount);
			mav.addObject("dataCount2", dataCount2);
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
	
	@RequestMapping(value = "/professor/notice/article", method = RequestMethod.GET)
	public ModelAndView profNoticeArticle(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글보기
		NoticeDAO dao = new NoticeDAO();
		MyUtil util = new MyUtil();
		LectureDAO lectureDao = new LectureDAO();
		
		HttpSession session = req.getSession();
		
		String page = req.getParameter("page");
		String size = req.getParameter("size");
		String query = "page=" + page + "&size=" + size;
		
		try {
			SessionInfo info = (SessionInfo) session.getAttribute("member");
			long notice_id = Long.parseLong(req.getParameter("notice_id"));
			
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
	        
	        if (!viewed.contains(notice_id)) {
	            dao.updateHitCount(notice_id); // 조회수 증가
	            viewed.add(notice_id);         // 세션에 조회 기록 저장
	            session.setAttribute("viewedNotice", viewed); // 세션에 다시 저장
	        }
			
			
			NoticeDTO ndto = dao.findById(notice_id);
			if(ndto == null) {
				return new ModelAndView("redirect:/professor/notice/list?" + query);
			}
			
			NoticeDTO prevDto = dao.findByPrev(ndto.getNotice_id(), schType, kwd);
			NoticeDTO nextDto = dao.findByNext(ndto.getNotice_id(), schType, kwd);
			
			List<NoticeDTO> listFile = dao.listNoticeFile(notice_id);
			
			ModelAndView mav = new ModelAndView("professor/notice/article");
			
			if (info != null) {
                String memberId = String.valueOf(info.getMember_id());
                List<LectureDTO> lectures = lectureDao.listsidebar(memberId);
                mav.addObject("lectureList", lectures);
            }
			
			mav.addObject("ndto", ndto);
			mav.addObject("prevDto", prevDto);
			mav.addObject("nextDto", nextDto);
			mav.addObject("listFile", listFile);
			mav.addObject("query", query);
			mav.addObject("page", page);
			mav.addObject("size", size);
			
			return mav;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("redirect:/professor/notice/list?" + query);
	
	}
	
	@RequestMapping(value = "/professor/notice/download", method = RequestMethod.GET)
	public void profNoticeDownload(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 파일 다운로드
		// 넘어온 파라미터 : 파일번호
		NoticeDAO dao = new NoticeDAO();
		FileManager fileManager = new FileManager();
		
		HttpSession session = req.getSession();
		
		// 파일 저장 경로
		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "notice";
		
		boolean b = false;

		try {
			long fileNum = Long.parseLong(req.getParameter("fileNum"));

			NoticeDTO dto = dao.findByFileId(fileNum);
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
	
	
	
	
}
