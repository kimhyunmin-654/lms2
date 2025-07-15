package com.lms2.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
//import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.lms2.model.ProfessorDTO;
import com.lms2.util.DBConn;
import com.lms2.util.DBUtil;

public class ProfessorDAO {
	private Connection conn = DBConn.getConnection();
	
	// 교수 등록
	public void insertProfessor(ProfessorDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			conn.setAutoCommit(false);

			sql = "INSERT INTO member(member_id, name, password, role, create_date, modify_date, avatar, email, phone, birth, addr1, addr2) "
				    + "VALUES(?,?,?,51,SYSDATE,SYSDATE,?,?,?,TO_DATE(?,'YYYY-MM-DD'),?,?)";
			
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
			
			sql = "INSERT INTO professor(member_id, position, department_id)"
					+ "VALUES(?,?,?)";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getMember_id());
			pstmt.setString(2, dto.getPosition());
			pstmt.setString(3, dto.getDepartment_id());
			
			pstmt.executeUpdate();
			
			conn.commit();
			
		} catch (SQLException e) {
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
	
	// 테이블 조인
	public ProfessorDTO findById(String member_id) {
		ProfessorDTO dto = null;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
		    sql = "SELECT m.member_id, m.name, m.password, m.role, m.create_date, m.modify_date, m.avatar, m.email, m.phone, m.birth, m.addr1, m.addr2, "
	                + " p.position, p.department_id "
	                + " FROM member m "
	                + " LEFT OUTER JOIN professor p ON m.member_id = p.member_id "
	                + " WHERE m.member_id = ?";
			
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, member_id);
		
		rs = pstmt.executeQuery();
		
		if(rs.next()) {
			dto = new ProfessorDTO();
			 dto.setMember_id(rs.getString("member_id"));
	            dto.setName(rs.getString("name"));
	            dto.setPassword(rs.getString("password"));
	            dto.setRole(rs.getInt("role"));
	            dto.setCreate_date(rs.getString("create_date"));
	            dto.setModify_date(rs.getString("modify_date"));
	            dto.setAvatar(rs.getString("avatar"));
	            dto.setEmail(rs.getString("email"));
	       
	            dto.setPhone(rs.getString("phone"));
	            dto.setBirth(rs.getString("birth"));
	            dto.setAddr1(rs.getString("addr1"));
	            dto.setAddr2(rs.getString("addr2"));
	            dto.setPosition(rs.getString("position"));
	            dto.setDepartment_id(rs.getString("department_id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return dto;
		
	}
	
	// 게시물 리스트
	public List<ProfessorDTO> listProfessor(int offset, int size){
		List<ProfessorDTO> list = new ArrayList<ProfessorDTO>();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append("");
			sb.append("");
			sb.append("");
			sb.append("");
			sb.append("");
			sb.append("");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setInt(1, offset);
			pstmt.setInt(2, size);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return list;
	}
	
	/*
	// 교수 수정
	public void updateProfessor(ProfessorDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql ="UPDATE member SET password = ?, modify_date = SYSDATE, avatar = ?, addr1 = ?, addr2 = ?"
					+ " WHERE member_id = ? ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getPassword());
			pstmt.setString(2, dto.getModify_date());
			pstmt.setString(3, dto.getAvatar());
			pstmt.setString(4, dto.getAddr1());
			pstmt.setString(5, dto.getAddr2());
			pstmt.setString(6, dto.getMember_id());
			
			pstmt.executeUpdate();
			
			pstmt.close();
			pstmt = null;
			
			sql = "UPDATE professor SET position = ?, department_id = ?"
					+ " WHERE member_id = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getPosition());
			pstmt.setString(2, dto.getDepartment_id());
			pstmt.setString(3, dto.getMember_id());
			
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(pstmt);
		}
	}
	
	// 교수 삭제
	public void deleteProfessor(String member_id) throws SQLException{
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "DELETE FROM professor WHERE member_id = ?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, member_id);
			
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}
	*/
	
}
