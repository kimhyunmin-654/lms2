package com.lms2.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.lms2.model.Pro_hwDTO;
import com.lms2.util.DBConn;
import com.lms2.util.DBUtil;


public class Pro_hwDAO {
	private Connection conn = DBConn.getConnection();
	
	//과제 등록
	public void inserthw(Pro_hwDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "INSERT INTO homework(homework_id, subject, content, reg_date, modify_date, hit_count, deadline_date, lecture_code)"
					+ " VALUES(HOMEWORK_SEQ.NEXTVAL, ?, ?, SYSDATE, SYSDATE, 0, TO_DATE(?, 'YYYY-MM-DD'), ?)";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getSubject());
			pstmt.setString(2, dto.getContent());
			pstmt.setString(3, dto.getDeadline_date());
			pstmt.setString(4, dto.getLecture_code());
			
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}
	
	//데이터개수
	public int dataCount() {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT COUNT (*) FROM homework";
			
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
	
	//검색데이터 개수
	public int dataCount(String schType, String kwd) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT COUNT (*) FROM homework";
			if(schType.equals("all")) {
				sql += " AND (INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1) ";
			} else if (schType.equals("reg_date")) {
				kwd = kwd.replaceAll("(\\-|\\.|\\/)", "");
				sql += " AND TO_CHAR(reg_date, 'YYYYMMDD'_ = ? ";
			} else {
				sql += " AND INSTR(" + schType + ", ?) >= 1 ";
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
	
	//과제 리스트
	public List<Pro_hwDTO> listPro_hw(int offset, int size) {
		List<Pro_hwDTO> list = new ArrayList<Pro_hwDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(" SELECT homework_id, subject, TO_CHAR(reg_date, 'YYYY-MM-DD') reg_date, hit_count ");
			sb.append(" FROM homework");
			sb.append(" ORDER BY homework_id DESC ");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setInt(1, offset);
			pstmt.setInt(2, size);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				Pro_hwDTO dto = new Pro_hwDTO();
				
				dto.setHomework_id(rs.getInt("homework_id"));
				dto.setSubject(rs.getString("subject"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setHit_count(rs.getInt("hit_count"));
				
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
	
	public List<Pro_hwDTO> listPro_hw(int offset, int size, String schType, String kwd) {
		List<Pro_hwDTO> list = new ArrayList<Pro_hwDTO>();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(" SELECT homework_id, subject, TO_CHAR(reg_date, 'YYYYMMDD') reg_date, hitCount ");
			sb.append(" FROM homework ");
			if(schType.equals("all")) {
				sb.append(" WHERE INSTR(subject, ?) >= 1 ");
			} else if (schType.equals("reg_date")) {
				kwd = kwd.replaceAll("(\\-|\\/|\\.)", "");
				sb.append(" WHERE TO_CHAR(reg_date, 'YYYYMMDD') = ? ");
			} else {
				sb.append(" WHERE INSTR(" + schType + ", ?) >= 1 ");
			}
			
			sb.append(" ORDER BY homework_id DESC ");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			if(schType.equals("all")) {
				pstmt.setString(1, kwd);
				pstmt.setInt(2, offset);
				pstmt.setInt(3, size);
			} else {
				pstmt.setString(1, kwd);
				pstmt.setInt(2, offset);
				pstmt.setInt(3, size);
			}
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				Pro_hwDTO dto = new Pro_hwDTO();
				
				dto.setHomework_id(rs.getInt("homework_id"));
				dto.setSubject(rs.getString("subject"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setHit_count(rs.getInt("hit_count"));
				
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
	
	//조회수 증가
	public void updateHitCount(int homework_id) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "UPDATE homework SET hit_count = hit_count + 1 WHERE homework_id = ?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, homework_id);
			
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}
	
	//과제 보기
	public Pro_hwDTO findById(int homework_id) {
		Pro_hwDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT homework_id, subject, content, hit_count, reg_date, deadline_date, lecture_code "
					+ " FROM homework "
					+ " WHERE homework_id = ? ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, homework_id);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new Pro_hwDTO();
				
				dto.setHomework_id(rs.getInt("homework_id"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setHit_count(rs.getInt("hit_count"));
				dto.setDeadline_date(rs.getString("deadline_date"));
				dto.setLecture_code(rs.getString("lecture_code"));
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return dto;
	}
	
	//이전글
	public Pro_hwDTO findByPrev(int homework_id, String schType, String kwd) {
		Pro_hwDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			if(kwd != null && kwd.length() != 0) {
				sb.append(" SELECT homework_id, subject ");
				sb.append(" FROM homework");
				sb.append(" WHERE homework_id < ? ");
				
				if(schType.equals("all")) {
					sb.append(" AND ( INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 ) ");
				} else if("reg_date".equals(schType)) {
					kwd = kwd.replaceAll("(\\-|\\/|\\.)", "");
					sb.append(" AND TO_CHAR(reg_date, 'YYYYMMDD') = ? ");
				} else {
					sb.append(" AND INSTR(" + schType + ", ?) >= 1 ");
				}
				
				sb.append(" ORDER BY homework_id DESC ");
				sb.append(" FETCH FIRST 1 ROWS ONLY ");
				
				pstmt = conn.prepareStatement(sb.toString());
				pstmt.setInt(1, homework_id);
				
				if(schType.equals("all")) {
					pstmt.setString(2, kwd);
					pstmt.setString(3, kwd);
				} else {
					pstmt.setString(2, kwd);
				}
			} else {
				sb.append(" SELECT homework_id, subject ");
				sb.append(" FROM homework ");
				sb.append(" WHERE homework_id < ? ");
				sb.append(" ORDER BY homework_id DESC ");
				sb.append(" FETCH FIRST 1 ROWS ONLY ");
				
				pstmt = conn.prepareStatement(sb.toString());
				pstmt.setInt(1, homework_id);
			}
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new Pro_hwDTO();
				dto.setHomework_id(rs.getInt("homework_id"));
				dto.setSubject(rs.getString("subject"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		return dto;
	}
	
	//다음글
	public Pro_hwDTO findByNext(int homework_id, String schType, String kwd) {
		Pro_hwDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			if(kwd != null && kwd.length() != 0) {
				sb.append(" SELECT homework_id, subject ");
				sb.append(" FROM homework ");
				sb.append(" WHERE homework_id > ? ");
				
				if(schType.equals("all")) {
					sb.append(" AND ( INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 ) ");
				} else if ("reg_date".equals(schType)) {
					kwd = kwd.replaceAll("(\\-|\\/|\\.)", "");
					sb.append(" AND TO_CHAR(reg_date, 'YYYYMMDD') = ? ");
				} else {
					sb.append(" AND INSTR(" + schType + ", ?) >= 1 ");
				}
				
				sb.append(" ORDER BY homework_id ASC ");
				sb.append(" FETCH FIRSR 1 ROWS ONLY ");
				
				pstmt = conn.prepareStatement(sb.toString());
				pstmt.setInt(1, homework_id);
				
				if(schType.equals("all")) {
					pstmt.setString(2, kwd);
					pstmt.setString(3, kwd);
				} else {
					pstmt.setString(2, kwd);
				}
			} else {
				sb.append(" SELECT homework_id, subject ");
				sb.append(" FROM homework ");
				sb.append(" ORDER BY homework_id ASC ");
				sb.append(" FETCH FIRST 1 ROWS ONLY ");
				
				pstmt = conn.prepareStatement(sb.toString());
				pstmt.setInt(1, homework_id);
			}
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new Pro_hwDTO();
				dto.setHomework_id(rs.getInt("homework_id"));
				dto.setSubject(rs.getString("subject"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return dto;
	}
	
	//과제 수정
	public void updateHw(Pro_hwDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "UPDATE homework SET subject = ?, content = ?, deadline_date = ? WHERE homework_id = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getSubject());
			pstmt.setString(2, dto.getContent());
			pstmt.setString(3, dto.getDeadline_date());
			pstmt.setInt(4, dto.getHomework_id());
			
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
		
	}
	
	//과제 삭제
	public void deleteHw(int homework_id, String memeber_id) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "DELETE FROM homework homework_id = ?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, homework_id);
			
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}
}
