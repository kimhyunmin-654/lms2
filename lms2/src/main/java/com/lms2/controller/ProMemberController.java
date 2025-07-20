package com.lms2.controller;

import java.io.IOException;
import java.util.List;

import com.lms2.dao.LectureDAO;
import com.lms2.dao.pro_memberDAO;
import com.lms2.model.LectureDTO;
import com.lms2.model.MemberDTO;
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
public class ProMemberController {
	
	@RequestMapping(value = "/professor/member/list", method = RequestMethod.GET)
	public ModelAndView list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("/professor/member/list");
		LectureDAO lectureDao = new LectureDAO();
		pro_memberDAO dao = new pro_memberDAO();
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
			if(page != null) {
				current_page = Integer.parseInt(page);
			}
			
			String schType = req.getParameter("schType");
			String kwd = req.getParameter("kwd");
			if(schType == null) {
				schType = "all";
				kwd = "";
			}
			
			kwd = util.decodeUrl(kwd);

			String query = "";
			if (kwd.length() != 0) {
				query = "schType=" + schType + "&kwd=" + util.encodeUrl(kwd);
			}
			
			String lecture_code = req.getParameter("lecture_code");
			if (lecture_code != null) {
				if (!query.isEmpty()) {
					query += "&";
				}
				query += "lecture_code=" + lecture_code;
			}
			
			kwd = util.decodeUrl(kwd);
					
			int dataCount;
			if(kwd.length() == 0) {
				dataCount = dao.dataCount(lecture_code);
			} else {
				dataCount = dao.dataCount(schType, kwd, lecture_code);
			}
			
			int size = 10;
			int total_page = util.pageCount(dataCount, size);
			if(current_page > total_page) {
				current_page = total_page;
			}
			
			int offset = (current_page - 1) * size;
			if(offset < 0) offset = 0;
			
			List<MemberDTO> list = null;
			if(kwd.length() == 0) {
				list = dao.listmember(lecture_code, offset, size);
			} else {
				list = dao.listmember(lecture_code, offset, size, schType, kwd);
			}
			
			// 페이징
			String cp = req.getContextPath();
			String listUrl = cp + "/professor/member/list";
			if(query.length() != 0) {
				listUrl += "?" + query;
			}
			
			String paging = util.paging(current_page, total_page, listUrl);
			
			// 포워딩할 JSP에 전달할 속성
			mav.addObject("list", list);
			mav.addObject("dataCount", dataCount);
			mav.addObject("size", size);
			mav.addObject("page", current_page);
			mav.addObject("total_page", total_page);
			mav.addObject("paging", paging);
			mav.addObject("schType", schType);
			mav.addObject("kwd", kwd);
			mav.addObject("lecture_code", lecture_code);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return mav;
	}
}
