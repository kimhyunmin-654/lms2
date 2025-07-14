package com.lms2.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.lms2.util.DBUtil;
import com.lms2.model.StudentDTO;
import com.lms2.util.DBConn;

public class StudentDAO {
	
	private Connection conn = DBConn.getConnection();
	
	// 학생 등록
	public void insertStudent(StudentDTO dto) throws SQLException {
		
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			conn.setAutoCommit(false);
			
			sql = " INSERT INTO member(member_id, name, password, role, create_date, modify_date, avatar, email, phone, birth, addr1, addr2) "
					+ " VALUES(?, ?, ?, 1, SYSDATE, SYSDATE, ?, ?, ?, TO_DATE(?, 'YYYY-MM-DD'), ?, ?) ";
			
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
	
	public StudentDTO findbyId(String member_id) {
		
		StudentDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = " ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, member_id);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new StudentDTO();
				
				dto.setMember_id(rs.getString("member_id"));
				dto.setName(rs.getString("name"));
			}
		}
		
		return dto;
	}

}
