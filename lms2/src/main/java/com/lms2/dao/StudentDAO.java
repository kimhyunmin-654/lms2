package com.lms2.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.lms2.util.DBUtil;
import com.lms2.model.Course_ApplicationDTO;
import com.lms2.model.LectureDTO;
import com.lms2.model.StudentDTO;
import com.lms2.model.StudentStatusDTO;
import com.lms2.util.DBConn;

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
			
			if(rs.next()) {
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
		List<StudentDTO> list = new ArrayList<StudentDTO>();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(" SELECT m.member_id, name, grade, admission_date, graduate_date, phone, birth ");
			sb.append(" FROM member m ");
			sb.append(" JOIN student s ON m.member_id = s.member_id ");
			sb.append(" ORDER BY m.member_id DESC ");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");
			
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, offset);
			pstmt.setInt(2, size);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				StudentDTO dto = new StudentDTO();
				
				dto.setMember_id(rs.getString("member_id"));
				dto.setName(rs.getString("name"));
				dto.setGrade(rs.getInt("grade"));
				dto.setAdmission_date("admission_date");
				dto.setGraduate_date("graduate_date");
				dto.setPhone(rs.getString("phone"));
				dto.setBirth(rs.getString("birth"));
				
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
	public StudentDTO findbyId(String member_id) {
		
		StudentDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = " SELECT m.member_id, name, password, role, TO_CHAR(create_date, 'YYYY-MM-DD') create_date, TO_CHAR(modify_date, 'YYYY-MM-DD') modify_date, avatar, email, phone, birth, addr1, addr2, grade, admission_date, TO_CHAR(admission_date, 'YYYY-MM-DD') admission_date, TO_CHAR(graduate_date, 'YYYY-MM-DD') graduate_date, department_id "
					+ " FROM member m "
					+ " JOIN student s ON s.member_id = m.member_id "
					+ " WHERE m.member_id = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, member_id);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new StudentDTO();
				
				dto.setMember_id(rs.getString("member_id"));
				dto.setName(rs.getString("name"));
				dto.setPassword(rs.getString("password"));
				dto.setRole(rs.getInt("role"));
				dto.setCreate_date(rs.getString("create_date"));
				dto.setModify_date(rs.getString("modify_date"));
				dto.setAvatar(rs.getString("avatar"));
				dto.setEmail(rs.getString("email"));
				if(dto.getEmail() != null) {
					String[] ss = dto.getEmail().split("@");
					if(ss.length == 2) {
						dto.setEmail1(ss[0]);
						dto.setEmail2(ss[1]);
					}
				}
				dto.setPhone(rs.getString("phone"));
				dto.setBirth(rs.getString("birth"));
				dto.setAddr1(rs.getString("addr1"));
				dto.setAddr2(rs.getString("addr2"));
				dto.setGrade(rs.getInt("grade"));
				dto.setAdmission_date(rs.getString("admission_date"));
				dto.setGraduate_date(rs.getString("graduate_date"));
				dto.setDepartment_id(rs.getString("department_id"));
				
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
			sql =  " UPDATE member SET password = ? , modify_date = SYSDATE, avatar = ?, email = ?, phone = ?, birth = TO_DATE(?, 'YYYY-MM-DD'), addr1 = ?, addr2 = ? "
					+ " WHERE member_id = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getPassword());
			pstmt.setString(2, dto.getAvatar());
			pstmt.setString(3, dto.getEmail());
			pstmt.setString(4, dto.getPhone());
			pstmt.setString(5, dto.getBirth());
			pstmt.setString(6, dto.getAddr1());
			pstmt.setString(7, dto.getAddr2());
			pstmt.setString(8, dto.getMember_id());
			
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(pstmt);
		}
		
	}
	
	// 학생 수정(관리자가 직접 수정)
		public void updateStudentByAdmin(StudentDTO dto) throws SQLException {
			PreparedStatement pstmt = null;
			String sql;
			
			try {
				sql =  " UPDATE member SET name = ?, password = ?, modify_date = SYSDATE, avatar = ?, email = ?, phone = ?, birth = TO_DATE(?, 'YYYY-MM-DD'), addr1 = ?, addr2 = ? "
						+ " WHERE member_id = ? ";
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
				pstmt = null;
				
				sql = " UPDATE student SET grade = ?,  admission_date = TO_DATE(?, 'YYYY-MM-DD'),  graduate_date = TO_DATE(?, 'YYYY-MM-DD'), department_id = ? "
						+ " WHERE member_id = ? ";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, dto.getGrade());
				pstmt.setString(2, dto.getAdmission_date());
				pstmt.setString(3, dto.getGraduate_date());
				pstmt.setString(4, dto.getDepartment_id());
				pstmt.setString(5, dto.getMember_id());
				
				pstmt.executeUpdate();
				
			} catch (Exception e) {
				e.printStackTrace();
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

		
		//  강의 리스트 ( 수강신청 하기 위한 리스트)
		public List<LectureDTO> listLecture(int offset, int size){
			List<LectureDTO> list = new ArrayList<LectureDTO>();
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			StringBuilder sb = new StringBuilder();
			
			try {
				sb.append(" SELECT lecture_code, subject, grade, classroom, division, lecture_year, semester, capacity, credit, department_id " );
				sb.append(" FROM LECTURE l");
				sb.append(" JOIN STUDENT s ON s.department_id = l.department_id ");
				// sb.append(" WHERE department_id = ? ");
				sb.append(" ORDER BY lecture_code DESC ");
				sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");
				
				pstmt = conn.prepareStatement(sb.toString());
				pstmt.setInt(1, offset);
				pstmt.setInt(2, size);
				rs = pstmt.executeQuery();
				
				while(rs.next()) {
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
				pstmt.execute();
				
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			} finally {
				DBUtil.close(pstmt);
			}
			
		}
		
		// 수강 신청한 리스트 (작성중)
		public List<Course_ApplicationDTO> listCourse(String department_id) {
			List<Course_ApplicationDTO> list = new ArrayList<Course_ApplicationDTO>();
			
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			StringBuilder sb = new StringBuilder();
			
			try {
				sb.append(" SELECT l.lecture_code, subject, grade, classroom, division, lecture_year, semester, capacity, credit ");
				sb.append(" FROM LECTURE l ");
				sb.append(" JOIN COURSE_APPLICATION c ON c.lecture_code = l.lecture_code ");
				sb.append(" JOIN STUDENT s ON s.member_id = c.member_id ");
				sb.append(" WHERE apply_status = '신청' ");
				sb.append(" ORDER BY course_id DESC ");
				
				pstmt = conn.prepareStatement(sb.toString());
				rs = pstmt.executeQuery();
				
				while(rs.next()) {
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
					lDto.setCredit(rs.getInt("credit"));
					
					cDto.setLecture(lDto);
					
					list.add(cDto);
				}
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return list;
			
		}


}
