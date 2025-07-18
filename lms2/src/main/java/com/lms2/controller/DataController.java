package com.lms2.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import com.lms2.dao.DataDAO;
import com.lms2.dao.LectureDAO;
import com.lms2.model.DataDTO;
import com.lms2.model.LectureDTO;
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
public class DataController {

	@RequestMapping(value = "/professor/bbs/list", method = RequestMethod.GET)
	public ModelAndView pList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// (교수) 자료실 리스트
		ModelAndView mav = new ModelAndView("/professor/bbs/list");

		DataDAO dao = new DataDAO();
		LectureDAO lectureDao = new LectureDAO();
		MyUtil util = new MyUtil();
		
		try {
			HttpSession session = req.getSession(false);
			SessionInfo info = (SessionInfo) session.getAttribute("member");
			if (info != null) {
				String memberId = String.valueOf(info.getMember_id());
				List<LectureDTO> lectures = lectureDao.listsidebar(memberId);
				mav.addObject("lectureList", lectures);
			}
			
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

			String query = "";
			if (kwd.length() != 0) {
				query = "schType=" + schType + "&kwd=" + util.encodeUrl(kwd);
			}

			// lecture_code 넘기기
			String lecture_code = req.getParameter("lecture_code");
			if (lecture_code != null) {
				if (!query.isEmpty()) {
					query += "&";
				}
				query += "lecture_code=" + lecture_code;
			}

			int dataCount;
			if (kwd.length() == 0) {
				dataCount = dao.dataCount(lecture_code);
			} else {
				dataCount = dao.dataCount(schType, kwd, lecture_code);
			}

			int size = 10;
			int total_page = util.pageCount(dataCount, size);
			if (current_page > total_page) {
				current_page = total_page;
			}

			int offset = (current_page - 1) * size;
			if (offset < 0)
				offset = 0;

			List<DataDTO> list = null;
			if (kwd.length() == 0) {
				list = dao.listData(offset, size, lecture_code);
			} else {
				list = dao.listData(offset, size, schType, kwd, lecture_code);
			}

			String cp = req.getContextPath();
			String listUrl = cp + "/professor/bbs/list";
			String articleUrl = cp + "/professor/bbs/article?page=" + current_page;
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
			mav.addObject("lecture_code", lecture_code);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}

	@RequestMapping(value = "/student/bbs/list", method = RequestMethod.GET)
	public ModelAndView sList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// (학생) 자료실 리스트
		ModelAndView mav = new ModelAndView("/student/bbs/list");

		DataDAO dao = new DataDAO();
		LectureDAO lectureDao = new LectureDAO();
		MyUtil util = new MyUtil();

		try {
			HttpSession session = req.getSession(false);
			SessionInfo info = (SessionInfo) session.getAttribute("member");
			if (info != null) {
				String memberId = String.valueOf(info.getMember_id());
				List<LectureDTO> lectures = lectureDao.listsidebar(memberId);
				mav.addObject("lectureList", lectures);
			}
			
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

			String query = "";
			if (kwd.length() != 0) {
				query = "schType=" + schType + "&kwd=" + util.encodeUrl(kwd);
			}
			
			// lecture_code 넘기기
			String lecture_code = req.getParameter("lecture_code");
			if (lecture_code != null) {
				if (!query.isEmpty()) {
					query += "&";
				}
				query += "lecture_code=" + lecture_code;
			}			
			
			int dataCount;
			if (kwd.length() == 0) {
				dataCount = dao.dataCount(lecture_code);
			} else {
				dataCount = dao.dataCount(schType, kwd, lecture_code);
			}

			int size = 10;
			int total_page = util.pageCount(dataCount, size);
			if (current_page > total_page) {
				current_page = total_page;
			}

			int offset = (current_page - 1) * size;
			if (offset < 0)
				offset = 0;

			
			List<DataDTO> list = null;
			if (kwd.length() == 0) {
				list = dao.listData(offset, size, lecture_code);
			} else {
				list = dao.listData(offset, size, schType, kwd, lecture_code);
			}

			String cp = req.getContextPath();
			String listUrl = cp + "/student/bbs/list";
			String articleUrl = cp + "/student/bbs/article?page=" + current_page;
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
			mav.addObject("lecture_code", lecture_code);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}

