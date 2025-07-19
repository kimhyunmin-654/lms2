package com.lms2.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.lms2.model.MemberDTO;
import com.lms2.util.DBConn;
import com.lms2.util.DBUtil;

public class pro_memberDAO {
	private Connection conn = DBConn.getConnection();
	
	
	public List<MemberDTO> listmember(String lectureCode, int offset, int size) {
	    List<MemberDTO> list = new ArrayList<MemberDTO>();
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    StringBuilder sb = new StringBuilder();

	    try {
	        sb.append(" SELECT m.member_id, m.name, TO_CHAR(birth, 'YYYY-MM-DD') birth, ");
	        sb.append(" m.email, m.phone, m.role, d.department_name ");
	        sb.append(" FROM course_application c ");
	        sb.append(" JOIN student s ON c.member_id = s.member_id ");
	        sb.append(" JOIN member m ON s.member_id = m.member_id ");
	        sb.append(" JOIN department d ON s.department_id = d.department_id ");
	        sb.append(" WHERE c.apply_status = '신청' ");
	        sb.append(" AND c.lecture_code = ? ");
	        sb.append(" ORDER BY m.member_id DESC ");
	        sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");

	        pstmt = conn.prepareStatement(sb.toString());
	        
	        pstmt.setInt(1, offset);
			pstmt.setInt(2, size);

	        pstmt.setString(1, lectureCode);           

	        rs = pstmt.executeQuery();
	        while (rs.next()) {
	            MemberDTO dto = new MemberDTO();

	            dto.setMember_id(rs.getString("member_id"));
	            dto.setName(rs.getString("name"));
	            dto.setBirth(rs.getString("birth"));
	            dto.setEmail(rs.getString("email"));
	            dto.setPhone(rs.getString("phone"));
	            dto.setRole(rs.getInt("role"));
	            dto.setDepartment_name(rs.getString("department_name"));

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