package com.lms2.controller;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.crypto.Data;

import com.lms2.dao.AdminDAO;
import com.lms2.dao.DataDAO;
import com.lms2.dao.LectureDAO;
import com.lms2.dao.NoticeDAO;
import com.lms2.dao.Pro_hwDAO;
import com.lms2.dao.StudentDAO;
import com.lms2.model.Course_ApplicationDTO;
import com.lms2.model.DataDTO;
import com.lms2.model.DepartmentDTO;
import com.lms2.model.LectureDTO;
import com.lms2.model.MemberDTO;
import com.lms2.model.NoticeDTO;
import com.lms2.model.Pro_hwDTO;
import com.lms2.model.SessionInfo;
import com.lms2.model.StudentDTO;
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
public class StudentController {

	// 학생 메인 페이지
	@RequestMapping(value = "/student/main/main", method = RequestMethod.GET)
	public ModelAndView main(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		StudentDAO dao = new StudentDAO();
		NoticeDAO noticeDao = new NoticeDAO();
		ModelAndView mav = new ModelAndView("student/main/main");

		try {
			// 수강 과목
			List<Course_ApplicationDTO> list = dao.listCourse(info.getMember_id());
			mav.addObject("list", list);

			// 공지사항
			List<NoticeDTO> listNotice = noticeDao.listNotice(0,5);
			mav.addObject("listNotice", listNotice);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return mav;
	}

	// 학생등록 GET
	@RequestMapping(value = "/admin/student/account", method = RequestMethod.GET)
	public ModelAndView accountStudentForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		StudentDAO dao = new StudentDAO();
		ModelAndView mav = new ModelAndView("admin/student/account");

		mav.addObject("dto", new StudentDTO());
		mav.addObject("mode", "account");

		try {
			mav.addObject("departmentList", dao.listDepartment());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return mav;
	}

	// 학생 등록 POST
	@RequestMapping(value = "/admin/student/account", method = RequestMethod.POST)
	public ModelAndView accountStudentSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		StudentDAO dao = new StudentDAO();
		HttpSession session = req.getSession();
		String message = "";

		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "avatar";

		FileManager fileManager = new FileManager();
		String avatar = null;

		try {
			Part part = req.getPart("selectFile");
			MyMultipartFile multiFile = fileManager.doFileUpload(part, pathname);
			if (multiFile != null) {
				avatar = multiFile.getSaveFilename();
			}

			StudentDTO dto = new StudentDTO();

			dto.setMember_id(req.getParameter("member_id"));
			dto.setName(req.getParameter("name"));
			dto.setPassword(req.getParameter("password"));
			dto.setEmail(req.getParameter("email1") + "@" + req.getParameter("email2"));
			dto.setPhone(req.getParameter("phone"));
			dto.setBirth(req.getParameter("birth"));
			dto.setAddr1(req.getParameter("addr1"));
			dto.setAddr2(req.getParameter("addr2"));
			dto.setGrade(Integer.parseInt(req.getParameter("grade")));
			dto.setAdmission_date(req.getParameter("admission_date"));
			dto.setDepartment_id(req.getParameter("department_id"));
			dto.setAvatar(avatar);

			dao.insertStudent(dto);

			session.setAttribute("mode", "account");
			return new ModelAndView("redirect:/admin/student/list");

		} catch (SQLException e) {
			if (e.getErrorCode() == 1) {
				message = "이미 등록된 학번입니다.";
			} else if (e.getErrorCode() == 1840 || e.getErrorCode() == 1861) {
				message = "날짜 형식이 올바르지 않습니다.";
			} else {
				message = "학생 등록에 실패했습니다.";
			}
		} catch (Exception e) {
			message = "학생 등록에 실패했습니다.";
			e.printStackTrace();
		}

		ModelAndView mav = new ModelAndView("admin/student/account");
		mav.addObject("mode", "account");
		mav.addObject("message", message);

		return mav;
	}

	// 학생 등록 완료 후
	@RequestMapping(value = "/admin/student/complete", method = RequestMethod.GET)
	public ModelAndView studentComplete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();

		String mode = (String) session.getAttribute("mode");
		String userName = (String) session.getAttribute("name");

		session.removeAttribute("mode");
		session.removeAttribute("name");

		if (mode == null) {
			return new ModelAndView("redirect:/");
		}

		String title;
		String message = "<b>" + userName + "</b>님 ";

		if (mode.equals("account")) {
			title = "학생등록";
			message += "학생 등록이 완료 되었습니다.";
		} else {
			title = "학생 정보 수정";
			message += "학생 정보 수정이 완료되었습니다.";
		}

