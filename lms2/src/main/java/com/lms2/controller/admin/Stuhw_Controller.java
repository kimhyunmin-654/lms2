package com.lms2.controller.admin;

import java.io.IOException;
import java.util.List;

import com.lms2.dao.Stuhw_DAO;
import com.lms2.model.SessionInfo;
import com.lms2.model.Std_hwDTO;
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
public class Stuhw_Controller {

    @RequestMapping(value = "/student/hw/list", method = RequestMethod.GET)
    public ModelAndView list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        ModelAndView mav = new ModelAndView("/student/hw/list.jsp");
        Stuhw_DAO dao = new Stuhw_DAO();
        MyUtil util = new MyUtil();

        try {
			String page = req.getParameter("page");
			int current_page = 1;
            HttpSession session = req.getSession(false);
            SessionInfo info = (SessionInfo) session.getAttribute("student");
            if (page != null) {
            	current_page = Integer.parseInt(page);
            }

            // 검색 옵션(필요하면 사용)
            String schType = req.getParameter("schType");
            String kwd = req.getParameter("kwd");
            if (schType == null) {
                schType = "all";
                kwd = "";
            }
            kwd = util.decodeUrl(kwd);

            int dataCount;
            if (kwd.length() == 0) {
                dataCount = dao.dataCount(info.getMember_id());
            } else {
                dataCount = dao.dataCount(info.getMember_id(), schType, kwd);
            }

            int size = 10;
            int total_page = util.pageCount(dataCount, size);
            if (current_page > total_page) {
                current_page = total_page;
            }
            int offset = (current_page - 1) * size;
            if (offset < 0) offset = 0;

            List<Std_hwDTO> list;
            if (kwd.length() == 0) {
                list = dao.listAssignment(info.getMember_id(), offset, size);
            } else {
                list = dao.listAssignment(info.getMember_id(), offset, size, schType, kwd);
            }

            String query = "";
            if (kwd.length() != 0) {
                query = "schType=" + schType + "&kwd=" + util.encodeUrl(kwd);
            }

            String cp = req.getContextPath();
            String listUrl = cp + "/student/hw/list";
            String articleUrl = cp + "/student/hw/article?page=" + current_page;
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

        } catch (Exception e) {
            e.printStackTrace();
        }

        return mav;
    }
}
