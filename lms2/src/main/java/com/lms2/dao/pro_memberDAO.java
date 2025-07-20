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
	        sb.append(" m.email, m.phone, m.role, d.department_name, c.lecture_code ");
	        sb.append(" FROM course_application c ");
	        sb.append(" JOIN student s ON c.member_id = s.member_id ");
	        sb.append(" JOIN member m ON s.member_id = m.member_id ");
	        sb.append(" JOIN department d ON s.department_id = d.department_id ");
	        sb.append(" WHERE c.apply_status = '신청' ");
	        sb.append(" AND c.lecture_code = ? ");
	        sb.append(" ORDER BY m.member_id DESC ");
	        sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");

	        pstmt = conn.prepareStatement(sb.toString());
	        
	        pstmt.setString(1, lectureCode);          
	        pstmt.setInt(2, offset);
			pstmt.setInt(3, size);
	        
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
	
	// 데이터개수
	public int dataCount(String lecture_code) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql  = "SELECT COUNT(*) FROM COURSE_APPLICATION WHERE apply_status = '신청' AND lecture_code = ? ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, lecture_code);  
			
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
	
	// 검색데이터 개수
	public int dataCount(String schType, String kwd, String lecture_code) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT COUNT(*) FROM DATA WHERE 1=1"; 

			if (schType.equals("all")) {
				sql += " AND (INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1)";
			} else if (schType.equals("reg_date")) {
				kwd = kwd.replaceAll("(\\-|\\.|\\/)", "");
				sql += " AND TO_CHAR(reg_date, 'YYYYMMDD') = ?";
			} else {
				sql += " AND INSTR(" + schType + ", ?) >= 1";
			}
			
			sql += " AND lecture_code = ?";

			pstmt = conn.prepareStatement(sql);

			if (schType.equals("all")) {
				pstmt.setString(1, kwd);
				pstmt.setString(2, kwd);
				pstmt.setString(3, lecture_code);
			} else {
				pstmt.setString(1, kwd);
				pstmt.setString(2, lecture_code);
			}

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
	
	public List<MemberDTO> listmember(String lecture_code, int offset, int size, String schType, String kwd) {
		List<MemberDTO> list = new ArrayList<MemberDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(" SELECT m.member_id, m.name, TO_CHAR(m.birth, 'YYYY-MM-DD') birth, m.email, m.phone, m.role, d.department_name ");
			sb.append(" FROM course_application  c ");
			sb.append(" JOIN student s ON c.member_id = s.member_id ");
		    sb.append(" JOIN member m ON s.member_id = m.member_id ");
		    sb.append(" JOIN department d ON s.department_id = d.department_id ");
		    sb.append(" WHERE c.apply_status = '신청' ");		
		    sb.append(" AND c.lecture_code = ? ");
		    
			if(schType.equals("all")) {
				sb.append(" AND ( INSTR(m.member_id, ?) >= 1 OR INSTR(m.name, ?) >= 1 ) ");
			} else if(schType.equals("birth")) { 
				kwd = kwd.replaceAll("(\\-|\\.|\\/)", "");
				sb.append("AND TO_CHAR(m.birth, 'YYYYMMDD') = ? ");
			} else if (schType.equals("name")) {
				sb.append(" AND INSTR(m.name, ?) >= 1");
			} else if (schType.equals("department_name")) {
				sb.append(" AND INSTR(d.department_name, ?) >= 1");		
			} else {
				sb.append(" AND INSTR(" + schType + ", ?) >= 1") ; 
			}
								
			sb.append(" ORDER BY m.member_id ASC");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");
	
			pstmt = conn.prepareStatement(sb.toString());
			
			if(schType.equals("all")) {
				pstmt.setString(1, kwd);
				pstmt.setString(2, kwd);
				pstmt.setInt(3, offset);
				pstmt.setInt(4, size);
			} else {
				pstmt.setString(1, kwd);
				pstmt.setInt(2, offset);
				pstmt.setInt(3, size);
			}
						
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
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
	
	// 특정 강의코드에 해당하는 모든 자료를 조회
	public List<MemberDTO> listmember(String lecture_code) {
	       List<MemberDTO> list = new ArrayList<MemberDTO>();
	       PreparedStatement pstmt = null;
	       ResultSet rs = null;
	       StringBuilder sb = new StringBuilder();

	       try {
	    	   sb.append(" SELECT m.member_id, m.name, TO_CHAR(birth, 'YYYY-MM-DD') birth, ");
		        sb.append(" m.email, m.phone, m.role, d.department_name, c.lecture_code");
		        sb.append(" FROM course_application c ");
		        sb.append(" JOIN student s ON c.member_id = s.member_id ");
		        sb.append(" JOIN member m ON s.member_id = m.member_id ");
		        sb.append(" JOIN department d ON s.department_id = d.department_id ");
		        sb.append(" WHERE c.apply_status = '신청' ");
		        sb.append(" AND c.lecture_code = ? ");
		        sb.append(" ORDER BY m.member_id DESC ");
		        sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");
	           
	           pstmt = conn.prepareStatement(sb.toString());
	           pstmt.setString(1, lecture_code);      

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
	
	

	
	
}