	@RequestMapping(value = "/professor/bbs/write", method = RequestMethod.GET)
	public ModelAndView writeForm(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// (교수) 자료실 글 쓰기
		ModelAndView mav = new ModelAndView("/professor/bbs/write");
		mav.addObject("mode", "write");
		LectureDAO lectureDao = new LectureDAO();
		
		try {
			HttpSession session = req.getSession();
			SessionInfo info = (SessionInfo) session.getAttribute("member");
			if (info != null) {
				String memberId = String.valueOf(info.getMember_id());
				List<LectureDTO> lectures = lectureDao.listsidebar(memberId);
				mav.addObject("lectureList", lectures);
			}

			String lecture_code = req.getParameter("lecture_code");
			
			DataDAO dao = new DataDAO();
			List<DataDTO> lectureList = dao.listLectureByMember(info.getMember_id(), lecture_code);
			mav.addObject("bbsLectureList", lectureList);
			mav.addObject("lecture_code", lecture_code);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}

	@RequestMapping(value = "/professor/bbs/write", method = RequestMethod.POST)
	public ModelAndView writeSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    DataDAO dao = new DataDAO();
	    FileManager fileManager = new FileManager();
	    
	    HttpSession session = req.getSession();
	    SessionInfo info = (SessionInfo) session.getAttribute("member");
	    
	    String root = session.getServletContext().getRealPath("/");
	    String pathname = root + "uploads" + File.separator + "lecture";
	    String lecture_code = req.getParameter("lecture_code");
	   

	    try {
	        Part p = req.getPart("selectFile"); 
	        
	        DataDTO dto = new DataDTO();
	        dto.setMember_id(info.getMember_id());
	        dto.setSubject(req.getParameter("subject"));
	        dto.setContent(req.getParameter("content"));
	        dto.setLecture_code(req.getParameter("lecture_code"));

	        MyMultipartFile multiFile = fileManager.doFileUpload(p, pathname);
	        if (multiFile != null) {
	            dto.setSave_filename(multiFile.getSaveFilename());
	            dto.setOriginal_filename(multiFile.getOriginalFilename());
	        }

	        dao.insertData(dto);

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return new ModelAndView("redirect:/professor/bbs/list?lecture_code=" + lecture_code);
	}

	@RequestMapping(value = "/professor/bbs/article", method = RequestMethod.GET)
	public ModelAndView pArticle(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// (교수) 자료실 글보기
		DataDAO dao = new DataDAO();
		MyUtil util = new MyUtil();

		String page = req.getParameter("page");
		String schType = req.getParameter("schType");
		String kwd = req.getParameter("kwd");
		String lecture_code = req.getParameter("lecture_code");
		
		if (schType == null) {
			schType = "all";
			kwd = "";
		}
		kwd = util.decodeUrl(kwd);

		String query = "page=" + page;
	    if (lecture_code != null && !lecture_code.isEmpty()) {
	        query += "&lecture_code=" + lecture_code;
	    }
	    if (!kwd.isEmpty()) {
	        query += "&schType=" + schType + "&kwd=" + util.encodeUrl(kwd);
	    }

		try {
			int data_id = Integer.parseInt(req.getParameter("num"));
			
			// 조회수 증가
			dao.updateHitCount(data_id);

			// 게시물 가져오기
			DataDTO dto = dao.findById(data_id);

			if (dto == null) {
				return new ModelAndView("redirect:/professor/bbs/list?" + query);
			}
			dto.setContent(util.htmlSymbols(dto.getContent()));

			// 이전글 다음글
			DataDTO prevDto = dao.findByPrev(dto.getData_id(), schType, kwd);
			DataDTO nextDto = dao.findByNext(dto.getData_id(), schType, kwd);

			ModelAndView mav = new ModelAndView("/professor/bbs/article");
			mav.addObject("dto", dto);
			mav.addObject("page", page);
			mav.addObject("query", query);
			mav.addObject("prevDto", prevDto);
			mav.addObject("nextDto", nextDto);
			mav.addObject("lecture_code", lecture_code);

			return mav;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/professor/bbs/list?" + query);
	}

	@RequestMapping(value = "/student/bbs/article", method = RequestMethod.GET)
	public ModelAndView sArticle(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// (학생) 자료실 글보기
		DataDAO dao = new DataDAO();
		MyUtil util = new MyUtil();

		String page = req.getParameter("page");
		String query = "page=" + page;

		try {
			int data_id = Integer.parseInt(req.getParameter("num"));
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
			
			String lecture_code = req.getParameter("lecture_code");
			if (lecture_code != null) {
				if (!query.isEmpty()) {
					query += "&";
				}
				query += "lecture_code=" + lecture_code;
			}

			// 조회수 증가
			dao.updateHitCount(data_id);

			// 게시물가져오기
			DataDTO dto = dao.findById(data_id);

			if (dto == null) {
				return new ModelAndView("redirect:/student/bbs/list?" + query);
			}
			dto.setContent(util.htmlSymbols(dto.getContent()));

			// 이전글 다음글
			DataDTO prevDto = dao.findByPrev(dto.getData_id(), schType, kwd);
			DataDTO nextDto = dao.findByNext(dto.getData_id(), schType, kwd);

			ModelAndView mav = new ModelAndView("/student/bbs/article");
			mav.addObject("dto", dto);
			mav.addObject("page", page);
			mav.addObject("query", query);
			mav.addObject("prevDto", prevDto);
			mav.addObject("nextDto", nextDto);
			mav.addObject("lecture_code", lecture_code);

			return mav;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/student/bbs/list?" + query);
	}

	@RequestMapping(value = "/professor/bbs/update", method = RequestMethod.GET)
	public ModelAndView updateForm(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 자료실 글수정 폼
		DataDAO dao = new DataDAO();
		String page = req.getParameter("page");
		
		try {
			int data_id = Integer.parseInt(req.getParameter("num"));
			DataDTO dto = dao.findById(data_id);
			
			if (dto == null) {
				return new ModelAndView("redirect:/professor/bbs/list?page=" + page);
			}
			
			ModelAndView mav = new ModelAndView("/professor/bbs/write");
			
			HttpSession session = req.getSession();
			SessionInfo info = (SessionInfo) session.getAttribute("member");
			
			String lecture_code = req.getParameter("lecture_code");

			List<DataDTO> lectureList = dao.listLectureByMember(info.getMember_id(), lecture_code);
			mav.addObject("lectureList", lectureList);
			mav.addObject("lecture_code", lecture_code);
			
			mav.addObject("dto", dto);
			mav.addObject("page", page);
			mav.addObject("mode", "update");
			
			return mav;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/professor/bbs/list?page=" + page);
	}

	@RequestMapping(value = "/professor/bbs/update", method = RequestMethod.POST)
	public ModelAndView updateSubmit(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 자료실 글수정 완료
		DataDAO dao = new DataDAO();

		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		FileManager fileManager = new FileManager();
		
		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "bbs";
		
		String page = req.getParameter("page");
		String lecture_code = req.getParameter("lecture_code");

		try {
			DataDTO dto = new DataDTO();

			dto.setData_id(Integer.parseInt(req.getParameter("num")));
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));
			dto.setSave_filename(req.getParameter("save_filename"));
			dto.setOriginal_filename(req.getParameter("original_filename"));
			dto.setLecture_code(req.getParameter("lecture_code"));

			dto.setMember_id(info.getMember_id());

			Part p = req.getPart("select_file");
			MyMultipartFile multiFile = fileManager.doFileUpload(p, pathname);
			if (multiFile != null) {
				if (req.getParameter("save_filename").length() != 0) {
					fileManager.doFiledelete(pathname, req.getParameter("save_filename"));
				}
				
				String save_filename = multiFile.getSaveFilename();
				String original_filename = multiFile.getOriginalFilename();
				dto.setSave_filename(save_filename);
				dto.setOriginal_filename(original_filename);
			}
			
			dao.updateData(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/professor/bbs/list?page=" + page + "&lecture_code=" + lecture_code);
	}

	@RequestMapping(value = "/professor/bbs/delete", method = RequestMethod.GET)
	public ModelAndView delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 자료실 글삭제
		DataDAO dao = new DataDAO();
		MyUtil util = new MyUtil();

		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		String page = req.getParameter("page");
		String lecture_code = req.getParameter("lecture_code");
		String query = "page=" + page + "&lecture_code=" + lecture_code;
		

		try {
			int data_id = Integer.parseInt(req.getParameter("num"));

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

			dao.deleteData(data_id, info.getMember_id());

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/professor/bbs/list?" + query);
	}

/*	@RequestMapping(value = "/professor/bbs/listReply", method = RequestMethod.GET)
	public ModelAndView listComment(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 리플리스트
		DataDAO dao = new DataDAO();
		MyUtil util = new MyUtil();

		try {
			int data_id = Integer.parseInt(req.getParameter("num"));
			String pageNo = req.getParameter("pageNo");
			int current_page = 1;
			if (pageNo != null) {
				current_page = Integer.parseInt(pageNo);
			}
			int size = 5;
			int total_page = 0;
			int commentCount = 0;

			commentCount = dao.dataCountComment(data_id);
			total_page = util.pageCount(commentCount, size);
			if (current_page > total_page) {
				current_page = total_page;
			}

			int offset = (current_page - 1) * size;
			if (offset < 0)
				offset = 0;

			List<Data_CommentDTO> listComment = dao.listComment(data_id, offset, size);

			for (Data_CommentDTO dto : listComment) {
				dto.setContent(dto.getContent().replaceAll("\n", "<br>"));

			}
			String paging = util.pagingMethod(current_page, total_page, "listpage");

			ModelAndView mav = new ModelAndView("data/listComment");

			mav.addObject("listComment", listComment);
			mav.addObject("pageNo", pageNo);
			mav.addObject("commentCount", commentCount);
			mav.addObject("total_page", total_page);
			mav.addObject("paging", paging);

			return mav;

		} catch (Exception e) {
			e.printStackTrace();
			resp.sendError(406);
			throw e;
		}
	}

	@ResponseBody
	@RequestMapping(value = "/data/insertReply", method = RequestMethod.POST)
	public Map<String, Object> insertComment(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Map<String, Object> model = new HashMap<String, Object>();
		// 리플 등록

		DataDAO dao = new DataDAO();

		String state = "false";

		try {
			Data_CommentDTO dto = new Data_CommentDTO();

			int data_id = Integer.parseInt(req.getParameter("num"));
			dto.setData_id(data_id);
			dto.setContent(req.getParameter("content"));
			String parent_comment_id = req.getParameter("parent_comment_id");
			if (parent_comment_id != null) {
				dto.setParent_comment_id(Integer.parseInt(parent_comment_id));
			}

			dao.insertComment(dto);
			state = "true";
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.put("state", state);

		return model;
	}

	@ResponseBody
	@RequestMapping(value = "/data/deleteReply", method = RequestMethod.POST)
	public Map<String, Object> deleteComment(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Map<String, Object> model = new HashMap<String, Object>();
		// 리플 삭제

		return model;
	}
*/
	
	@RequestMapping(value = "/lecture/download")
	public void download(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 파일 다운로드
		DataDAO dao = new DataDAO();
		FileManager fileManager = new FileManager();

		HttpSession session = req.getSession();

		// 파일 저장 경로
		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "lecture";

		boolean b = false;

		try {
			int data_id = Integer.parseInt(req.getParameter("num"));

			DataDTO dto = dao.findById(data_id);
			if (dto != null) {
				b = fileManager.doFiledownload(dto.getSave_filename(), dto.getOriginal_filename(), pathname, resp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (!b) {
			resp.setContentType("text/html;charset=utf-8");
			PrintWriter out = resp.getWriter();
			out.print("<script>alert('파일다운로드가 실패 했습니다.');history.back();</script>");
		}
	}

}