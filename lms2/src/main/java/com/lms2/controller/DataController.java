package com.lms2.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lms2.dao.DataDAO;
import com.lms2.model.DataDTO;
import com.lms2.model.SessionInfo;
import com.lms2.mvc.annotation.RequestMapping;
import com.lms2.mvc.annotation.RequestMethod;
import com.lms2.mvc.annotation.ResponseBody;
import com.lms2.mvc.view.ModelAndView;
import com.lms2.util.MyUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class DataController {
	
	@RequestMapping(value = "/data/list", method = RequestMethod.GET)
	public ModelAndView list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//자료실 리스트
		ModelAndView mav = new ModelAndView("data/list");
		
		DataDAO dao = new DataDAO();
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
			
			List<DataDTO> list = null;
			if(kwd.length() == 0) {
				list = dao.listData(offset, size);
			} else {
				list = dao.listData(offset, size, schType, kwd);
			}
			
			String query = "";
			if(kwd.length() != 0) {
				query = "schType=" + schType + "&kwd=" + util.encodeUrl(kwd);
			}
			
			String cp = req.getContextPath();
			String listUrl = cp + "data/list";
			String articleUrl = cp + "/data/article?page=" + current_page;
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
	
	@RequestMapping(value = "/data/write", method = RequestMethod.GET)
	public ModelAndView writeForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//자료실 글쓰기
		ModelAndView mav = new ModelAndView("data/write");
		mav.addObject("mode", "write");
		return mav;
	}
	
	@RequestMapping(value = "/data/write", method = RequestMethod.POST)
	public ModelAndView writeSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//자료실 글 저장
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
	
	@RequestMapping(value = "/data/article", method = RequestMethod.GET)
	public ModelAndView article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//자료실 글보기
		DataDAO dao = new DataDAO();
		MyUtil util = new MyUtil();
		
		String page = req.getParameter("page");
		String query = "page=" + page;
		
		try {
			int data_id = Integer.parseInt(req.getParameter("data_id"));
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
			//게시물가져오기
			DataDTO dto = dao.findById(data_id);
			if(dto == null) {
				return new ModelAndView("redirect:/data/list?" + query);
			}
			dto.setContent(util.htmlSymbols(dto.getContent()));
			
			//이전글 다음글
			DataDTO prevDto = dao.findByPrev(dto.getData_id(), schType, kwd);
			DataDTO nextDto = dao.findByNext(dto.getData_id(), schType, kwd);
			
			ModelAndView mav = new ModelAndView("data/article");
			
			mav.addObject("dto", dto);
			mav.addObject("page", page);
			mav.addObject("query", query);
			mav.addObject("prevDto", prevDto);
			mav.addObject("nextDto", nextDto);
			
			return mav;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ModelAndView("redirect:/data/list?" + query);
	}
	
	@RequestMapping(value = "/data/update", method = RequestMethod.GET)
	public ModelAndView updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//자료실 글수정 폼
		DataDAO dao = new DataDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		String page = req.getParameter("page");
		
		try {
			int data_id = Integer.parseInt(req.getParameter("data_id"));
			DataDTO dto = dao.findById(data_id);
			
			if(dto == null) {
				return new ModelAndView("redirect:/data/list?page=" + page);
			}
			
			//게시물 작성자가 아니면
			if(! dto.getMember_id().equals(info.getMember_id())) {
				return new ModelAndView("redirect:/data/list?page=" + page);
			}
			
			ModelAndView mav = new ModelAndView("data/write");
			
			mav.addObject("dto", dto);
			mav.addObject("page", page);
			mav.addObject("mode", "update");
			
			return mav;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ModelAndView("redirect:/data/list?page=" + page);
	}
	
	@RequestMapping(value = "/data/update", method = RequestMethod.POST)
	public ModelAndView updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//자료실 글수정 완료
		DataDAO dao = new DataDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		String page = req.getParameter("page");
		
		try {
			DataDTO dto = new DataDTO();
			
			dto.setData_id(Integer.parseInt(req.getParameter("data_id")));
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));
			
			dto.setMember_id(info.getMember_id());
			
			dao.updateData(dto);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ModelAndView("redirect:/data/list?page=" + page);
	}
	
	@RequestMapping(value = "/data/delete", method = RequestMethod.GET)
	public ModelAndView delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//자료실 글삭제
		DataDAO dao = new DataDAO();
		MyUtil util = new MyUtil();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		String page = req.getParameter("page");
		String query = "page=" + page;
		
		try {
			int data_id = Integer.parseInt(req.getParameter("data_id"));
			
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
			
			dao.deleteData(data_id, info.getMember_id());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ModelAndView("redirect:/data/list?" + query);
	}
	
	@RequestMapping(value = "/data/listReply", method = RequestMethod.GET)
	public ModelAndView listComment(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//리플리스트
		ModelAndView mav = new ModelAndView("data/listReply");
		
		return mav;//리스트페이지로 리턴
	}
	
	@ResponseBody
	@RequestMapping(value = "/data/insertReply", method = RequestMethod.POST)
	public Map<String, Object> insertComment(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Map<String, Object> model = new HashMap<String, Object>();
		//리플 등록
		
		return model;
	}
	
	@ResponseBody
	@RequestMapping(value = "/data/deleteReply", method = RequestMethod.POST)
	public Map<String, Object> deleteComment(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Map<String, Object> model = new HashMap<String, Object>();
		//리플 삭제
		
		return model;
	}
	
}
