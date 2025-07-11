package com.lms2.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lms2.dao.DataDAO;
import com.lms2.model.DataDTO;
import com.lms2.mvc.annotation.RequestMapping;
import com.lms2.mvc.annotation.RequestMethod;
import com.lms2.mvc.annotation.ResponseBody;
import com.lms2.mvc.view.ModelAndView;
import com.lms2.util.MyUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
			
			String paging = util.paging(current_page, total_page, articleUrl);
			
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
		
		return mav;
	}
	
	@RequestMapping(value = "/data/write", method = RequestMethod.POST)
	public ModelAndView writeSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//자료실 글 저장
		ModelAndView mav = new ModelAndView("data/write");
		
		return mav;
	}
	
	@RequestMapping(value = "/data/article", method = RequestMethod.GET)
	public ModelAndView article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//자료실 글보기
		ModelAndView mav = new ModelAndView("data/article");
		
		return mav;
	}
	
	@RequestMapping(value = "/data/update", method = RequestMethod.GET)
	public ModelAndView updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//자료실 글수정 폼
		ModelAndView mav = new ModelAndView("data/update");
		
		return mav;//리스트 페이지로 리턴
	}
	
	@RequestMapping(value = "/data/update", method = RequestMethod.POST)
	public ModelAndView updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//자료실 글수정 완료
		ModelAndView mav = new ModelAndView("data/update");
		
		return mav;//리스트페이지로 리턴
	}
	
	@RequestMapping(value = "/data/delete", method = RequestMethod.GET)
	public ModelAndView delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//자료실 글수정 완료
		ModelAndView mav = new ModelAndView("data/delete");
		
		return mav;//리스트페이지로 리턴
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
