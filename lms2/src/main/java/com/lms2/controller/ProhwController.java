package com.lms2.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import com.lms2.dao.LectureDAO;
import com.lms2.dao.Pro_hwDAO;
import com.lms2.model.LectureDTO;
import com.lms2.model.Pro_hwDTO;
import com.lms2.model.SessionInfo;
import com.lms2.model.Std_hwDTO;
import com.lms2.mvc.annotation.Controller;
import com.lms2.mvc.annotation.RequestMapping;
import com.lms2.mvc.annotation.RequestMethod;
import com.lms2.mvc.view.ModelAndView;
import com.lms2.util.FileManager;
import com.lms2.util.MyMultipartFile;
import com.lms2.util.MyUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

@Controller
public class ProhwController {

	  @RequestMapping(value = "/professor/hw/list", method = RequestMethod.GET)
	    public ModelAndView list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	        ModelAndView mav = new ModelAndView("/professor/hw/list");
	        Pro_hwDAO dao = new Pro_hwDAO();
	        LectureDAO lectureDao = new LectureDAO();
	        MyUtil util = new MyUtil();

	        try {
	            HttpSession session = req.getSession(false);
	            SessionInfo info = (SessionInfo) session.getAttribute("member");
	            String lecture_code = null;

	            List<LectureDTO> lectures = null;
	            if (info != null) {
	                String memberId = String.valueOf(info.getMember_id());
	                lectures = lectureDao.listsidebar(memberId);
	                mav.addObject("lectureList", lectures);
	            }

	            String page = req.getParameter("page");
	            int current_page = 1;
	            if (page != null) {
	                current_page = Integer.parseInt(page);
	            }

	            String schType = req.getParameter("schType");
	            String kwd = req.getParameter("kwd");
	            if (schType == null) {
	                schType = "all";
	                kwd = "";
	            }
	            kwd = util.decodeUrl(kwd);

	            String query = "";
	            if (kwd.length() != 0) {
	                query = "schType=" + schType + "&kwd=" + util.encodeUrl(kwd);
	            }

	            lecture_code = req.getParameter("lecture_code");

	            if (lecture_code == null || lecture_code.trim().isEmpty()) {
	                if (lectures != null && !lectures.isEmpty()) {
	                    lecture_code = lectures.get(0).getLecture_code();
	                }
	            }

	            if (lecture_code != null && !lecture_code.trim().isEmpty()) {
	                if (!query.isEmpty()) {
	                    query += "&";
	                }
	                query += "lecture_code=" + lecture_code;
	            }

	            int dataCount;
	            if (kwd.length() == 0) {
	                dataCount = dao.dataCount(lecture_code);
	            } else {
	                dataCount = dao.dataCount(schType, kwd, lecture_code);
	            }

	            int size = 10;
	            int total_page = util.pageCount(dataCount, size);
	            if (current_page > total_page) {
	                current_page = total_page;
	            }

	            int offset = (current_page - 1) * size;
	            if (offset < 0) offset = 0;

	            List<Pro_hwDTO> list = null;
	            if (kwd.length() == 0) {
	                list = dao.listPro_hw(offset, size, lecture_code);
	            } else {
	                list = dao.listPro_hw(offset, size, schType, kwd, lecture_code);
	            }

	            String cp = req.getContextPath();
	            String listUrl = cp + "/professor/hw/list";
	            String articleUrl = cp + "/professor/hw/article?page=" + current_page;

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
	            mav.addObject("lecture_code", lecture_code);

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return mav;
	    }

	//학생과제보기
	@RequestMapping(value = "/student/hw/article", method = RequestMethod.GET)
	public ModelAndView sArticle(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
				query += "&schType=" + schType + "&kwd=" + util.encodeUrl(kwd);
			}
			
