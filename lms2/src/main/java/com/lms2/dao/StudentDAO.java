package com.lms2.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.lms2.util.DBUtil;
import com.lms2.model.StudentDTO;
import com.lms2.util.DBConn;

public class StudentDAO {
	
	private Connection conn = DBConn.getConnection();
	
	// 학생 등록
	private void insertStudent(StudentDTO dto) throws SQLException {
		
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

}
