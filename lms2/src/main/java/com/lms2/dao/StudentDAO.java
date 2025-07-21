package com.lms2.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lms2.model.Attendance_recordDTO;
import com.lms2.model.Course_ApplicationDTO;
import com.lms2.model.DepartmentDTO;
import com.lms2.model.LectureDTO;
import com.lms2.model.StudentDTO;
import com.lms2.util.DBConn;
import com.lms2.util.DBUtil;

public class StudentDAO {

	private Connection conn = DBConn.getConnection();

	// 학생 등록
	public void insertStudent(StudentDTO dto) throws SQLException {

		PreparedStatement pstmt = null;
		String sql;

		try {
			conn.setAutoCommit(false);

			sql = " INSERT INTO member(member_id, name, password, role, create_date, modify_date, avatar, email, phone, birth, addr1, addr2, zip) "
					+ " VALUES(?, ?, ?, 1, SYSDATE, SYSDATE, ?, ?, ?, TO_DATE(?, 'YYYY-MM-DD'), ?, ?, ?) ";

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, dto.getMember_id());
			pstmt.setString(2, dto.getName());
			pstmt.setString(3, dto.getPassword());
			pstmt.setString(4, dto.getAvatar());
			pstmt.setString(5, dto.getEmail());
			pstmt.setString(6, dto.getPhone());
			pstmt.setString(7, dto.getBirth());
			pstmt.setString(8, dto.getAddr1());
			pstmt.setString(9, dto.getAddr2());
			pstmt.setString(10, dto.getZip());

			pstmt.executeUpdate();
			pstmt.close();
			pstmt = null;

			sql = " INSERT INTO STUDENT(member_id, grade, admission_date, graduate_date, department_id) "
					+ " VALUES(?, ?, TO_DATE(?, 'YYYY-MM-DD'), TO_DATE(?, 'YYYY-MM-DD'), ?) ";

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, dto.getMember_id());
			pstmt.setInt(2, dto.getGrade());
			pstmt.setString(3, dto.getAdmission_date());
			pstmt.setString(4, dto.getGraduate_date());
			pstmt.setString(5, dto.getDepartment_id());

			pstmt.executeUpdate();
			pstmt.close();
			pstmt = null;

			sql = " INSERT INTO STUDENT_STATUS(history_id, year, semester, grade, academic_status, reg_date, member_id) "
					+ " VALUES(STUDENT_STATUS_SEQ.NEXTVAL, SYSDATE, 1, ?, '입학', SYSDATE, ?) ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, dto.getGrade());
			pstmt.setString(2, dto.getMember_id());
			pstmt.executeUpdate();

