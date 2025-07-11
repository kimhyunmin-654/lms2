package com.lms2.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.crypto.Data;

import com.lms2.model.DataDTO;
import com.lms2.util.DBConn;
import com.lms2.util.DBUtil;

public class DataDAO {
	private Connection conn = DBConn.getConnection();
	
	//데이터 추가
	public void insertData(DataDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "INSERT INTO data(data_id, subject, content, hit_count, reg_date, modify_date, lecture_code)"
					+ " VALUES(DATA_SEQ.NEXTVAL, ?, ?, 0, SYSDATE, SYSDATE, ?)";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getSubject());
			pstmt.setString(2, dto.getContent());
			pstmt.setString(3, dto.getLecture_code());
			
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
			sql = "SELECT COUNT (*) FROM DATA";
			
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
			sql = "SELECT COUNT(*) FROM DATA";
			if(schType.equals("all")) {
				sql += " AND (INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 ) ";
			} else if (schType.equals("reg_date")) {
				kwd = kwd.replaceAll("(\\-|\\.|\\/)", "");
				sql += " AND TO_CHAR(reg_date, 'YYYYMMDD') = ? ";
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
	
	//자료실 리스트
	public List<DataDTO> listData(int offset, int size) {
		List<DataDTO> list = new ArrayList<DataDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(" SELECT data_id, subject, TO_CHAR(reg_date, 'YYYY-MM-DD') reg_date, hit_count ");
			sb.append(" FROM DATA");
			sb.append(" ORDER BY data_id DESC ");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setInt(1, offset);
			pstmt.setInt(2, size);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				DataDTO dto = new DataDTO();
				
				dto.setData_id(rs.getInt("data_id"));
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
	
	public List<DataDTO> listData(int offset, int size, String schType, String kwd) {
		List<DataDTO> list = new ArrayList<DataDTO>();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(" SELECT data_id, subject, TO_CHAR(reg_date, 'YYYY-MM-DD') reg_date, hit_count ");
			sb.append(" FROM DATA ");
			if(schType.equals("all")) {
				sb.append(" WHERE INSTR(subject, ?) >= 1 ");
			} else if(schType.equals("reg_date")) {
				kwd = kwd.replaceAll("(\\-|\\.|\\/)", "");
				sb.append(" WHERE TO_CHAR(reg_date, 'YYYYMMDD') = ? ");
			} else {
				sb.append(" WHERE INSTR(" + schType + ", ?) >= 1 ");
			}
			
			sb.append(" ORDER BY data_id DESC ");
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
				DataDTO dto = new DataDTO();
				
				dto.setData_id(rs.getInt("data_id"));
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
	public void updateHitCount(int data_id) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "UPDATE DATA SET hit_count = hit_count + 1 WHERE data_id = ?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, data_id);
			
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		} 
	}
	
	//해당 자료 보기
	public DataDTO findById(int data_id) {
		DataDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT ";
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return dto;
	}
}
