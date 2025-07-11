package com.lms2.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.lms2.model.LectureDTO;
import com.lms2.util.DBConn;
import com.lms2.util.DBUtil;

public class LectureDAO {
	private Connection conn = DBConn.getConnection();
	
	public void insertLecture(LectureDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
	
		
		try {
			sql = "INSERT INTO LECTURE(lecture_code, subject, grade, classroom, division, lecture_year, semester, capacity, credit, department_id, member_id)"
					+ " VALUES(?,?,?,?,?,?,?,?,?,?,?)";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getLecture_code());
			pstmt.setString(2, dto.getSubject());
			pstmt.setInt(3, dto.getGrade());
			pstmt.setString(4, dto.getClassroom());
			pstmt.setString(5, dto.getDivision());
			pstmt.setInt(6, dto.getLecture_year());
			pstmt.setString(7, dto.getSemester());
			pstmt.setInt(8, dto.getCapacity());
			//pstmt.setInt(9, dto.getCredit());
			pstmt.setString(10, dto.getDepartment_id());
			pstmt.setString(11, dto.getMember_id());
			
		
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
		
	}
}
