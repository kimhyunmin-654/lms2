package com.lms2.controller;

import java.io.IOException;
import java.util.List;

import com.lms2.dao.AdminDAO;
import com.lms2.dao.DataDAO;
import com.lms2.dao.LectureDAO;
import com.lms2.dao.NoticeDAO;
import com.lms2.dao.ProfessorDAO;
import com.lms2.model.DataDTO;
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

	// 교수 목록 [관리자]
	@RequestMapping(value = "/admin/professor/list", method = RequestMethod.GET)
	public ModelAndView list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("admin/professor/list");
		ProfessorDAO dao = new ProfessorDAO();
		MyUtil util = new MyUtil();
	
		 try {
			 String page = req.getParameter("page");
				int current_page = 1;
				if(page != null) {
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
				
				int dataCount, total_page;

				dataCount = dao.dataCount();
				
				total_page = util.pageCount(dataCount, size);
				
				if(current_page > total_page) {
					current_page = total_page;
				}
		
				int offset = (current_page - 1) * size;
				if(offset < 0) offset = 0;
				
				List<ProfessorDTO> list;
				if(kwd.length() != 0) {
					list = dao.listProfessor(offset, size, schType, kwd);
				} else {
					list = dao.listProfessor(offset, size);
				}
				
				
				String cp = req.getContextPath();
				String query = "size=" + size;
				String listUrl;
				String articleUrl;
				
				if(kwd.length() != 0) {
					query += "&schType=" + schType + "&kwd=" + util.encodeUrl(kwd);
				}
				listUrl = cp + "/admin/admin/list?" + query;
				articleUrl = cp + "/admin/admin/article?page=" + current_page + "&" + query;
				
				String paging = util.paging(current_page, total_page, listUrl);
				
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
	
	// 교수 등록 폼
	@RequestMapping(value = "/admin/professor/write", method = RequestMethod.GET)
	public ModelAndView wirteForm(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("admin/professor/write");
		mav.addObject("mode", "write");
		return mav;
	}
	
	// 교수 등록
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
	
	// 교수 메인페이지
	@RequestMapping(value = "/professor/main/main", method = RequestMethod.GET)
	public ModelAndView main(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		NoticeDAO noticeDao = new NoticeDAO();
		ProfessorDAO dao = new ProfessorDAO();
		ModelAndView mav = new ModelAndView("professor/main/main");

		try {
			// 강의목록
			List<LectureDTO> list = dao.plistLecture(info.getMember_id());
			mav.addObject("list", list);
			
			// 공지사항
			List<NoticeDTO> listNotice = noticeDao.listNotice();
			mav.addObject("listNotice", listNotice);
			

		} catch (Exception e) {
			e.printStackTrace();
		}

		return mav;
	}
	
	// 강의 목록
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
	    NoticeDAO noticeDao = new NoticeDAO();
	    DataDAO dataDao = new DataDAO();

	    String page = req.getParameter("page");
	    String size = req.getParameter("size");
	    String query = "page=" + page + "&size=" + size;

	    if (page == null) page = "1";
	    if (size == null) size = "10";


	    try {
	        String lecture_code = req.getParameter("lecture_code");

	        ModelAndView mav = new ModelAndView("professor/lecture/main1");

	        // 강의 정보 조회
	        LectureDTO dto = dao.findById1(lecture_code);

	        // 공지사항 목록 조회
	        List<NoticeDTO> listNotice = noticeDao.listNotice();
	        mav.addObject("listNotice", listNotice);

	        // 자료실 목록 조회 
	        List<DataDTO> listData = dataDao.listData(lecture_code); 
	        mav.addObject("listData", listData);

	        // 교수의 강의 목록 조회 (사이드바)
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