			String lecture_code = req.getParameter("lecture_code");
			if(lecture_code != null) {
				if(!query.isEmpty()) {
					query += "&";
				}
				query += "lecture_code=" + lecture_code;
			}
			Pro_hwDTO dto = dao.findById(homework_id);
			if(dto == null) {
				return new ModelAndView("redirect:/student/hw/list?" + query);
			}
			dto.setContent(util.htmlSymbols(dto.getContent()));
			
			Pro_hwDTO prevDto = dao.findByPrev(dto.getHomework_id(), dto.getLecture_code(), schType, kwd);
			Pro_hwDTO nextDto = dao.findByNext(dto.getHomework_id(), dto.getLecture_code(), schType, kwd);
			
			ModelAndView mav = new ModelAndView("student/hw/article");
			mav.addObject("dto", dto);
			mav.addObject("page", page);
			mav.addObject("query", query);
			mav.addObject("prevDto", prevDto);
			mav.addObject("nextDto", nextDto);
			mav.addObject("lecture_code", lecture_code);
			
			return mav;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("redirect:/student/hw/list?" + query);
	}
	
	//학생 글리스트
	@RequestMapping(value = "/student/hw/list", method = RequestMethod.GET)
	public ModelAndView sList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    ModelAndView mav = new ModelAndView("/student/hw/list");

	    Pro_hwDAO dao = new Pro_hwDAO();
	    LectureDAO lectureDao = new LectureDAO();
	    MyUtil util = new MyUtil();

	    HttpSession session = req.getSession(false);
	    SessionInfo info = (SessionInfo) session.getAttribute("member");

	    try {
	        String lecture_code = req.getParameter("lecture_code"); 

	        if (info != null) {
	            String memberId = String.valueOf(info.getMember_id());
	            List<LectureDTO> lectures = lectureDao.listsidebar(memberId);
	            mav.addObject("lectureList", lectures);

	            if (lecture_code == null || lecture_code.trim().isEmpty()) {
	                if (lectures != null && !lectures.isEmpty()) {
	                    lecture_code = lectures.get(0).getLecture_code();
	                    return new ModelAndView("redirect:/student/hw/list?lecture_code=" + lecture_code);
	                }
	            }
	        }

	        String page = req.getParameter("page");
	        int current_page = 1;
	        if (page != null) {
	            current_page = Integer.parseInt(page);
	        }

	        String schType = req.getParameter("schType");
	        String kwd = req.getParameter("kwd");
	        if (schType == null) {
	            schType = "all";
	            kwd = "";
	        }
	        kwd = util.decodeUrl(kwd);

	        String query = "";
	        if (kwd.length() != 0) {
	            query = "schType=" + schType + "&kwd=" + util.encodeUrl(kwd);
	        }

	        System.out.println("lecture_code = " + lecture_code);
	        if (lecture_code != null) {
	            if (!query.isEmpty()) {
	                query += "&";
	            }
	            query += "lecture_code=" + lecture_code;
	        }

	        int dataCount;
	        if (kwd.length() == 0) {
	            dataCount = dao.dataCount(lecture_code);
	        } else {
	            dataCount = dao.dataCount(schType, kwd, lecture_code);
	        }

	        int size = 10;
	        int total_page = util.pageCount(dataCount, size);
	        if (current_page > total_page) {
	            current_page = total_page;
	        }

	        int offset = (current_page - 1) * size;
	        if (offset < 0) offset = 0;

	        List<Pro_hwDTO> list = null;
	        if (kwd.length() == 0) {
	            list = dao.listPro_hw(offset, size, lecture_code);
	        } else {
	            list = dao.listPro_hw(offset, size, schType, kwd, lecture_code);
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
	        mav.addObject("lecture_code", lecture_code);

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return mav;
	}
	
	@RequestMapping(value = "/professor/hw/write", method = RequestMethod.GET)
	public ModelAndView writeForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//글쓰기 폼
		ModelAndView mav = new ModelAndView("professor/hw/write");
		mav.addObject("mode", "write");
		LectureDAO lectureDao = new LectureDAO();
		
		try {
			HttpSession session = req.getSession();
			SessionInfo info = (SessionInfo) session.getAttribute("member");
			
			if(info != null) {
				String memberId = String.valueOf(info.getMember_id());
				List<LectureDTO> lectures = lectureDao.listsidebar(memberId);
				mav.addObject("lectureList", lectures);
				
			}
			
			String lecture_code = req.getParameter("lecture_code");
			
			Pro_hwDAO dao = new Pro_hwDAO();
			List<Pro_hwDTO> lectureList = dao.listLectureByMember(info.getMember_id(), lecture_code);
			mav.addObject("hwLectureList", lectureList);
			mav.addObject("lecture_code", lecture_code);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return mav;
	}
	
	@RequestMapping(value = "/professor/hw/write", method = RequestMethod.POST)
	public ModelAndView writeSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//글쓰기 완료
		Pro_hwDAO dao = new Pro_hwDAO();
		FileManager fileManager = new FileManager();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "lecture";
		String lecture_code = req.getParameter("lecture_code");
		
		try {
			Part p = req.getPart("selectFile");
			Pro_hwDTO dto = new Pro_hwDTO();
			
			dto.setMember_id(info.getMember_id());
			
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));
			dto.setDeadline_date(req.getParameter("deadline_date"));
			dto.setLecture_code(req.getParameter("lecture_code"));
			
			MyMultipartFile multiFile = fileManager.doFileUpload(p, pathname);
			if(multiFile != null) {
				dto.setSave_filename(multiFile.getSaveFilename());
				dto.setOriginal_filename(multiFile.getOriginalFilename());
			}
			dao.inserthw(dto);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ModelAndView("redirect:/professor/hw/list?lecture_code=" + lecture_code);
	}
	
	@RequestMapping(value = "/professor/hw/article", method = RequestMethod.GET)
    public ModelAndView article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LectureDAO lectureDao = new LectureDAO();
        Pro_hwDAO dao = new Pro_hwDAO();
        MyUtil util = new MyUtil();

        String page = req.getParameter("page");
        if (page == null || page.isEmpty()) {
            page = "1";
        }
        String query = "page=" + page;

        try {
            HttpSession session = req.getSession(false);
            SessionInfo info = (SessionInfo) session.getAttribute("member");
            int homework_id = Integer.parseInt(req.getParameter("homework_id"));
            String schType = req.getParameter("schType");
            String kwd = req.getParameter("kwd");
            if (schType == null) {
                schType = "all";
                kwd = "";
            }
            kwd = util.decodeUrl(kwd);

            if (kwd.length() != 0) {
                query += "&schType=" + schType + "&kwd=" + util.encodeUrl(kwd);
            }

            String lecture_code = req.getParameter("lecture_code");
            if (lecture_code != null && !lecture_code.trim().isEmpty()) {
                if (!query.isEmpty()) {
                    query += "&";
                }
                query += "lecture_code=" + lecture_code;
            }

            dao.updateHitCount(homework_id);

            Pro_hwDTO dto = dao.findById(homework_id);

            if (dto == null) {
                return new ModelAndView("redirect:/professor/hw/list?" + query);
            }
            dto.setContent(util.htmlSymbols(dto.getContent()));

            Pro_hwDTO prevDto = dao.findByPrev(dto.getHomework_id(), dto.getLecture_code(), schType, kwd);
            Pro_hwDTO nextDto = dao.findByNext(dto.getHomework_id(), dto.getLecture_code(), schType, kwd);

            ModelAndView mav = new ModelAndView("/professor/hw/article");

            if (info != null) {
                String memberId = String.valueOf(info.getMember_id());
                List<LectureDTO> lectures = lectureDao.listsidebar(memberId);
                mav.addObject("lectureList", lectures);
            }

            mav.addObject("dto", dto);
            mav.addObject("page", page);
            mav.addObject("query", query);
            mav.addObject("prevDto", prevDto);
            mav.addObject("nextDto", nextDto);
            mav.addObject("lecture_code", lecture_code);

            return mav;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ModelAndView("redirect:/professor/hw/list?" + query);
    }
	
	@RequestMapping(value = "/professor/hw/update", method = RequestMethod.GET)
	public ModelAndView updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    Pro_hwDAO dao = new Pro_hwDAO();
	    LectureDAO lectureDao = new LectureDAO();
	    String page = req.getParameter("page");

	    HttpSession session = req.getSession();
	    SessionInfo info = (SessionInfo) session.getAttribute("member");

	    try {
	        int homework_id = Integer.parseInt(req.getParameter("homework_id"));
	        Pro_hwDTO dto = dao.findById(homework_id);

	        if (dto == null) {
	            return new ModelAndView("redirect:/professor/hw/list?page=" + page);
	        }

	        // 권한 체크
	        if (!dto.getMember_id().equals(info.getMember_id())) {
	            return new ModelAndView("redirect:/professor/hw/list?page=" + page);
	        }

	        ModelAndView mav = new ModelAndView("/professor/hw/write");
	        mav.addObject("dto", dto);
	        mav.addObject("page", page);
	        mav.addObject("mode", "update");

	        // 사이드바 강의 목록도 같이 넘기기
	        String memberId = String.valueOf(info.getMember_id());
	        List<LectureDTO> lectures = lectureDao.listsidebar(memberId);
	        mav.addObject("lectureList", lectures);

	        return mav;

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return new ModelAndView("redirect:/professor/hw/list?page=" + page);
	}
	
	@RequestMapping(value = "/professor/hw/update", method = RequestMethod.POST)
	public ModelAndView updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    Pro_hwDAO dao = new Pro_hwDAO();
	    FileManager fileManager = new FileManager();

	    HttpSession session = req.getSession();
	    SessionInfo info = (SessionInfo) session.getAttribute("member");

	    String root = session.getServletContext().getRealPath("/");
	    String pathname = root + "uploads" + File.separator + "hw";

	    String page = req.getParameter("page");
	    String lecture_code = req.getParameter("lecture_code");

	    ModelAndView mav;

	    try {
	        if (info == null) {
	            // 로그인 정보 없으면 에러 페이지로 이동
	            mav = new ModelAndView("redirect:/member/login");
	            return mav;
	        }

	        if (lecture_code == null || lecture_code.trim().isEmpty()) {
	            throw new IllegalArgumentException("lecture_code가 null이거나 비어있습니다.");
	        }

	        Pro_hwDTO dto = new Pro_hwDTO();
	        dto.setHomework_id(Integer.parseInt(req.getParameter("homework_id")));
	        dto.setSubject(req.getParameter("subject"));
	        dto.setContent(req.getParameter("content"));
	        dto.setDeadline_date(req.getParameter("deadline_date"));
	        dto.setLecture_code(lecture_code);
	        dto.setMember_id(info.getMember_id());

	        // 파일 업로드 처리
	        Part p = req.getPart("selectfile");
	        MyMultipartFile multiFile = fileManager.doFileUpload(p, pathname);

	        if (multiFile != null) {
	            // 새 파일 업로드되면 기존 파일 삭제
	            String oldSaveFilename = req.getParameter("save_filename");
	            if (oldSaveFilename != null && !oldSaveFilename.isEmpty()) {
	                fileManager.doFiledelete(pathname, oldSaveFilename);
	            }

	            dto.setSave_filename(multiFile.getSaveFilename());
	            dto.setOriginal_filename(multiFile.getOriginalFilename());
	        } else {
	            // 새 파일 없으면 기존 파일 유지
	            dto.setSave_filename(req.getParameter("save_filename"));
	            dto.setOriginal_filename(req.getParameter("original_filename"));
	        }

	        dao.updateHw(dto);

	        mav = new ModelAndView("redirect:/professor/hw/list?page=" + page + "&lecture_code=" + lecture_code);

	    } catch (Exception e) {
	        e.printStackTrace();
	        // 실패 시 목록으로 리다이렉트 (또는 에러 페이지로 보내도 됨)
	        mav = new ModelAndView("redirect:/professor/hw/list?page=" + page + "&lecture_code=" + lecture_code);
	    }

	    return mav;
	}
	 @RequestMapping(value = "/professor/hw/delete", method = RequestMethod.GET)
	    public ModelAndView delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
	            if (schType == null) {
	                schType = "all";
	                kwd = "";
	            }
	            kwd = util.decodeUrl(kwd);

	            if (kwd.length() != 0) {
	                query += "&schType=" + schType + "&kwd=" + util.encodeUrl(kwd);
	            }

	            String lecture_code = req.getParameter("lecture_code");
	            if (lecture_code != null && !lecture_code.trim().isEmpty()) {
	                if (!query.isEmpty()) {
	                    query += "&";
	                }
	                query += "lecture_code=" + lecture_code;
	            }

	            dao.deleteHw(homework_id, info.getMember_id());

	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        return new ModelAndView("redirect:/professor/hw/list?" + query);
	    }

	@RequestMapping(value = "/student/hw/submit", method = RequestMethod.GET)
	public ModelAndView submitForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    ModelAndView mav = new ModelAndView("/student/hw/submit");

	    int homework_id = Integer.parseInt(req.getParameter("homework_id"));
	    mav.addObject("homework_id", homework_id);
	    mav.addObject("mode", "submit");

	    return mav;
	}
	@RequestMapping(value = "/student/hw/submit", method = RequestMethod.POST)
	public ModelAndView hwSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Pro_hwDAO dao = new Pro_hwDAO();
	    FileManager fileManager = new FileManager();

	    HttpSession session = req.getSession();
	    SessionInfo info = (SessionInfo) session.getAttribute("member");

	    String root = session.getServletContext().getRealPath("/");
	    String pathname = root + "uploads" + File.separator + "submit";

	    try {
	        Part p = req.getPart("selectFile");
	        Std_hwDTO dto = new Std_hwDTO();

	        // 반드시 course_id로 받아야 함!
	        dto.setCourse_id(Integer.parseInt(req.getParameter("course_id")));
	        dto.setAssign_name(req.getParameter("assign_name"));
	        dto.setAssign_content(req.getParameter("assign_content"));
	        dto.setAssign_status(1); 

	        MyMultipartFile multiFile = fileManager.doFileUpload(p, pathname);
	        if (multiFile != null) {
	            dto.setSave_filename(multiFile.getSaveFilename());
	            dto.setOriginal_filename(multiFile.getOriginalFilename());
	            dto.setFile_size(multiFile.getSize());
	        }

	        dao.submitHw(dto);

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return new ModelAndView("redirect:/student/hw/list");
	}
	
	@RequestMapping(value = "/hw/download", method = RequestMethod.GET)
	public void downloadFile(HttpServletRequest req, HttpServletResponse resp) throws Exception {
	    String saveFilename = req.getParameter("saveFilename");
	    String originalFilename = req.getParameter("originalFilename");

	    if (saveFilename == null || originalFilename == null) {
	        resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
	        return;
	    }

	    String root = req.getSession().getServletContext().getRealPath("/");
	    String pathname = root + "uploads" + File.separator + "student_hw";

	    FileManager fileManager = new FileManager();
	    boolean success = fileManager.doFiledownload(saveFilename, originalFilename, pathname, resp);

	    if (!success) {
	        resp.setContentType("text/html; charset=UTF-8");
	        PrintWriter out = resp.getWriter();
	        out.print("<script>alert('파일 다운로드에 실패했습니다.');history.back();</script>");
	        out.flush();
	    }
	}
}