		ModelAndView mav = new ModelAndView("admin/student/complete");

		mav.addObject("title", title);
		mav.addObject("message", message);

		return mav;
	}

	// 학생 리스트
	@RequestMapping(value = "/admin/student/list", method = RequestMethod.GET)
	public ModelAndView studentList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    StudentDAO dao = new StudentDAO();
	    MyUtil util = new MyUtil();

	    ModelAndView mav = new ModelAndView("admin/student/list");

	    try {
	        String page = req.getParameter("page");
	        int current_page = 1;
	        if (page != null) {
	            current_page = Integer.parseInt(page);
	        }

	        String pageSize = req.getParameter("size");
	        int size = pageSize == null ? 10 : Integer.parseInt(pageSize);

	        String schType = req.getParameter("schType");
	        String kwd = req.getParameter("kwd");
	        if (schType == null) {
	            schType = "all";
	            kwd = "";
	        }

	        kwd = util.decodeUrl(kwd);

	        int dataCount = (kwd.length() != 0)
	    			? dao.dataCount(schType, kwd)
	    			: dao.dataCount();

	        int total_page = util.pageCount(dataCount, size);
	        if (current_page > total_page) {
	            current_page = total_page;
	        }

	        int offset = (current_page - 1) * size;
	        if (offset < 0) offset = 0;

	        List<StudentDTO> list;
	        if (kwd.length() != 0) {
	            list = dao.listStudent(offset, size, schType, kwd);
	        } else {
	            list = dao.listStudent(offset, size);
	        }
	        String cp = req.getContextPath();
	        String query = "size=" + size + "&schType=" + schType + "&kwd=" + URLEncoder.encode(kwd, "UTF-8");

	        String listUrl = cp + "/admin/student/list?" + query;
	        String articleUrl = cp + "/admin/student/article?page=" + current_page + "&" + query;
	        String paging = util.paging(current_page, total_page, listUrl);

	        mav.addObject("list", list);
	        mav.addObject("dataCount", dataCount);
	        mav.addObject("size", size);
	        mav.addObject("page", current_page);
	        mav.addObject("total_page", total_page);
	        mav.addObject("articleUrl", articleUrl);
	        mav.addObject("paging", paging);
	        mav.addObject("kwd", kwd);
	        mav.addObject("schType", schType);

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return mav;
	}

	// 학생 수정폼
	@RequestMapping(value = "/admin/student/update", method = RequestMethod.GET)
	public ModelAndView updateStudentForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String member_id = req.getParameter("member_id");

		StudentDAO dao = new StudentDAO();
		StudentDTO dto = null;
		List<Map<String, Object>> statusList = null;
		List<DepartmentDTO> departmentList = null;

		try {
			dto = dao.findById(member_id);
			statusList = dao.getStudentStatusList();
			departmentList = dao.listDepartment();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (dto == null) {
			return new ModelAndView("redirect:/admin/student/list");
		}

		ModelAndView mav = new ModelAndView("admin/student/account");
		mav.addObject("dto", dto);
		mav.addObject("mode", "update");
		mav.addObject("statusList", statusList);
		mav.addObject("departmentList", departmentList);

		return mav;
	}

	// 학생 수정
	@RequestMapping(value = "/admin/student/update", method = RequestMethod.POST)
	public ModelAndView updateStudent(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		StudentDAO dao = new StudentDAO();
		HttpSession session = req.getSession();

		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "avatar";

		FileManager fileManager = new FileManager();
		String avatar = null;

		try {
			// 기존 아바타
			String originalAvatar = req.getParameter("originalAvatar");

			Part part = req.getPart("selectFile");
			MyMultipartFile multiFile = fileManager.doFileUpload(part, pathname);

			if (multiFile != null && multiFile.getSize() > 0) {
				avatar = multiFile.getSaveFilename();

				// 기존 파일 삭제
				if (originalAvatar != null && !originalAvatar.isEmpty()) {
					fileManager.doFiledelete(pathname, originalAvatar);
				}

			} else {
				avatar = originalAvatar; // 새 파일 없으면 기존 유지
			}

			StudentDTO dto = new StudentDTO();

			dto.setMember_id(req.getParameter("member_id"));
			dto.setName(req.getParameter("name"));
			dto.setPassword(req.getParameter("password"));
			dto.setEmail(req.getParameter("email1") + "@" + req.getParameter("email2"));
			dto.setPhone(req.getParameter("phone"));
			dto.setBirth(req.getParameter("birth"));
			dto.setAddr1(req.getParameter("addr1"));
			dto.setAddr2(req.getParameter("addr2"));
			dto.setGrade(Integer.parseInt(req.getParameter("grade")));
			dto.setAdmission_date(req.getParameter("admission_date"));
			dto.setDepartment_id(req.getParameter("department_id"));
			dto.setAcademic_status(req.getParameter("academic_status"));

			dto.setAvatar(avatar);

			dao.updateStudentByAdmin(dto);

			session.setAttribute("mode", "update");
			session.setAttribute("name", dto.getName());

			return new ModelAndView("redirect:/admin/student/complete");

		} catch (Exception e) {
			e.printStackTrace();
		}

		ModelAndView mav = new ModelAndView("admin/student/account");
		mav.addObject("mode", "update");

		try {
			StudentDTO dto = dao.findById(req.getParameter("member_id"));
			mav.addObject("dto", dto);
			mav.addObject("statusList", dao.getStudentStatusList());
		} catch (Exception e) {
			e.printStackTrace();
		}

		mav.addObject("message", "수정에 실패했습니다.");
		return mav;
	}

	// 학생 삭제
	@RequestMapping(value = "/admin/student/delete", method = RequestMethod.GET)
	public ModelAndView deleteStudent(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		StudentDAO dao = new StudentDAO();
		String member_id = req.getParameter("member_id");

		try {
			dao.deleteStudentByAdmin(member_id);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/admin/student/list");
	}

	// 학생 상세정보 출력
	@RequestMapping(value = "/admin/student/article", method = RequestMethod.GET)
	public ModelAndView viewStudent(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String member_id = req.getParameter("member_id");

		StudentDAO dao = new StudentDAO();
		StudentDTO dto = null;

		try {
			dto = dao.findById(member_id);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (dto == null) {
			return new ModelAndView("redirect:/admin/student/list");
		}

		ModelAndView mav = new ModelAndView("admin/student/article");
		mav.addObject("dto", dto);
		return mav;
	}

	@ResponseBody
	@RequestMapping(value = "/admin/student/userIdCheck", method = RequestMethod.POST)
	public Map<String, Object> studentUserIdCheck(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		Map<String, Object> model = new HashMap<>();

		String member_id = req.getParameter("member_id");

		AdminDAO dao = new AdminDAO();
		MemberDTO dto = dao.findById(member_id);

		String passed = (dto == null) ? "true" : "false";
		model.put("passed", passed);

		return model;
	}

	@RequestMapping(value = "/student/student/pwd", method = RequestMethod.GET)
	public ModelAndView pwdForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 패스워드 확인 폼(정보수정)
		ModelAndView mav = new ModelAndView("student/student/pwd");

		return mav;
	}

	@RequestMapping(value = "/student/student/pwd", method = RequestMethod.POST)
	public ModelAndView pwdSubmit(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 패스워드 확인
		StudentDAO dao = new StudentDAO();

		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		try {
			MemberDTO dto = dao.findById(info.getMember_id());
			if (dto == null) {
				session.invalidate();
				return new ModelAndView("redirect:/student/main/main");
			}

			String password = req.getParameter("password");
			String mode = req.getParameter("mode");

			if (!dto.getPassword().equals(password)) {
				ModelAndView mav = new ModelAndView("student/student/pwd");
				
				mav.addObject("mode", mode);
				mav.addObject("message", "패스워드가 일치하지 않습니다.");

				return mav;
			}

			// 정보수정 화면
			ModelAndView mav = new ModelAndView("student/student/update");

			mav.addObject("dto", dto);
			mav.addObject("mode", "update");

			return mav;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/student/main/main");
	}

	@RequestMapping(value = "/student/lecture/list", method = RequestMethod.GET) 
	public ModelAndView lectureList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 강의 리스트
		LectureDAO dao = new LectureDAO();
		MyUtil util = new MyUtil();

		ModelAndView mav = new ModelAndView("student/lecture/list");

		try {
			String page = req.getParameter("page");
			int current_page = 1;
			if (page != null) {
				current_page = Integer.parseInt(page);
			}

			int dataCount = dao.dataCount();

			int size = 10;
			int total_page = util.pageCount(dataCount, size);
			if (current_page > total_page) {
				current_page = total_page;
			}

			int offset = (current_page - 1) * size;
			if (offset < 0)
				offset = 0;

			List<LectureDTO> list = dao.listLecture(offset, size);

			String cp = req.getContextPath();
			String listUrl = cp + "/student/lecture/list";
			String articleUrl = cp + "/student/lecture/view?page=" + current_page;

			String paging = util.paging(current_page, total_page, listUrl);

			mav.addObject("list", list);
			mav.addObject("dataCount", dataCount);
			mav.addObject("size", size);
			mav.addObject("page", page);
			mav.addObject("total_page", total_page);
			mav.addObject("articleUrl", articleUrl);
			mav.addObject("paging", paging);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}

	@RequestMapping(value = "/student/lecture/account", method = RequestMethod.POST)
	public ModelAndView lectureSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 수강 신청
		StudentDAO dao = new StudentDAO();

		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		try {
			String memberId = info.getMember_id();
	        String lectureCode = req.getParameter("lecture_code");
	        
	        boolean already = dao.isAlready(memberId, lectureCode);
	        if (already) {
	            session.setAttribute("error", "이미 수강 신청한 강의입니다.");
	            return new ModelAndView("redirect:/student/lecture/list");
	        }
			
			
			Course_ApplicationDTO dto = new Course_ApplicationDTO();

			dto.setMember_id(memberId);
			dto.setLecture_code(lectureCode);

			dao.insertCourse(dto);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/student/lecture/list");
	}

	@RequestMapping(value = "/student/lecture/compList", method = RequestMethod.GET)
	public ModelAndView studentCompList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 수강 신청 완료
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		StudentDAO dao = new StudentDAO();
		ModelAndView mav = new ModelAndView("student/lecture/compList");
		LectureDAO lectureDao = new LectureDAO();

		try {
			if (info != null) {
				String memberId = String.valueOf(info.getMember_id());
				List<LectureDTO> lectures = lectureDao.std_listsidebar(memberId);
				mav.addObject("lectureList", lectures);
			}

			List<Course_ApplicationDTO> list = dao.listCourse(info.getMember_id());

			mav.addObject("list", list);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return mav;
	}

	@RequestMapping(value = "/student/study/list", method = RequestMethod.GET)
	public ModelAndView studentNowStudy(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 강의 정보 페이지
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		StudentDAO dao = new StudentDAO();
		LectureDAO lectureDao = new LectureDAO();
		DataDAO dataDao = new DataDAO();
		Pro_hwDAO hwDao = new Pro_hwDAO();
		ModelAndView mav = new ModelAndView("/student/study/list");

		try {
			String lecture_code = req.getParameter("lecture_code");
		
			if (info != null) {
				String memberId = String.valueOf(info.getMember_id());
				List<LectureDTO> lectures = lectureDao.std_listsidebar(memberId);
				mav.addObject("lectureList", lectures);
			}

			List<Course_ApplicationDTO> list = dao.listCourse(info.getMember_id());
			List<DataDTO> lectureFileList = dataDao.listData(lecture_code);
			List<Pro_hwDTO> hwList = hwDao.listPro_hw(0, 10, lecture_code);
			
			mav.addObject("list", list);
			mav.addObject("lectureFileList", lectureFileList);
			mav.addObject("hwList", hwList);
			mav.addObject("lecture_code", lecture_code);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return mav;
	}


	// ⚠️⚠️ 파일 충돌 날까봐 임시로 이곳에 지정. 나중에 SidebarController로 이동해야 함 - 하은
	@RequestMapping(value = "/layout/student_menusidebar", method = RequestMethod.GET)
	public ModelAndView lectureListSidebar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		if (info == null) {
			return new ModelAndView("redirect:/member/login");
		}

		StudentDAO dao = new StudentDAO();

		List<Course_ApplicationDTO> lectureList = dao.listCourse(info.getMember_id());

		ModelAndView mav = new ModelAndView("/layout/student_menusidebar");
		mav.addObject("lectureList", lectureList);

		return mav;
	}

	@RequestMapping(value = "/student/student/update", method = RequestMethod.POST)
	public ModelAndView updateSubmit(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 개인정보수정완료
		StudentDAO dao = new StudentDAO();
		FileManager fileManager = new FileManager();

		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "member";

		try {
			StudentDTO dto = new StudentDTO();

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

			dto.setAvatar(req.getParameter("avatar"));
			Part p = req.getPart("selectFile");
			MyMultipartFile multilPart = fileManager.doFileUpload(p, pathname);
			if (multilPart != null) {
				// 기존 사진 삭제
				if (dto.getAvatar().length() != 0) {
					fileManager.doFiledelete(pathname, dto.getAvatar());
				}

				dto.setAvatar(multilPart.getSaveFilename());
			}

			dao.updateStudent(dto);

			// 로그인한 사용자 본인일 경우에만 세션 정보 갱신
			if (info.getMember_id().equals(dto.getMember_id())) {
				info.setAvatar(dto.getAvatar());
				info.setName(dto.getName());
				info.setDivision(dto.getDivision());
				session.setAttribute("member", info);
			}

			return new ModelAndView("redirect:/student/main/main");

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/student/main/main");

	}

}