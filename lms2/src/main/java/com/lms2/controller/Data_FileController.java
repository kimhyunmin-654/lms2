package com.lms2.controller;

import java.io.File;
import java.io.IOException;

import com.lms2.dao.DataDAO;
import com.lms2.dao.Data_FileDAO;
import com.lms2.model.DataDTO;
import com.lms2.model.SessionInfo;
import com.lms2.mvc.view.ModelAndView;
import com.lms2.util.FileManager;
import com.lms2.util.MyMultipartFile;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

public class Data_FileController {

	public ModelAndView writeSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    DataDAO dataDao = new DataDAO();
	    Data_FileDAO fileDao = new Data_FileDAO();
	    FileManager fileManager = new FileManager();

	    HttpSession session = req.getSession();
	    SessionInfo info = (SessionInfo) session.getAttribute("member");

	    String root = session.getServletContext().getRealPath("/");
	    String pathname = root + "uploads" + File.separator + "data";

	    try {
	        DataDTO dto = new DataDTO();
	        dto.setMember_id(info.getMember_id());
	        dto.setSubject(req.getParameter("subject"));
	        dto.setContent(req.getParameter("content"));
	        dto.setLecture_code(req.getParameter("lecture_code"));

	        // 파일 업로드
	        Part p = req.getPart("uploadFile");
	        if (p != null && p.getSize() > 0) {
	            MyMultipartFile multi = fileManager.doFileUpload(p, pathname);
	            if (multi != null) {
	                dto.setSave_filename(multi.getSaveFilename());
	                dto.setOriginal_filename(multi.getOriginalFilename());
	                dto.setFile_size((int) p.getSize());
	            }
	        }

	        int dataId = dataDao.insertData(dto);
	        dto.setData_id(dataId);

	        if (dto.getSave_filename() != null) {
	            fileDao.insertFile(dto);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return new ModelAndView("redirect:/bbs/list");
	}
}