			conn.commit();

		} catch (SQLException e) {
			DBUtil.rollback(conn);
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(pstmt);

			try {
				conn.setAutoCommit(true);
			} catch (Exception e2) {
			}
		}

	}

	// 학생수
	public int dataCount() {
		int result = 0;

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = " SELECT COUNT(*) FROM student ";

			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				result = rs.getInt(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return result;

	}
	
	// 검색 조건이 있는 학생 수 조회
	public int dataCount(String schType, String kwd) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append("SELECT COUNT(*) ");
			sb.append("FROM member m ");
			sb.append("JOIN student s ON m.member_id = s.member_id ");
			sb.append("LEFT JOIN department d ON s.department_id = d.department_id ");
			sb.append("WHERE 1=1 ");

			if (schType.equals("all")) {
	            sb.append("AND (INSTR(m.member_id, ?) >= 1 OR INSTR(m.name, ?) >= 1) ");
	        } else if (schType.equals("name")) {
	            sb.append("AND INSTR(m.name, ?) >= 1 ");
	        } else if (schType.equals("department_name")) {
	            sb.append("AND INSTR(d.department_name, ?) >= 1 ");
	        } else {
	            sb.append("AND INSTR(" + schType + ", ?) >= 1 ");
	        }
			
			pstmt = conn.prepareStatement(sb.toString());

			pstmt.setString(1, kwd);
			if (schType.equals("all")) {
				pstmt.setString(2, kwd);
			}

			rs = pstmt.executeQuery();
			if (rs.next()) {
				result = rs.getInt(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return result;
	}

	// 학생 리스트
	public List<StudentDTO> listStudent(int offset, int size) {
		List<StudentDTO> list = new ArrayList<>();

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append("SELECT m.member_id, m.name, s.grade, ");
			sb.append("TO_CHAR(s.admission_date, 'YYYY-MM-DD') AS admission_date, ");
			sb.append("TO_CHAR(s.graduate_date, 'YYYY-MM-DD') AS graduate_date, ");
			sb.append("m.phone, TO_CHAR(m.birth, 'YYYY-MM-DD') AS birth, m.email, ");
			sb.append("s.department_id, d.department_name, ");
			sb.append("(SELECT academic_status FROM student_status ss ");
			sb.append(" WHERE ss.member_id = s.member_id ");
			sb.append(" ORDER BY ss.reg_date DESC FETCH FIRST 1 ROWS ONLY) AS academic_status ");
			sb.append("FROM member m ");
			sb.append("JOIN student s ON m.member_id = s.member_id ");
			sb.append("LEFT JOIN department d ON s.department_id = d.department_id ");
			sb.append("ORDER BY m.member_id DESC ");
			sb.append("OFFSET ? ROWS FETCH FIRST ? ROWS ONLY");

			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, offset);
			pstmt.setInt(2, size);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				StudentDTO dto = new StudentDTO();
				dto.setMember_id(rs.getString("member_id"));
				dto.setName(rs.getString("name"));
				dto.setGrade(rs.getInt("grade"));
				dto.setAdmission_date(rs.getString("admission_date"));
				dto.setGraduate_date(rs.getString("graduate_date"));
				dto.setPhone(rs.getString("phone"));
				dto.setBirth(rs.getString("birth"));
				dto.setEmail(rs.getString("email"));
				dto.setDepartment_id(rs.getString("department_id"));
				dto.setDepartment_name(rs.getString("department_name"));
				dto.setAcademic_status(rs.getString("academic_status"));

				list.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return list;
	}
	
	// 검색에서 학생 리스트
	public List<StudentDTO> listStudent(int offset, int size, String schType, String kwd) {
	    List<StudentDTO> list = new ArrayList<>();
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    StringBuilder sb = new StringBuilder();

	    try {
	        sb.append("SELECT m.member_id, m.name, s.grade, ");
	        sb.append("TO_CHAR(s.admission_date, 'YYYY-MM-DD') AS admission_date, ");
	        sb.append("TO_CHAR(s.graduate_date, 'YYYY-MM-DD') AS graduate_date, ");
	        sb.append("m.phone, TO_CHAR(m.birth, 'YYYY-MM-DD') AS birth, m.email, ");
	        sb.append("s.department_id, d.department_name, ");
	        sb.append("(SELECT academic_status FROM student_status ss ");
	        sb.append(" WHERE ss.member_id = s.member_id ");
	        sb.append(" ORDER BY ss.reg_date DESC FETCH FIRST 1 ROWS ONLY) AS academic_status ");
	        sb.append("FROM member m ");
	        sb.append("JOIN student s ON m.member_id = s.member_id ");
	        sb.append("LEFT JOIN department d ON s.department_id = d.department_id ");
	        sb.append("WHERE 1=1 ");

	        if (schType.equals("all")) {
	            sb.append("AND (INSTR(m.member_id, ?) >= 1 OR INSTR(m.name, ?) >= 1) ");
	        } else if (schType.equals("name")) {
	            sb.append("AND INSTR(m.name, ?) >= 1 ");
	        } else if (schType.equals("department_name")) {
	            sb.append("AND INSTR(d.department_name, ?) >= 1 ");
	        } else {
	            sb.append("AND INSTR(" + schType + ", ?) >= 1 ");
	        }

	        sb.append("ORDER BY m.member_id DESC ");
	        sb.append("OFFSET ? ROWS FETCH FIRST ? ROWS ONLY");

	        pstmt = conn.prepareStatement(sb.toString());

	        if (schType.equals("all")) {
	            pstmt.setString(1, kwd);
	            pstmt.setString(2, kwd);
	            pstmt.setInt(3, offset);
	            pstmt.setInt(4, size);
	        } else {
	            pstmt.setString(1, kwd);
	            pstmt.setInt(2, offset);
	            pstmt.setInt(3, size);
	        }

	        rs = pstmt.executeQuery();

	        while (rs.next()) {
	            StudentDTO dto = new StudentDTO();
	            dto.setMember_id(rs.getString("member_id"));
	            dto.setName(rs.getString("name"));
	            dto.setGrade(rs.getInt("grade"));
	            dto.setAdmission_date(rs.getString("admission_date"));
	            dto.setGraduate_date(rs.getString("graduate_date"));
	            dto.setPhone(rs.getString("phone"));
	            dto.setBirth(rs.getString("birth"));
	            dto.setEmail(rs.getString("email"));
	            dto.setDepartment_id(rs.getString("department_id"));
	            dto.setDepartment_name(rs.getString("department_name"));
	            dto.setAcademic_status(rs.getString("academic_status"));

	            list.add(dto);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        DBUtil.close(rs);
	        DBUtil.close(pstmt);
	    }

	    return list;
	}


	// 학번으로 학생 찾기
	public StudentDTO findById(String member_id) {
	    StudentDTO dto = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    StringBuilder sb = new StringBuilder();

	    try {
	        sb.append("SELECT m.member_id, m.name, m.password, m.role, ");
	        sb.append("TO_CHAR(m.create_date, 'YYYY-MM-DD') AS create_date, ");
	        sb.append("TO_CHAR(m.modify_date, 'YYYY-MM-DD') AS modify_date, ");
	        sb.append("m.avatar, m.email, m.phone, TO_CHAR(m.birth, 'YYYY-MM-DD') AS birth, ");
	        sb.append("m.addr1, m.addr2, m.zip, ");
	        sb.append("s.grade, TO_CHAR(s.admission_date, 'YYYY-MM-DD') AS admission_date, ");
	        sb.append("TO_CHAR(s.graduate_date, 'YYYY-MM-DD') AS graduate_date, ");
	        sb.append("s.department_id, d.department_name, ");
	        sb.append("(SELECT academic_status FROM student_status ss ");
	        sb.append(" WHERE ss.member_id = m.member_id ");
	        sb.append(" ORDER BY ss.reg_date DESC FETCH FIRST 1 ROWS ONLY) AS academic_status ");
	        sb.append("FROM member m ");
	        sb.append("JOIN student s ON m.member_id = s.member_id ");
	        sb.append("JOIN department d ON s.department_id = d.department_id ");
	        sb.append("WHERE m.member_id = ?");

	        pstmt = conn.prepareStatement(sb.toString());
	        pstmt.setString(1, member_id);
	        rs = pstmt.executeQuery();

	        if (rs.next()) {
	            dto = new StudentDTO();
	            dto.setMember_id(rs.getString("member_id"));
	            dto.setName(rs.getString("name"));
	            dto.setPassword(rs.getString("password"));
	            dto.setRole(rs.getInt("role"));
	            dto.setCreate_date(rs.getString("create_date"));
	            dto.setModify_date(rs.getString("modify_date"));
	            dto.setAvatar(rs.getString("avatar"));

	            String email = rs.getString("email");
	            dto.setEmail(email);
	            if (email != null) {
	                String[] ss = email.split("@");
	                if (ss.length == 2) {
	                    dto.setEmail1(ss[0]);
	                    dto.setEmail2(ss[1]);
	                }
	            }

	            dto.setPhone(rs.getString("phone"));
	            dto.setBirth(rs.getString("birth"));
	            dto.setAddr1(rs.getString("addr1"));
	            dto.setAddr2(rs.getString("addr2"));
	            dto.setZip(rs.getString("zip"));
	            dto.setGrade(rs.getInt("grade"));
	            dto.setAdmission_date(rs.getString("admission_date"));
	            dto.setGraduate_date(rs.getString("graduate_date"));
	            dto.setDepartment_id(rs.getString("department_id"));
	            dto.setDepartment_name(rs.getString("department_name"));

	            dto.setAcademic_status(rs.getString("academic_status"));
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        DBUtil.close(rs);
	        DBUtil.close(pstmt);
	    }

	    return dto;
	}

	// 학생 수정(학생이 직접 수정)
	public void updateStudent(StudentDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = " UPDATE member SET modify_date = SYSDATE, avatar = ?, email = ?, phone = ?, birth = TO_DATE(?, 'YYYY-MM-DD'), addr1 = ?, addr2 = ?, zip = ? "
					+ " WHERE member_id = ? ";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getAvatar());
			pstmt.setString(2, dto.getEmail());
			pstmt.setString(3, dto.getPhone());
			pstmt.setString(4, dto.getBirth());
			pstmt.setString(5, dto.getAddr1());
			pstmt.setString(6, dto.getAddr2());
			pstmt.setString(7, dto.getZip());
			pstmt.setString(8, dto.getMember_id());

			pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(pstmt);
		}

	}

	// 관리자 학생 수정
	public void updateStudentByAdmin(StudentDTO dto) throws SQLException {
	    PreparedStatement pstmt = null;
	    String sql;

	    try {
	        // member 테이블 수정
	        sql = " UPDATE member SET name = ?, password = ?, modify_date = SYSDATE, avatar = ?, email = ?, phone = ?, birth = TO_DATE(?, 'YYYY-MM-DD'), addr1 = ?, addr2 = ? WHERE member_id = ? ";
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, dto.getName());
	        pstmt.setString(2, dto.getPassword());
	        pstmt.setString(3, dto.getAvatar());
	        pstmt.setString(4, dto.getEmail());
	        pstmt.setString(5, dto.getPhone());
	        pstmt.setString(6, dto.getBirth());
	        pstmt.setString(7, dto.getAddr1());
	        pstmt.setString(8, dto.getAddr2());
	        pstmt.setString(9, dto.getMember_id());
	        pstmt.executeUpdate();
	        pstmt.close();

	        // student 테이블 수정
	        sql = " UPDATE student SET grade = ?, admission_date = TO_DATE(?, 'YYYY-MM-DD'), graduate_date = TO_DATE(?, 'YYYY-MM-DD'), department_id = ? WHERE member_id = ? ";
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setInt(1, dto.getGrade());
	        pstmt.setString(2, dto.getAdmission_date());
	        pstmt.setString(3, dto.getGraduate_date());
	        pstmt.setString(4, dto.getDepartment_id());
	        pstmt.setString(5, dto.getMember_id());
	        pstmt.executeUpdate();
	        pstmt.close();

	        // 가장 최신 상태 UPDATE
	        sql = "UPDATE student_status SET academic_status = ?, grade = ?, reg_date = SYSDATE "
	            + "WHERE member_id = ? AND reg_date = ("
	            + "SELECT MAX(reg_date) FROM student_status WHERE member_id = ?)";
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, dto.getAcademic_status());
	        pstmt.setInt(2, dto.getGrade());
	        pstmt.setString(3, dto.getMember_id());
	        pstmt.setString(4, dto.getMember_id());
	        pstmt.executeUpdate();

	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new SQLException(e);
	    } finally {
	        DBUtil.close(pstmt);
	    }
	}

	// 학생 삭제(관리자가 삭제)
	public void deleteStudentByAdmin(String member_id) throws SQLException {

		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = " DELETE FROM student WHERE member_id = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, member_id);
			pstmt.executeUpdate();

			pstmt.close();
			pstmt = null;

			sql = " DELETE FROM member WHERE member_id = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, member_id);
			pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}

	// 강의 리스트 ( 수강신청 하기 위한 리스트)
	public List<LectureDTO> listLecture(int offset, int size) {
		List<LectureDTO> list = new ArrayList<LectureDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append(
					" SELECT lecture_code, subject, grade, classroom, division, lecture_year, semester, capacity, credit, department_id ");
			sb.append(" FROM LECTURE l");
			sb.append(" JOIN STUDENT s ON s.department_id = l.department_id ");
			// sb.append(" WHERE department_id = ? ");
			sb.append(" ORDER BY lecture_code DESC ");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");

			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, offset);
			pstmt.setInt(2, size);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				LectureDTO dto = new LectureDTO();

				dto.setLecture_code(rs.getString("lecture_code"));
				dto.setSubject(rs.getString("subject"));
				dto.setGrade(rs.getInt("grade"));
				dto.setClassroom(rs.getString("classroom"));
				dto.setDivision(rs.getString("division"));
				dto.setLecture_year(rs.getInt("lecture_year"));
				dto.setSemester(rs.getString("semester"));
				dto.setCapacity(rs.getInt("capacity"));
				dto.setCredit(rs.getDouble("credit"));
				dto.setDepartment_id(rs.getString("department_id"));

				list.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return list;
	}

	// 학생 수강신청
	public void insertCourse(Course_ApplicationDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = " INSERT INTO COURSE_APPLICATION (course_id, apply_status, member_id, lecture_code) "
					+ " VALUES(COURSE_APPLICATION_SEQ.NEXTVAL, '신청', ?, ?)  ";
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, dto.getMember_id());
			pstmt.setString(2, dto.getLecture_code());
			pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}

	}

	// 수강 신청한 리스트
	public List<Course_ApplicationDTO> listCourse(String member_id) {
		List<Course_ApplicationDTO> list = new ArrayList<Course_ApplicationDTO>();

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append(" SELECT l.member_id, m.name, l.lecture_code, l.subject, l.grade, l.classroom, l.division, ");
			sb.append(" l.lecture_year, l.semester, l.capacity, l.credit ");
			sb.append(" FROM LECTURE l ");
			sb.append(" JOIN COURSE_APPLICATION c ON c.lecture_code = l.lecture_code ");
			sb.append(" JOIN PROFESSOR p ON l.member_id = p.member_id ");
			sb.append(" JOIN MEMBER m ON m.member_id = p.member_id ");
			sb.append(" WHERE c.member_id = ? AND c.apply_status = '신청' ");
			sb.append(" ORDER BY c.course_id DESC ");

			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, member_id);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				LectureDTO lDto = new LectureDTO();
				Course_ApplicationDTO cDto = new Course_ApplicationDTO();

				lDto.setLecture_code(rs.getString("lecture_code"));
				lDto.setSubject(rs.getString("subject"));
				lDto.setGrade(rs.getInt("grade"));
				lDto.setClassroom(rs.getString("classroom"));
				lDto.setDivision(rs.getString("division"));
				lDto.setLecture_year(rs.getInt("lecture_year"));
				lDto.setSemester(rs.getString("semester"));
				lDto.setCapacity(rs.getInt("capacity"));
				lDto.setCredit(rs.getDouble("credit"));
				lDto.setName(rs.getString("name"));
				cDto.setMember_id(rs.getString("member_id"));
				cDto.setLecture(lDto);

				list.add(cDto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;

	}

	// 특정 학생의 출석 리스트
	public List<Attendance_recordDTO> listAttendance(String member_id) {
		List<Attendance_recordDTO> list = new ArrayList<Attendance_recordDTO>();

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append(" SELECT attend_date, checkin_time, checkout_time, a.status, c.member_id, week, lecture_code, ");
			sb.append(
					" CASE a.status WHEN 0 THEN '결석' WHEN 1 THEN '출석' WHEN 2 THEN '지각' ELSE '미체크' END AS attendance_status ");
			sb.append(" FROM Attendance_record a ");
			sb.append(" JOIN COURSE_APPLICATION c ON a.course_id = c.course_id ");
			sb.append(" WHERE c. member_id = ? ");

			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, member_id);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				Attendance_recordDTO dto = new Attendance_recordDTO();

				dto.setAttend_date(rs.getString("attend_date"));
				dto.setCheckin_time(rs.getString("checkin_time"));
				dto.setCheckout_time(rs.getString("checkout_time"));
				dto.setStatus(rs.getInt("status"));
				dto.setMember_id(rs.getString("member_id"));
				dto.setWeek(rs.getInt("week"));
				dto.setLecture_code(rs.getString("lecture_code"));

				list.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return list;
	}

	// 학적 상태 목록 조회
	public List<Map<String, Object>> getStudentStatusList() throws SQLException {
		List<Map<String, Object>> list = new ArrayList<>();

		String sql = "SELECT DISTINCT academic_status AS status_name " + "FROM student_status ORDER BY academic_status";

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			int i = 1;
			while (rs.next()) {
				Map<String, Object> map = new HashMap<>();
				map.put("status_id", i++); // 순번으로라도 ID 부여
				map.put("status_name", rs.getString("status_name"));
				list.add(map);
			}
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return list;
	}
	
	// 학과 선택(조회)
	public List<DepartmentDTO> listDepartment() throws SQLException {
	    List<DepartmentDTO> list = new ArrayList<>();

	    String sql = "SELECT department_id, department_name FROM department ORDER BY department_id";

	    PreparedStatement pstmt = null;
	    ResultSet rs = null;

	    try {
	        pstmt = conn.prepareStatement(sql);
	        rs = pstmt.executeQuery();

	        while (rs.next()) {
	            DepartmentDTO dto = new DepartmentDTO();
	            dto.setDepartment_id(rs.getString("department_id"));
	            dto.setDepartment_name(rs.getString("department_name"));
	            list.add(dto);
	        }
	    } finally {
	        DBUtil.close(rs);
	        DBUtil.close(pstmt);
	    }

	    return list;
	}
}
