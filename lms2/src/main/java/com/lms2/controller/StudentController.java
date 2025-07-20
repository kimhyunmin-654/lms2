package com.lms2.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lms2.dao.AdminDAO;
import com.lms2.dao.LectureDAO;
import com.lms2.dao.NoticeDAO;
import com.lms2.dao.StudentDAO;
import com.lms2.model.Course_ApplicationDTO;
import com.lms2.model.LectureDTO;
import com.lms2.model.MemberDTO;
import com.lms2.model.NoticeDTO;
import com.lms2.model.SessionInfo;
import com.lms2.model.StudentDTO;
import com.lms2.mvc.annotation.Controller;
import com.lms2.mvc.annotation.RequestMapping;
import com.lms2.mvc.annotation.RequestMethod;
import com.lms2.mvc.annotation.ResponseBody;
import com.lms2.mvc.view.ModelAndView;
import com.lms2.util.MyUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class StudentController {

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
			List<NoticeDTO> listNotice = noticeDao.listNotice();
			mav.addObject("listNotice", listNotice);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return mav;
	}

	@RequestMapping(value = "/admin/student/list", method = RequestMethod.GET)
	public ModelAndView studentList(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 학생 리스트
		StudentDAO dao = new StudentDAO();
		MyUtil util = new MyUtil();

		ModelAndView mav = new ModelAndView("admin/student/list");

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

			List<StudentDTO> list = dao.listStudent(offset, size);

			String cp = req.getContextPath();
			String listUrl = cp + "/admin/student/list";
			String articleUrl = cp + "/admin/article?page=" + current_page;

			String paging = util.paging(current_page, total_page, listUrl);

			mav.addObject("list", list);
			mav.addObject("dataCount", dataCount);
			mav.addObject("size", size);
			mav.addObject("page", current_page);
			mav.addObject("total_page", total_page);
			mav.addObject("articleUrl", articleUrl);
			mav.addObject("paging", paging);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}

	@RequestMapping(value = "/admin/student/account", method = RequestMethod.GET)
	public ModelAndView accountStudentForm(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 학생 등록 폼
		ModelAndView mav = new ModelAndView("admin/student/account");
		mav.addObject("mode", "account");

		return mav;
	}

	@RequestMapping(value = "/admin/student/account", method = RequestMethod.POST)
	public ModelAndView accountStudentSubmit(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 학생 등록
		StudentDAO dao = new StudentDAO();

		// 사진 파일은 나중에... -김하은

		HttpSession session = req.getSession();

		String message = "";

		try {
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

			dto.setStatus_id(1);
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

	@RequestMapping(value = "/admin/student/complete", method = RequestMethod.GET)
	public ModelAndView studentComplete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 학생 등록 완료 후
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

	@RequestMapping(value = "/student/lecture/list", method = RequestMethod.GET)
	public ModelAndView lectureList(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
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
	public ModelAndView lectureSubmit(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 수강 신청
		StudentDAO dao = new StudentDAO();

		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		try {
			Course_ApplicationDTO dto = new Course_ApplicationDTO();

			dto.setMember_id(info.getMember_id());
			dto.setLecture_code(req.getParameter("lecture_code"));

			dao.insertCourse(dto);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/student/lecture/list");
	}

	@RequestMapping(value = "/student/lecture/compList", method = RequestMethod.GET)
	public ModelAndView studentCompList(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 수강 신청 완료
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		StudentDAO dao = new StudentDAO();
		ModelAndView mav = new ModelAndView("student/lecture/compList");
		LectureDAO lectureDao = new LectureDAO();

		try {
			if (info != null) {
				String memberId = String.valueOf(info.getMember_id());
				List<LectureDTO> lectures = lectureDao.listsidebar(memberId);
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
	public ModelAndView studentNowStudy(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 강의 정보 페이지
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		StudentDAO dao = new StudentDAO();
		ModelAndView mav = new ModelAndView("student/study/list");

		try {
			List<Course_ApplicationDTO> list = dao.listCourse(info.getMember_id());

			mav.addObject("list", list);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return mav;
	}

	// ⚠️⚠️ 파일 충돌 날까봐 임시로 이곳에 지정. 나중에 SidebarController로 이동해야 함 - 하은
	@RequestMapping(value = "/layout/student_menusidebar", method = RequestMethod.GET)
	public ModelAndView lectureListSidebar(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

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

	// 학생 수정폼
	@RequestMapping(value = "/admin/student/update", method = RequestMethod.GET)
	public ModelAndView updateStudentForm(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String member_id = req.getParameter("member_id");

		StudentDAO dao = new StudentDAO();
		StudentDTO dto = null;
		List<Map<String, Object>> statusList = null;

		try {
			dto = dao.findById(member_id);
			statusList = dao.getStudentStatusList();
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

		return mav;
	}

	// 학생 수정
	@RequestMapping(value = "/admin/student/update", method = RequestMethod.POST)
	public ModelAndView updateStudent(HttpServletRequest req, HttpServletResponse resp)
	        throws ServletException, IOException {
	    StudentDAO dao = new StudentDAO();
	    HttpSession session = req.getSession();

	    try {
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

	        String statusIdStr = req.getParameter("status_id");
	        if (statusIdStr != null && !statusIdStr.isEmpty()) {
	            dto.setStatus_id(Integer.parseInt(statusIdStr));
	        }

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
	public ModelAndView deleteStudent(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

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
	@RequestMapping(value = "/admin/article", method = RequestMethod.GET)
	public ModelAndView viewStudent(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
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
	public Map<String, Object> studentUserIdCheck(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		Map<String, Object> model = new HashMap<>();

		String member_id = req.getParameter("member_id");

		AdminDAO dao = new AdminDAO();
		MemberDTO dto = dao.findById(member_id);

		String passed = (dto == null) ? "true" : "false";
		model.put("passed", passed);

		return model;
	}

}