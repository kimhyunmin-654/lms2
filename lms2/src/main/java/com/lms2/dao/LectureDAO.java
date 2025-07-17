package com.lms2.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
			pstmt.setDouble(9, dto.getCredit());
			pstmt.setString(10, dto.getDepartment_id());
			pstmt.setString(11, dto.getMember_id());
			
			pstmt.executeUpdate();
			
			pstmt.close();
			pstmt = null;
			
			sql = "INSERT INTO LECTURE_FILE(file_id, save_filename, original_filename, file_size, lecture_code, reg_date) "
		             + "VALUES(LECTURE_FILE_SEQ.NEXTVAL, ?, ?, ?, ?, SYSDATE)";

			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getSave_filename());
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
	
	public List<LectureDTO> listLecture(int offset, int size){
		List<LectureDTO> list = new ArrayList<LectureDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(" SELECT l.member_id, name, lecture_code, subject, grade, classroom, division, lecture_year, semester, capacity, credit, department_id ");	
			sb.append(" FROM lecture l ");
			sb.append(" JOIN member m ON l.member_id = m.member_id ");
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
				dto.setName(rs.getString("name"));
				
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
	
	// 강의수
		public int dataCount() {
			int result = 0;
			
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql;
			
			try {
				sql = " SELECT COUNT(*) FROM lecture ";
				
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
		
		// 사이드바
		public List<LectureDTO> listsidebar(String member_id) {
			List<LectureDTO> list = new ArrayList<>();
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				String sql = "SELECT subject FROM lecture WHERE member_id = ?";

				pstmt = conn.prepareStatement(sql);

				pstmt.setString(1, member_id);
				rs = pstmt.executeQuery();

				while (rs.next()) {
					LectureDTO dto = new LectureDTO();
					dto.setSubject(rs.getString("subject"));
					list.add(dto);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			return list;
		}
}
