package com.lms2.controller;

import java.io.IOException;
import java.util.List;

import com.lms2.dao.Pro_hwDAO;
import com.lms2.model.Pro_hwDTO;
import com.lms2.model.SessionInfo;
import com.lms2.mvc.annotation.RequestMapping;
import com.lms2.mvc.annotation.RequestMethod;
import com.lms2.mvc.view.ModelAndView;
import com.lms2.util.MyUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class ProhwController {

	@RequestMapping(value = "/hw/list", method = RequestMethod.GET)
	public ModelAndView list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//리스트보기
		ModelAndView mav = new ModelAndView("professor/hw/list");
		
		Pro_hwDAO dao = new Pro_hwDAO();
		MyUtil util = new MyUtil();
		
		try {
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
			
			int dataCount;
			if(kwd.length() == 0) {
				dataCount = dao.dataCount();
			} else {
				dataCount = dao.dataCount(schType, kwd);
			}
			
			int size = 10;
			int total_page = util.pageCount(dataCount, size);
			if(current_page > total_page) {
				current_page = total_page;
			}
			
			int offset = (current_page - 1) * size;
			if(offset < 0) offset = 0;
			
			List<Pro_hwDTO> list = null;
			if(kwd.length() == 0) {
				list = dao.listPro_hw(offset, size);
			} else {
				list = dao.listPro_hw(offset, size, schType, kwd);
			}
			
			String query = "";
			if(kwd.length() != 0) {
				query = "schType=" + schType + "&kwd=" + util.encodeUrl(kwd);
			}
			
			String cp = req.getContextPath();
			String listUrl = cp + "/hw/list";
			String articleUrl = cp + "/hw/article?page=" + current_page;
			if(query.length() != 0) {
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}
	
	@RequestMapping(value = "/hw/write", method = RequestMethod.GET)
	public ModelAndView writeForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//글쓰기 폼
		ModelAndView mav = new ModelAndView("professor/hw/write");
		mav.addObject("mode", "write");
		return mav;
	}
	
	@RequestMapping(value = "/hw/write", method = RequestMethod.POST)
	public ModelAndView writeSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//글쓰기 완료
		Pro_hwDAO dao = new Pro_hwDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		try {
			Pro_hwDTO dto = new Pro_hwDTO();
			
			dto.setMember_id(info.getMember_id());
			
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));
			
			dao.inserthw(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ModelAndView("redirect:/hw/list");
	}
	
	@RequestMapping(value = "/hw/article", method = RequestMethod.GET)
	public ModelAndView article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//글보기
		Pro_hwDAO dao = new Pro_hwDAO();
		MyUtil util = new MyUtil();
		
		String page = req.getParameter("page");
		String query = "page=" + page;
		
		try {
			int homework_id = Integer.parseInt(req.getParameter("homework_id"));
			String schType = req.getParameter("schType");
			String kwd = req.getParameter("kwd");
			if(schType == null) {
				schType = "all";
				kwd = "";
			}
			kwd = util.decodeUrl(kwd);
			
			if(kwd.length() != 0) {
				query += "*schType=" + schType + "&kwd=" + util.encodeUrl(kwd);
			}
			
			dao.updateHitCount(homework_id);
			
			//과제게시물 가져오기
			Pro_hwDTO dto = dao.findById(homework_id);
			if(dto == null) {
				return new ModelAndView("redirect:/hw/list?" + query);
			}
			dto.setContent(util.htmlSymbols(dto.getContent()));
			
			Pro_hwDTO prevDto = dao.findByPrev(dto.getHomework_id(), schType, kwd);
			Pro_hwDTO nextDto = dao.findByNext(dto.getHomework_id(), schType, kwd);
			
			ModelAndView mav = new ModelAndView("hw/article");
			
			mav.addObject("dto", dto);
			mav.addObject("page", page);
			mav.addObject("query", query);
			mav.addObject("prevDto", prevDto);
			mav.addObject("nextDto", nextDto);
			
			return mav;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ModelAndView("redirect:/hw/list?" + query);
	}
	
	@RequestMapping(value = "/hw/update", method = RequestMethod.GET)
	public ModelAndView updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//글수정
		
		Pro_hwDAO dao = new Pro_hwDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		String page = req.getParameter("page");
		
		try {
			int homework_id = Integer.parseInt(req.getParameter("homework_id"));
			Pro_hwDTO dto = dao.findById(homework_id);
			
			if(dto == null) {
				return new ModelAndView("redirect:/hw/list?page=" + page);
			}
			
			if(! dto.getMember_id().equals(info.getMember_id())) {
				return new ModelAndView("redirect:/hw.list?page=" + page);
			}
			
			ModelAndView mav = new ModelAndView("hw/write");
			
			mav.addObject("dto", dto);
			mav.addObject("page", page);
			mav.addObject("mode", "update");
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/hw/list?page=" + page);
	}
	
	@RequestMapping(value = "/hw/update", method = RequestMethod.POST)
	public ModelAndView updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//글수정 완료
		Pro_hwDAO dao = new Pro_hwDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		String page = req.getParameter("page");
		
		try {
			Pro_hwDTO dto = new Pro_hwDTO();
			
			dto.setHomework_id(Integer.parseInt(req.getParameter("homework_id")));
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));
			
			dto.setMember_id(info.getMember_id());
			
			dao.updateHw(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ModelAndView("redirect:/hw/list?page=" + page);
	}
	
	@RequestMapping(value = "/hw/delete", method = RequestMethod.GET)
	public ModelAndView delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//글삭제
		Pro_hwDAO dao = new Pro_hwDAO();
		MyUtil util = new MyUtil();

		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		String page = req.getParameter("page");
		String query = "page=" + page;
		
		try {
			int homework_id = Integer.parseInt(req.getParameter("homework_id"));
			
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
			
			dao.deleteHw(homework_id, info.getMember_id());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ModelAndView("redirect:/hw/list?" + query);
		
	}
		
}
