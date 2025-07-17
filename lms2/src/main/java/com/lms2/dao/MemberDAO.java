package com.lms2.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.lms2.model.MemberDTO;
import com.lms2.util.DBConn;
import com.lms2.util.DBUtil;

public class MemberDAO {
	private Connection conn = DBConn.getConnection();
	
	public MemberDTO loginMember(String member_id, String password) {
		MemberDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT  m1.member_id, name, m1.password, role, create_date, modify_date, avatar, email, phone, birth, addr1, addr2, zip, a1.division "
					+ " FROM member m1 "
					+ " LEFT OUTER JOIN student s1 ON m1.member_id = s1.member_id "
					+ " LEFT OUTER JOIN professor p1 ON m1.member_id = p1.member_id "
					+ " LEFT OUTER JOIN admin a1 ON m1.member_id = a1.member_id "
					+ " WHERE m1.member_id = ? AND m1.password = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, member_id);
			pstmt.setString(2, password);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new MemberDTO();
				
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
				dto.setZip(rs.getString("zip"));
				dto.setDivision(rs.getString("division"));
				
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return dto;
	}
}
