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
	public void insertAttendance(Attendance_recordDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			conn.setAutoCommit(false);
			
			sql = " SELECT ATTEND_ID FROM ATTENDANCE_RECORD WHERE COURSE_ID = ? AND week = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, dto.getCourse_id());
			pstmt.setInt(2, dto.getWeek());
			rs = pstmt.executeQuery();
			
			boolean status = false;
			int attend_id = 0;
			
			if(rs.next()) {
				status = true;
				attend_id = rs.getInt("attend_id");
			}
			
			rs.close();
			pstmt.close();
			rs = null;
			pstmt = null;
			
			if(status) {
				
				sql = " UPDATE ATTENDANCE_RECORD SET checkin_time = SYSDATE, checkout_time = SYSDATE, status = ?"
						+ " WHERE attend_id = ?  ";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, dto.getStatus());
				pstmt.setInt(2, attend_id);
				
			} else {
				
				sql = "INSERT INTO ATTENDANCE_RECORD(attend_id, attend_date, checkin_time, checkout_time, status, course_id, week)"
						+ " VALUES(ATTENDANCE_RECORD_SEQ.NEXTVAL, SYSDATE, SYSDATE, SYSDATE, ?, ?, ?)";

				pstmt = conn.prepareStatement(sql);

				pstmt.setInt(1, dto.getStatus());
				pstmt.setInt(2, dto.getCourse_id());
				pstmt.setInt(3, dto.getWeek());
			}
			
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
			DBUtil.close(rs);

			try {
				conn.setAutoCommit(true);
			} catch (Exception e2) {
			}
		}
	}

	public List<Attendance_recordDTO> listapplication(int offset, int size, String lecture_code) {
		List<Attendance_recordDTO> list = new ArrayList<Attendance_recordDTO>();

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append(" SELECT c.course_id, c.member_id, m.name, c.lecture_code ");
			sb.append(" FROM COURSE_APPLICATION c ");
			sb.append(" JOIN member m ON c.member_id = m.member_id ");
			sb.append(" WHERE c.apply_status = '신청' ");
			sb.append(" AND c.lecture_code = ?  ");
			sb.append(" ORDER BY c.member_id DESC ");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");

			pstmt = conn.prepareStatement(sb.toString());

			pstmt.setString(1, lecture_code);
			pstmt.setInt(2, offset);
			pstmt.setInt(3, size);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				Attendance_recordDTO dto = new Attendance_recordDTO();

				dto.setCourse_id(rs.getInt("course_id"));
				dto.setMember_id(rs.getString("member_id"));
				dto.setName(rs.getString("name"));
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

	// 출석 개수
	public int dataCount(String member_id, int status, int week) {
		int result = 0;

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append(" SELECT COUNT(*) ");
			sb.append(" FROM Attendance_record a ");
			sb.append(" JOIN COURSE_APPLICATION c ON a.course_id = c.course_id ");
			sb.append(" WHERE c.member_id = ? ");
			sb.append(" AND a.status = ? ");
			sb.append(" AND a.week = ? ");

			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, member_id);
			pstmt.setInt(2, status);
			pstmt.setInt(3, week);

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


	// 특정 강의의 특정 주차 출석 현황 조회 (전체 수강생 포함)
	public List<Attendance_recordDTO> listAttendanceByWeek(String lecture_code, int week) {
		List<Attendance_recordDTO> list = new ArrayList<Attendance_recordDTO>();

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append(" SELECT c.member_id, m.name, c.course_id, ");
			sb.append(" a.attend_date, a.checkin_time, a.checkout_time, a.status, a.week ");
			sb.append(" FROM COURSE_APPLICATION c ");
			sb.append(" JOIN MEMBER m ON c.member_id = m.member_id ");
			sb.append(" LEFT JOIN ATTENDANCE_RECORD a ON c.course_id = a.course_id AND a.week = ? ");
			sb.append(" WHERE c.lecture_code = ? ");
			sb.append(" ORDER BY c.member_id ");

			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, week);
			pstmt.setString(2, lecture_code);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				Attendance_recordDTO dto = new Attendance_recordDTO();

				dto.setMember_id(rs.getString("member_id"));
				dto.setName(rs.getString("name"));
				dto.setCourse_id(rs.getInt("course_id"));

				dto.setAttend_date(rs.getString("attend_date"));
				dto.setCheckin_time(rs.getString("checkin_time"));
				dto.setCheckout_time(rs.getString("checkout_time"));

				Integer status = rs.getObject("status", Integer.class);
				dto.setStatus(status != null ? status : -1);

				dto.setWeek(rs.getInt("week"));

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
	
	
	// 전체 주차 (학생용)
	public int dataCountAll(String member_id, int status, String lecture_code) {
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		int result = 0;

		try {
				
			sql = " SELECT COUNT(*) "
					+ " FROM attendance_record r "
					+ " JOIN COURSE_APPLICATION m ON r.course_id = m.course_id "
					+ " JOIN STUDENT s ON m.member_id = s.member_id "
					+ " WHERE s.member_id=? AND status=? AND lecture_code = ? ";
		
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, member_id);
			pstmt.setInt(2, status);
			pstmt.setString(3, lecture_code);
			
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
	
	public List<Attendance_recordDTO> listAttendanceByLectureAndStudent(String lecture_code, String member_id) throws SQLException {
	    List<Attendance_recordDTO> list = new ArrayList<>();
	    
	    PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

	    try {
	    	
	   		sql = "SELECT a.* "
	   				+ " FROM ATTENDANCE_RECORD a "
	               + " JOIN COURSE_APPLICATION c ON a.course_id = c.course_id "
	               + " WHERE c.lecture_code = ? AND c.member_id = ? "
	               + " ORDER BY a.week ASC ";
	   		
	   		pstmt = conn.prepareStatement(sql);
	    
	        pstmt.setString(1, lecture_code);
	        pstmt.setString(2, member_id);
	        rs = pstmt.executeQuery();
	        
	            while (rs.next()) {
	                Attendance_recordDTO dto = new Attendance_recordDTO();
	                
	                dto.setAttend_id(rs.getInt("attend_id"));
	                dto.setWeek(rs.getInt("week"));
	                Integer status = rs.getObject("status", Integer.class);
					dto.setStatus(status != null ? status : -1);
	                
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

}