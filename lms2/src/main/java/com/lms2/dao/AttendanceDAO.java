package com.lms2.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.lms2.model.Attendance_recordDTO;
import com.lms2.util.DBConn;
import com.lms2.util.DBUtil;

public class AttendanceDAO {
	private Connection conn = DBConn.getConnection();
	
	// 등록
	public void insertAttendance(Attendance_recordDTO dto) throws SQLException{
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			conn.setAutoCommit(false);
			
			sql = "INSERT INTO ATTENDANCE_RECORD(attend_id, attend_date, checkin_time, checkout_time, status, course_id)"
					+ " VALUES(ATTENDANCE_RECORD_SEQ.NEXTVAL, SYSDATE, SYSDATE, SYSDATE, ?, ?)";
		
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, dto.getStatus());
			pstmt.setInt(2, dto.getCourse_id());
			
			pstmt.executeUpdate();
			
			
			pstmt.close();
			pstmt = null;
			
			conn.commit();
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}finally {
			DBUtil.close(pstmt);
			
			try {
				conn.setAutoCommit(true);
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
	}
	
	public List<Attendance_recordDTO> listapplication(int offset, int size){
		List<Attendance_recordDTO> list = new ArrayList<Attendance_recordDTO>();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(" SELECT c.course_id, c.member_id, m.name, c.lecture_code ");
	        sb.append(" FROM COURSE_APPLICATION c ");
	        sb.append(" JOIN member m ON c.member_id = m.member_id ");
	        sb.append(" WHERE c.apply_status = '신청' ");
	        sb.append(" ORDER BY c.member_id DESC ");
	        sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");
	       
			
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setInt(1, offset);
			pstmt.setInt(2, size);
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				Attendance_recordDTO dto = new Attendance_recordDTO();
				
				 dto.setCourse_id(rs.getInt("course_id"));
		         dto.setMember_id(rs.getString("member_id"));
		         dto.setName(rs.getString("name"));
		         dto.setLecture_code(rs.getString("lecture_code"));
		         
		      
		         list.add(dto);

			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		return list;
	}
	
}