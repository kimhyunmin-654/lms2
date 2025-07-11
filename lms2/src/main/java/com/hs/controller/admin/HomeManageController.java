package com.hs.controller.admin;

import java.io.IOException;

import com.hs.mvc.annotation.Controller;
import com.hs.mvc.annotation.RequestMapping;
import com.hs.mvc.view.ModelAndView;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class HomeManageController {
	@RequestMapping("/admin")
	public ModelAndView main(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("admin/home/main");
		return mav;
	}
}
