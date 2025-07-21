package com.lms2.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.lms2.dao.MemberDAO;
import com.lms2.model.MemberDTO;
import com.lms2.model.SessionInfo;
import com.lms2.mvc.annotation.Controller;
import com.lms2.mvc.annotation.RequestMapping;
import com.lms2.mvc.annotation.RequestMethod;
import com.lms2.mvc.annotation.ResponseBody;
import com.lms2.mvc.view.ModelAndView;
import com.lms2.util.FileManager;
import com.lms2.util.MyMultipartFile;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

@Controller
public class pro_InformationController {

	@RequestMapping(value = "/professor/information/pwd", method = RequestMethod.GET)
	public ModelAndView pwdForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("professor/information/pwd");

		String mode = req.getParameter("mode");

		mav.addObject("mode", mode);

		return mav;
	}

	// 패스워드확인
	@RequestMapping(value = "/professor/information/pwd", method = RequestMethod.POST)
	public ModelAndView pwdSubmit(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		MemberDAO dao = new MemberDAO();

		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		try {
			MemberDTO dto = dao.findById(info.getMember_id());
			if (dto == null) {
				session.invalidate();
				return new ModelAndView("redirect:/professor/main/main");
			}

			String password = req.getParameter("password");
			String mode = req.getParameter("mode");

			if (!dto.getPassword().equals(password)) {
				ModelAndView mav = new ModelAndView("professor/information/pwd");

				mav.addObject("mode", mode);
				mav.addObject("message", "패스워드가 일치하지 않습니다.");

				return mav;
			}

			ModelAndView mav = new ModelAndView("professor/information/account");

			mav.addObject("dto", dto);
			mav.addObject("mode", "update");

			return mav;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/professor/main/main");
	}

	@RequestMapping(value = "/professor/information/update", method = RequestMethod.GET)
	public ModelAndView updateForm(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String member_id = req.getParameter("member_id");
		MemberDAO dao = new MemberDAO();
		MemberDTO dto = dao.findById(member_id);

		if (dto == null) {
			return new ModelAndView("redirect:/professor/main/main");
		}

		ModelAndView mav = new ModelAndView("professor/information/account");
		mav.addObject("dto", dto);


		return mav;

	}

	@RequestMapping(value = "/professor/information/update", method = RequestMethod.POST)
	public ModelAndView updateSubmit(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 개인정보수정완료
		MemberDAO dao = new MemberDAO();
		FileManager fileManager = new FileManager();

		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "member";
		
		try {
			MemberDTO dto = new MemberDTO();
			
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
			
	        System.out.println("업로드된 파일: " + multilPart);
	        System.out.println("기존 avatar 파라미터: " + req.getParameter("avatar"));
	        System.out.println("최종 dto.setAvatar(): " + dto.getAvatar());
			if(multilPart != null) {
				// 기존 사진 삭제
				if(dto.getAvatar() != null && !dto.getAvatar().isEmpty()) {
					fileManager.doFiledelete(pathname, dto.getAvatar());
				}
				
				dto.setAvatar(multilPart.getSaveFilename());
			}
			
			dao.updateMember(dto);
			
			info.setAvatar(dto.getAvatar());
			
			session.setAttribute("member", info);
			session.setAttribute("mode", "update");
			session.setAttribute("name", dto.getName());
			
			return new ModelAndView("redirect:/professor/main/main");
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/professor/main/main");
	}
	
	@ResponseBody
	@RequestMapping(value = "/professor/information/deleteAvatar", method = RequestMethod.POST)
	public Map<String, Object> deleteAvatar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//프로필 사진 삭제
		Map<String, Object> model = new HashMap<String, Object>();
		
		MemberDAO dao = new MemberDAO();
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
}