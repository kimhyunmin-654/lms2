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
	public int inserthw(Pro_hwDTO dto) throws SQLException {
		int hwId = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
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
	        pstmt.close();
	        
	        sql = "SELECT HOMEWORK_SEQ.CURRVAL FROM dual";
	        pstmt = conn.prepareStatement(sql);
	        rs = pstmt.executeQuery();
	        if(rs.next()) {
	        	hwId = rs.getInt(1);
	        }
	        rs.close();
	        pstmt.close();
	        
			
			if(dto.getOriginal_filename() != null && dto.getSave_filename() != null) {
				sql = "INSERT INTO homework_file(file_id, save_filename, original_filename, file_size, homework_id) "
						+ " VALUES(HOMEWORK_FILE_SEQ.NEXTVAL, ?, ?, ?, ?)";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, dto.getSave_filename());
				pstmt.setString(2, dto.getOriginal_filename());
				pstmt.setInt(3, dto.getFile_size());
				pstmt.setInt(4, hwId);
				pstmt.executeUpdate();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
			DBUtil.close(rs);
		}
		return hwId;
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
	        if (schType.equals("all")) {
	            sql = "SELECT COUNT(*) FROM homework WHERE (INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1)";
	        } else if (schType.equals("reg_date")) {
	            kwd = kwd.replaceAll("(\\-|\\.|\\/)", ""); // yyyyMMdd 형태로 정규화
	            sql = "SELECT COUNT(*) FROM homework WHERE TO_CHAR(reg_date, 'YYYYMMDD') = ?";
	        } else {
	            sql = "SELECT COUNT(*) FROM homework WHERE INSTR(" + schType + ", ?) >= 1";
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
			sb.append(" SELECT h.homework_id, h.subject, ");
			sb.append(" TO_CHAR(h.reg_date, 'YYYY-MM-DD') reg_date, ");
			sb.append(" h.hit_count, TO_CHAR(h.deadline_date, 'YYYY-MM-DD') AS deadline_date, ");
			sb.append(" f.save_filename, f.original_filename ");
			sb.append(" FROM homework h ");
			sb.append(" LEFT JOIN homework_file f ON h.homework_id = f.homework_id ");
			sb.append(" ORDER BY h.homework_id DESC ");
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
				dto.setDeadline_date(rs.getString("deadline_date"));
				dto.setSave_filename(rs.getString("save_filename"));
				dto.setOriginal_filename(rs.getString("original_filename"));
				
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
			sb.append(" SELECT h.homework_id, h.subject, TO_CHAR(reg_date, 'YYYYMMDD') reg_date, h.hit_count, ");
			sb.append(" f.save_filename, f.original_filename ");
			sb.append(" FROM homework h ");
			sb.append(" LEFT JOIN homework_file f ON h.homework_id = f.homework_id ");
			
			if(schType.equals("all")) {
				sb.append(" WHERE INSTR(subject, ?) >= 1 ");
			} else if (schType.equals("reg_date")) {
				kwd = kwd.replaceAll("(\\-|\\/|\\.)", "");
				sb.append(" WHERE TO_CHAR(reg_date, 'YYYYMMDD') = ? ");
			} else {
				sb.append(" WHERE INSTR(h." + schType + ", ?) >= 1 ");
			}
			
			sb.append(" ORDER BY h.homework_id DESC ");
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
				dto.setSave_filename(rs.getString("save_filename"));
				dto.setOriginal_filename(rs.getString("original_filename"));
				
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
			sql = "SELECT h.homework_id, h.subject, h.content, h.hit_count, "
					+ " TO_CHAR(h.reg_date, 'YYYYMMDD') reg_date, h.deadline_date, "
					+ " NVL(l.subject, '강의없음') AS lecture_subject, "
					+ " f.save_filename, f.original_filename, f.file_size "
					+ " FROM homework h "
					+ " LEFT JOIN LECTURE l ON h.lecture_code = l.lecture_code "
					+ " LEFT JOIN homework_file f ON h.homework_id = f.homework_id "
					+ " WHERE h.homework_id = ? ";
			
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
				dto.setLecture_code(rs.getString("lecture_subject"));
				dto.setSave_filename(rs.getString("save_filename"));
				dto.setOriginal_filename(rs.getString("original_filename"));
				dto.setFile_size(rs.getInt("file_size"));				
				
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
	        sb.append(" SELECT homework_id, subject ");
	        sb.append(" FROM homework ");
	        sb.append(" WHERE homework_id > ? ");

	        if (kwd != null && kwd.length() != 0) {
	            if (schType.equals("all")) {
	                sb.append(" AND ( INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 ) ");
	            } else if ("reg_date".equals(schType)) {
	                kwd = kwd.replaceAll("(\\-|\\/|\\.)", "");
	                sb.append(" AND TO_CHAR(reg_date, 'YYYYMMDD') = ? ");
	            } else {
	                sb.append(" AND INSTR(" + schType + ", ?) >= 1 ");
	            }
	        }

	        sb.append(" ORDER BY homework_id ASC ");
	        sb.append(" FETCH FIRST 1 ROWS ONLY "); // 오타 수정됨

	        pstmt = conn.prepareStatement(sb.toString());

	        pstmt.setInt(1, homework_id);

	        if (kwd != null && kwd.length() != 0) {
	            if (schType.equals("all")) {
	                pstmt.setString(2, kwd);
	                pstmt.setString(3, kwd);
	            } else {
	                pstmt.setString(2, kwd);
	            }
	        }

	        rs = pstmt.executeQuery();

	        if (rs.next()) {
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
			sql = "UPDATE homework SET subject = ?, content = ?, deadline_date = ?, lecture_code = ? WHERE homework_id = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getSubject());
			pstmt.setString(2, dto.getContent());
			pstmt.setString(3, dto.getDeadline_date());
			pstmt.setString(4, dto.getLecture_code());
			pstmt.setInt(5, dto.getHomework_id());
			
			pstmt.executeUpdate();
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
			sql = "DELETE FROM homework WHERE homework_id = ?";
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
