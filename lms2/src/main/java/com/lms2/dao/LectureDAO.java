package com.lms2.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.lms2.model.LectureDTO;
import com.lms2.util.DBConn;
import com.lms2.util.DBUtil;

public class LectureDAO {
	private Connection conn = DBConn.getConnection();
	
	public void insertLecture(LectureDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
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
			pstmt.setString(9, dto.getCredit());
			pstmt.setString(10, dto.getDepartment_id());
			pstmt.setString(11, dto.getMember_id());
			
			pstmt.executeUpdate();
			
			pstmt.close();
			pstmt = null;
			
			sql = "INSERT INTO LECTURE_FILE(file_id, save_filename, original_filename, file_size, lecture_code, reg_date) "
		             + "VALUES(LECTURE_FILE_SEQ.NEXTVAL, ?, ?, ?, ?, SYSDATE)";

			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getSava_filename());
			pstmt.setString(2, dto.getOriginal_filename());
			pstmt.setInt(3, dto.getFile_size());
			pstmt.setString(4, dto.getLecture_code());
			
			pstmt.executeUpdate();
			
			conn.commit();
			
		
		} catch (SQLException e) {
			DBUtil.rollback(conn);
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
			
			try {
				conn.setAutoCommit(true);
			} catch (Exception e2) {
			
			}
		}
		
	}
}
