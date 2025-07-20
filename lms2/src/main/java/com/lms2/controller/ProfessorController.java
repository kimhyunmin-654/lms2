package com.lms2.controller;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lms2.dao.AdminDAO;
import com.lms2.dao.DataDAO;
import com.lms2.dao.LectureDAO;
import com.lms2.dao.NoticeDAO;
import com.lms2.dao.ProfessorDAO;
import com.lms2.model.DataDTO;
import com.lms2.model.DeparmentDTO;
import com.lms2.model.LectureDTO;
import com.lms2.model.MemberDTO;
import com.lms2.model.NoticeDTO;
import com.lms2.model.ProfessorDTO;
import com.lms2.model.SessionInfo;
import com.lms2.mvc.annotation.Controller;
import com.lms2.mvc.annotation.RequestMapping;
import com.lms2.mvc.annotation.RequestMethod;
import com.lms2.mvc.annotation.ResponseBody;
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
				listUrl = cp + "/admin/professor/list?" + query;
				articleUrl = cp + "/admin/professor/article?page=" + current_page + "&" + query;
				
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
		ProfessorDAO professorDao = new ProfessorDAO();
		
		 List<DeparmentDTO> listDepartment = professorDao.listDepartment();
		    
		 mav.addObject("listDepartment", listDepartment);
		 mav.addObject("mode", "write");
		    
		 return mav;
	}
	
	// 교수 등록
	@RequestMapping(value = "/admin/professor/write", method = RequestMethod.POST)
	public ModelAndView accountAdminSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 관리자 등록
		ProfessorDAO dao = new ProfessorDAO();
		FileManager fileManager = new FileManager();
		
		HttpSession session = req.getSession();
		
		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "member";
		
		String message = "";
		
		try {
			ProfessorDTO dto = new ProfessorDTO();
			
			dto.setMember_id(req.getParameter("member_id"));
			dto.setName(req.getParameter("name"));
			dto.setPassword(req.getParameter("password"));
			String e1 = req.getParameter("email1");
			String e2 = req.getParameter("email2");
			dto.setEmail(e1 + "@" + e2);
			dto.setPhone(req.getParameter("phone"));
			dto.setBirth(req.getParameter("birth"));
			dto.setAddr1(req.getParameter("addr1"));
			dto.setAddr2(req.getParameter("addr2"));
			dto.setZip(req.getParameter("zip"));
			
			dto.setPosition(req.getParameter("position"));
			dto.setDepartment_id(req.getParameter("department_id"));
			
			Part P = req.getPart("selectFile");
			MyMultipartFile multiPart = fileManager.doFileUpload(P, pathname);
			if(multiPart != null) {
				dto.setAvatar(multiPart.getSaveFilename());
			}
			
			dao.insertProfessor(dto);
			
			session.setAttribute("mode", "write");
			
			return new ModelAndView("redirect:/admin/professor/list");
			
		} catch (SQLException e) {
			if(e.getErrorCode() == 1) {
				message = "이미 등록된 교번입니다.";
			} else if(e.getErrorCode() == 1840 || e.getErrorCode() == 1861) {
				message = "날짜 형식이 올바르지 않습니다.";
			} else {
				message = "교수 등록에 실패했습니다.";
			}
		} catch (Exception e) {
			message = "교수 등록에 실패했습니다.";
			e.printStackTrace();
		}
		
		ModelAndView mav = new ModelAndView("admin/professor/write");
		mav.addObject("mode", "write");
		mav.addObject("message", message);
		
		return mav;
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
	
	
	@RequestMapping(value = "/admin/professor/article", method = RequestMethod.GET)
	public ModelAndView article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 관리자 상세보기
		ProfessorDAO dao = new ProfessorDAO();;
		MyUtil util = new MyUtil();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		String page = req.getParameter("page");
		String size = req.getParameter("size");
		String query = "page=" + page + "&size=" + size;
		
		try {
			String member_id = req.getParameter("member_id");
			
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
			
			ProfessorDTO dto = dao.findById(member_id);
			if(dto == null) {
				return new ModelAndView("redirect:/admin/professor/list?" + query); 
			}
			
			ModelAndView mav = new ModelAndView("admin/professor/article");
			
			mav.addObject("dto", dto);
			mav.addObject("query", query);
			mav.addObject("page", page);
			mav.addObject("size", size);
			
			return mav;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("redirect:/admin/professor/list?" + query);
	}
	
	@ResponseBody // Map을 JSON 형식의 문자열로 변환하여 응답
	@RequestMapping(value = "/admin/professor/userIdCheck", method = RequestMethod.POST)
	public Map<String, Object> memberIdCheck(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Map<String, Object> model = new HashMap<String, Object>();
		
		AdminDAO dao = new AdminDAO();
		
		String member_id = req.getParameter("member_id");
		MemberDTO dto = dao.findById(member_id);
		
		String passed = "false";
		if(dto == null) {
			passed = "true";
		}
		
		model.put("passed", passed);
		
		return model;
		
	}
	
	@RequestMapping(value = "/admin/professor/pwd", method = RequestMethod.GET)
	public ModelAndView pwdForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 패스워드 확인 폼(정보수정)
		ModelAndView mav = new ModelAndView("admin/professor/pwd");
		
		String mode = req.getParameter("mode");
		
		mav.addObject("mode", mode);
		
		return mav;
	}
	
	@RequestMapping(value = "/admin/professor/pwd", method = RequestMethod.POST)
	public ModelAndView pwdSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 패스워드 확인
		AdminDAO dao = new AdminDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		try {
			MemberDTO dto = dao.findById(info.getMember_id());
			if(dto == null) {
				session.invalidate();
				return new ModelAndView("redirect:/admin/professor/list");
			}
			
			String password = req.getParameter("password");
			String mode = req.getParameter("mode");
			
			if(! dto.getPassword().equals(password)) {
				ModelAndView mav = new ModelAndView("admin/professor/pwd");
				
				mav.addObject("mode", mode);
				mav.addObject("message", "패스워드가 일치하지 않습니다.");
				
				return mav;
			}
			
			// 정보수정 화면
			ModelAndView mav = new ModelAndView("admin/professor/write");
			
			mav.addObject("dto", dto);
			mav.addObject("mode", "update");
			
			return mav;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ModelAndView("redirect:/admin/professor/list");
	}
	
	// 수정 폼
	@RequestMapping(value = "/admin/professor/update", method = RequestMethod.GET)
	public ModelAndView updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    String member_id = req.getParameter("member_id");
	    
	    ProfessorDAO dao = new ProfessorDAO();
	    ProfessorDTO dto = dao.findById(member_id);
	    
	    List<DeparmentDTO> listDepartment = dao.listDepartment();

	    if (dto == null) {
	        return new ModelAndView("redirect:/admin/professor/list");
	    }
	    
	    ModelAndView mav = new ModelAndView("admin/professor/write");
	    mav.addObject("dto", dto);
	    
	    mav.addObject("mode", "update");
	    mav.addObject("listDepartment", listDepartment);
	    mav.addObject("page", req.getParameter("page"));
	    mav.addObject("size", req.getParameter("size"));

	    return mav;
	}
	
	@RequestMapping(value = "/admin/professor/update", method = RequestMethod.POST)
	public ModelAndView updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 개인정보수정완료
		ProfessorDAO dao = new ProfessorDAO();
		FileManager fileManager = new FileManager();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "member";
		
		try {
			ProfessorDTO dto = new ProfessorDTO();
			
			dto.setMember_id(req.getParameter("member_id"));
			dto.setName(req.getParameter("name"));
			dto.setPassword(req.getParameter("password"));
			
			dto.setBirth(req.getParameter("birth"));
			String e1 = req.getParameter("email1");
			String e2 = req.getParameter("email2");
			dto.setEmail(e1 + "@" + e2);
			dto.setPhone(req.getParameter("phone"));
			dto.setZip(req.getParameter("zip"));
			dto.setAddr1(req.getParameter("addr1"));
			dto.setAddr2(req.getParameter("addr2"));
			dto.setPosition(req.getParameter("position"));
			
			 String departmentId = req.getParameter("department_id");
		        if (departmentId == null || departmentId.trim().isEmpty()) {
		            throw new ServletException("학과는 필수 항목입니다.");
		        }
			dto.setDepartment_id(req.getParameter("department_id"));
			dto.setDivision(req.getParameter("division"));
			
			dto.setAvatar(req.getParameter("avatar"));
			Part p = req.getPart("selectFile");
			MyMultipartFile multilPart = fileManager.doFileUpload(p, pathname);
			if(multilPart != null) {
				// 기존 사진 삭제
				if(dto.getAvatar().length() != 0) {
					fileManager.doFiledelete(pathname, dto.getAvatar());
				}
				
				dto.setAvatar(multilPart.getSaveFilename());
			}
			
			dao.updateProfessor(dto);
			
			 if (info.getMember_id().equals(dto.getMember_id())) {
		            info.setAvatar(dto.getAvatar());
		            info.setName(dto.getName());
		            info.setDivision(dto.getDivision());
		            session.setAttribute("member", info);
		        }
				
				
				return new ModelAndView("redirect:/admin/professor/list");
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return new ModelAndView("redirect:/admin/professor/list");
			
		}
		
	
	@ResponseBody
	@RequestMapping(value = "/admin/professor/deleteAvatar", method = RequestMethod.POST)
	public Map<String, Object> deleteAvatar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 프로파일 포토 삭제 - AJAX
		Map<String, Object> model = new HashMap<String, Object>();
		
		AdminDAO dao = new AdminDAO();
		FileManager fileManager = new FileManager();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "member";
		
		String state = "false";
		
		try {
			String avatar = req.getParameter("avatar");
			if(avatar != null && avatar.length() != 0) {
				fileManager.doFiledelete(pathname, avatar);
				
				dao.deleteAvatar(info.getMember_id());
				
				info.setAvatar("");
				state = "true";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		model.put("state", state);
		
		return model;
	}
	
	@RequestMapping(value = "/admin/professor/delete", method = RequestMethod.GET)
	public ModelAndView delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 관리자 삭제
	    ProfessorDAO dao = new ProfessorDAO();
	    FileManager fileManager = new FileManager();
	    MyUtil util = new MyUtil();

	    HttpSession session = req.getSession();

	    String root = session.getServletContext().getRealPath("/");
	    String pathname = root + "uploads" + File.separator + "member";

	    String page = req.getParameter("page");
	    String size = req.getParameter("size");
	    String query = "page=" + page + "&size=" + size;

	    try {
	        String member_id = req.getParameter("member_id");

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

	        ProfessorDTO dto = dao.findById(member_id);
	        if (dto == null) {
	            return new ModelAndView("redirect:/admin/professor/list?" + query);
	        }

	        // 프로필 이미지 삭제
	        if (dto.getAvatar() != null && !dto.getAvatar().equals("")) {
	            fileManager.doFiledelete(pathname, dto.getAvatar());
	        }

	        dao. deleteProfessor(member_id);

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return new ModelAndView("redirect:/admin/professor/list?" + query);
	}
	
	
	

	
	
}
