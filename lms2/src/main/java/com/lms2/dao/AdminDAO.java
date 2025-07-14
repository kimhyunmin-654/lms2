package com.lms2.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.lms2.model.AdminDTO;
import com.lms2.util.DBConn;
import com.lms2.util.DBUtil;

public class AdminDAO {
	private Connection conn = DBConn.getConnection();
	
	// 관리자 등록
	public void insertAdmin(AdminDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			conn.setAutoCommit(false);
			
			sql = "INSERT INTO member(member_id, name, password, role ,create_date, modify_date, avatar, email, phone, birth, addr1, addr2) "
					+ " VALUES(?, ?, ?, 99 ,SYSDATE, SYSDATE, ?, ?, ?, TO_DATE(?, 'YYYY-MM-DD), ?, ?";
			
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
			
			sql = "INSERT INTO admin(member_id, positon, division) "
					+ "	VALUES(?, ? ,?) ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getMember_id());
			pstmt.setString(2, dto.getPosition());
			pstmt.setString(3, dto.getDivision());
			
			pstmt.executeUpdate();
			
			conn.commit();
			
		} catch (SQLException e) {
			DBUtil.rollback(conn);
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
	
	// 관리자 검색
	public AdminDTO findById(String member_id) {
		AdminDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = " SELECLT m.member_id, name, password, role ,create_date, modify_date, avatar, email, phone, TO_CHAR(birth, 'YYYY-MM-DD) birth, addr1, addr2 "
					+ " FROM member m "
					+ " LEFT OUTER JOIN admin a ON m.member_id = a.member_id "
					+ " WHERE m.member_id = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, member_id);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new AdminDTO();
				dto.setMember_id(rs.getString("member_id"));
				dto.setName(rs.getString("name"));
				dto.setPassword(rs.getString("password"));
				dto.setRole(rs.getInt("role"));
				dto.setCreate_date(rs.getString("create_date"));
				dto.setModify_date(rs.getString("modify_date"));
				dto.setAvatar(rs.getString("avatar"));
				dto.setEmail(rs.getString("email"));
				if(dto.getEmail() != null) {
					String[] ss = dto.getEmail().split("@");
					if(ss.length == 2) {
						dto.setEmail1(ss[0]);
						dto.setEmail2(ss[1]);
					}
				}
				dto.setPhone(rs.getString("phone"));
				dto.setBirth(rs.getString("birth"));
				dto.setAddr1(rs.getString("addr1"));
				dto.setAddr2(rs.getString("addr2"));
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		
		return dto;
		
	}
	
	// 관리자 수정
	public void updateAdmin(AdminDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			conn.setAutoCommit(false);

			sql = "UPDATE member SET name = ?, password = ?, avatar = ?, email = ?, phone = ?, birth = TO_DATE(?, 'YYYY-MM-DD'), addr1 = ?, addr2 = ? "
					+ "WHERE member_id = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getName());
			pstmt.setString(2, dto.getPassword());
			pstmt.setString(3, dto.getAvatar());
			pstmt.setString(4, dto.getEmail());
			pstmt.setString(5, dto.getPhone());
			pstmt.setString(6, dto.getBirth());
			pstmt.setString(7, dto.getAddr1());
			pstmt.setString(8, dto.getAddr2());
			pstmt.setString(9, dto.getMember_id());
			pstmt.executeUpdate();
			pstmt.close();

			sql = "UPDATE admin SET position = ?, division = ? WHERE member_id = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getPosition());
			pstmt.setString(2, dto.getDivision());
			pstmt.setString(3, dto.getMember_id());
			pstmt.executeUpdate();

			conn.commit();
		} catch (Exception e) {
			DBUtil.rollback(conn);
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
			try {
				conn.setAutoCommit(true);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	// 관리자 삭제
	public void deleteAdmin(String member_id, int role) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			if(role == 99) {
				conn.setAutoCommit(false);
				
				sql = " DELETE FROM member WHERE  member_id = ?";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, member_id);
				
				pstmt.executeUpdate();
				pstmt.close();
				pstmt = null;
				
				sql = " DELETE FROM admin WHERE member_id = ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, member_id);
				
				pstmt.executeUpdate();
				
				conn.commit();
				
			}
			
			
			
		} catch (SQLException e) {
			DBUtil.rollback(conn);
			
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
	
	// 관리자 사진 초기화
	public void deleteAvatar(String member_id) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = " UPDATE member SET avatar = null WHERE member_id = ? ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, member_id);
			
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(pstmt);
		}
	}
	
	// 관리자 개수
	public int dataCount() {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql  = "SELECT COUNT(*) FROM member WHERE role = 99";
			
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
	
	// 검색에서 관리자 개수
	public int dataCount(String schType, String kwd) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT COUNT(*) "
					+ " FROM member m "
					+ " JOIN admin a ON m.member_id = a.member_id "
					+ " WHERE role = 99";
			
			if(schType.equals("all")) { // 교번 또는 이름
				sql += " AND ( INSTR(member_id, ?) >= 1 OR INSTR(name, ?) >= 1 ) ";
			} else if(schType.equals("reg_date")) { // 등록일자 검색(YYYYMMDD 형식)
				kwd = kwd.replaceAll("(\\-|\\.|\\/)", "");
				sql += "AND TO_CHAR(reg_date, 'YYYYMMDD') = ? ";
			} else { // 기타 email, phone, birth, addr 등
				sql += " AND INSTR(" + schType + ", ?) >= 1" ; 
			}
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, kwd);
			if(schType.equals("all")) {
				pstmt.setString(2, kwd);
			}
			
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
	
	// 관리자 리스트
	public List<AdminDTO> listAdmin(int offset, int size) {
		List<AdminDTO> list = new ArrayList<AdminDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(" SELECT m.member_id, m.name, TO_CHAR(birth, 'YYYY-MM-DD') birth, email, phone, a.position, a.division ");
			sb.append(" FROM member m ");
			sb.append(" JOIN admin a ON m.member_id = a.member_id");
			sb.append(" WHERE role = 99");
			sb.append(" ORDER BY member_id ASC");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");

			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setInt(1, offset);
			pstmt.setInt(2, size);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				AdminDTO dto = new AdminDTO();
				
				dto.setMember_id(rs.getString("member_id"));
				dto.setName(rs.getString("name"));
				dto.setBirth(rs.getString("birth"));
				dto.setEmail(rs.getString("email"));
				dto.setPhone(rs.getString("phone"));
				dto.setPosition(rs.getString("position"));
				dto.setDivision(rs.getString("division"));
				
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

