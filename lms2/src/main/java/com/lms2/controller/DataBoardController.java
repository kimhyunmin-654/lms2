package com.lms2.controller;

import java.io.IOException;
import java.util.List;

import com.lms2.dao.DataDAO;
import com.lms2.model.DataDTO;
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
public class DataBoardController {
	@RequestMapping(value = "/professor/bbs/list", method = RequestMethod.GET)
	public ModelAndView pList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("/WEB-INF/views/professor/bbs/list.jsp");
		DataDAO dao = new DataDAO();
		MyUtil util = new MyUtil();

		try {
			String page = req.getParameter("page");
			int current_page = 1;
			if (page != null) current_page = Integer.parseInt(page);

			int dataCount = dao.dataCount();
			int size = 10;
			int total_page = util.pageCount(dataCount, size);
			if (current_page > total_page) current_page = total_page;

			int offset = (current_page - 1) * size;
			if (offset < 0) offset = 0;

			List<DataDTO> list = dao.listData(offset, size);
			String cp = req.getContextPath();
			String listUrl = cp + "/data/list";
			String articleUrl = cp + "/data/article?page=" + current_page;
			String paging = util.paging(current_page, total_page, listUrl);

			mav.addObject("list", list);
			mav.addObject("articleUrl", articleUrl);
			mav.addObject("dataCount", dataCount);
			mav.addObject("page", current_page);
			mav.addObject("total_page", total_page);
			mav.addObject("paging", paging);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}
	
	@RequestMapping(value = "/student/bbs/list", method = RequestMethod.GET)
	public ModelAndView sList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("/WEB-INF/views/student/bbs/list.jsp");
		DataDAO dao = new DataDAO();
		MyUtil util = new MyUtil();

		try {
			String page = req.getParameter("page");
			int current_page = 1;
			if (page != null) current_page = Integer.parseInt(page);

			int dataCount = dao.dataCount();
			int size = 10;
			int total_page = util.pageCount(dataCount, size);
			if (current_page > total_page) current_page = total_page;

			int offset = (current_page - 1) * size;
			if (offset < 0) offset = 0;

			List<DataDTO> list = dao.listData(offset, size);
			String cp = req.getContextPath();
			String listUrl = cp + "/data/list";
			String articleUrl = cp + "/data/article?page=" + current_page;
			String paging = util.paging(current_page, total_page, listUrl);

			mav.addObject("list", list);
			mav.addObject("articleUrl", articleUrl);
			mav.addObject("dataCount", dataCount);
			mav.addObject("page", current_page);
			mav.addObject("total_page", total_page);
			mav.addObject("paging", paging);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}

	@RequestMapping(value = "/professor/bbs/article", method = RequestMethod.GET)
	public ModelAndView pArticle(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("/WEB-INF/views/data/article.jsp");
		DataDAO dao = new DataDAO();
		MyUtil util = new MyUtil();

		String page = req.getParameter("page");
		long num = Long.parseLong(req.getParameter("num"));
		try {
			/*dao.updateHitCount(num);
			DataDTO dto = dao.findById(num);
			if (dto == null) {
				resp.sendRedirect("/data/list?page=" + page);
				return null;
			}
			dto.setContent(util.htmlSymbols(dto.getContent()));
			mav.addObject("dto", dto);
			mav.addObject("page", page);*/
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}
	
	@RequestMapping(value = "/student/bbs/article", method = RequestMethod.GET)
	public ModelAndView sArticle(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("/WEB-INF/views/data/article.jsp");
		DataDAO dao = new DataDAO();
		MyUtil util = new MyUtil();

		String page = req.getParameter("page");
		long num = Long.parseLong(req.getParameter("num"));
		try {
			/*dao.updateHitCount(num);
			DataDTO dto = dao.findById(num);
			if (dto == null) {
				resp.sendRedirect("/data/list?page=" + page);
				return null;
			}
			dto.setContent(util.htmlSymbols(dto.getContent()));
			mav.addObject("dto", dto);
			mav.addObject("page", page);*/
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}

	@RequestMapping(value = "/professor/bbs/write", method = RequestMethod.GET)
	public ModelAndView writeForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("/WEB-INF/views/data/write.jsp");
		mav.addObject("mode", "write");
		return mav;
	}

	@RequestMapping(value = "/professor/bbs/write", method = RequestMethod.POST)
	public ModelAndView writeSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		DataDAO dao = new DataDAO();
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		try {
			DataDTO dto = new DataDTO();
			dto.setMember_id(info.getMember_id());
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));
			dao.insertData(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("redirect:/data/list");
	}

	@RequestMapping(value = "/professor/bbs/update", method = RequestMethod.GET)
	public ModelAndView updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("/WEB-INF/views/professor/bbs/write.jsp");
		DataDAO dao = new DataDAO();
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		String page = req.getParameter("page");
		long num = Long.parseLong(req.getParameter("num"));
		try {
			/*DataDTO dto = dao.findById(num);
			if (dto == null || (!dto.getMember_id().equals(info.getMember_id()) && info.getRole() != 99)) {
				return new ModelAndView("redirect:/data/list?page=" + page);
			}
			mav.addObject("dto", dto);
			mav.addObject("page", page);
			mav.addObject("mode", "update");*/
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}

	@RequestMapping(value = "/professor/bbs/update", method = RequestMethod.POST)
	public ModelAndView updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		DataDAO dao = new DataDAO();
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		String page = req.getParameter("page");
		try {
			DataDTO dto = new DataDTO();
			dto.setData_id(Integer.parseInt(req.getParameter("num")));
			dto.setMember_id(info.getMember_id());
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));
			dao.updateData(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("redirect:/data/list?page=" + page);
	}

	@RequestMapping(value = "/professor/bbs/delete", method = RequestMethod.GET)
	public ModelAndView delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		DataDAO dao = new DataDAO();
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		String page = req.getParameter("page");
		try {
			long num = Long.parseLong(req.getParameter("num"));
			// DataDTO dto = dao.findById(num);
			// if (dto == null || (!dto.getMember_id().equals(info.getMember_id()) && info.getRole() != 99)) {
			// 	return new ModelAndView("redirect:/data/list?page=" + page);
			// }
			// dao.deleteData(num);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("redirect:/data/list?page=" + page);
	}
}
