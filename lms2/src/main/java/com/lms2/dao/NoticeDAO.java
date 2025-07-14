package com.lms2.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.lms2.model.AdminDTO;
import com.lms2.model.NoticeDTO;
import com.lms2.util.DBConn;
import com.lms2.util.DBUtil;
import com.lms2.util.MyMultipartFile;

public class NoticeDAO {
	Connection conn = DBConn.getConnection();
	
	// 공지사항 데이터 추가
	public void insertNotice(NoticeDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			// 공지사항 INSERT + notice_id 반환
			sql = "INSERT INTO notice(notice_id, is_notice, subject, content, hit_count, reg_date, modify_date, is_visible, member_id) "
				 + "VALUES(notice_seq.NEXTVAL, ?, ?, ?, 0, SYSDATE, SYSDATE, 1, ?)";
			pstmt = conn.prepareStatement(sql, new String[] { "notice_id" });
			pstmt.setInt(1, dto.getIs_notice());
			pstmt.setString(2, dto.getSubject());
			pstmt.setString(3, dto.getContent());
			pstmt.setString(4, dto.getMember_id());
			pstmt.executeUpdate();

			// 방금 생성된 notice_id 얻기
			rs = pstmt.getGeneratedKeys();
			if (rs.next()) {
				dto.setNotice_id(rs.getLong(1)); // DTO에 저장
			}
			rs.close();
			pstmt.close();
			pstmt = null;

			// 첨부 파일이 있다면 파일 테이블에도 INSERT
			if (dto.getListFile() != null && !dto.getListFile().isEmpty()) {
				sql = "INSERT INTO notice_file(file_num, file_size, save_filename, original_filename, notice_id) "
					+ "VALUES(noticeFile_seq.NEXTVAL, ?, ?, ?, ?)";

				pstmt = conn.prepareStatement(sql);
				for (MyMultipartFile mf : dto.getListFile()) {
					pstmt.setLong(1, mf.getSize());
					pstmt.setString(2, mf.getSaveFilename());
					pstmt.setString(3, mf.getOriginalFilename());
					pstmt.setLong(4, dto.getNotice_id());
					pstmt.executeUpdate();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
	}
	
	// 전체 데이터 개수
	public int dataCount() {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT NVL(COUNT(*), 0) FROM notice";
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

	
	// 검색에서 전체 개수
	public int dataCount(String schType, String kwd) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT NVL(COUNT(*), 0) FROM notice n "
					+ " JOIN admin a ON n.member_id = a.member_id ";
			if (schType.equals("all")) {
				sql += "  WHERE INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 ";
			} else if (schType.equals("reg_date")) {
				kwd = kwd.replaceAll("(\\-|\\/|\\.)", "");
				sql += "  WHERE TO_CHAR(reg_date, 'YYYYMMDD') = ? ";
			} else {
				sql += "  WHERE INSTR(" + schType + ", ?) >= 1 ";
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
	
	// 공지사항 리스트
	public List<NoticeDTO> listNotice(int offset, int size) {
		List<NoticeDTO> list = new ArrayList<NoticeDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(" SELECT n.notice_id, subject, hit_count, reg_date, n.member_id, m.name ");
			sb.append(" FROM notice n ");
			sb.append(" JOIN admin a ON n.member_id = a.member_id");
			sb.append(" JOIN member m ON a.member_id = m.member_id");
			sb.append(" ORDER BY n.notice_id DESC ");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setInt(1, offset);
			pstmt.setInt(2, size);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				NoticeDTO dto = new NoticeDTO();
				
				dto.setNotice_id(rs.getLong("notice_id"));
				dto.setSubject(rs.getString("subject"));
				dto.setHit_count(rs.getInt("hit_count"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setMember_id(rs.getString("member_id"));
				dto.setName(rs.getString("name"));
				
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
	
	
	// 검색에서  공지사항 리스트
	public List<NoticeDTO> listNotice(int offset, int size, String schType, String kwd) {
		List<NoticeDTO> list = new ArrayList<NoticeDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(" SELECT n.notice_id, subject, hit_count, reg_date, n.member_id, m.name ");
			sb.append(" FROM notice n ");
			sb.append(" JOIN admin a ON n.member_id = a.member_id");
			sb.append(" JOIN member m ON a.member_id = m.member_id");
			if (schType.equals("all")) {
				sb.append(" WHERE INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 ");
			} else if (schType.equals("reg_date")) {
				kwd = kwd.replaceAll("(\\-|\\/|\\.)", "");
				sb.append(" WHERE TO_CHAR(reg_date, 'YYYYMMDD') = ?");
			} else {
				sb.append(" WHERE INSTR(" + schType + ", ?) >= 1 ");
			}
		
			sb.append(" ORDER BY n.notice_id DESC ");
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
				NoticeDTO dto = new NoticeDTO();
				
				dto.setNotice_id(rs.getLong("notice_id"));
				dto.setSubject(rs.getString("subject"));
				dto.setHit_count(rs.getInt("hit_count"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setMember_id(rs.getString("member_id"));
				dto.setName(rs.getString("name"));
				
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
	
	// 공지글
	public List<NoticeDTO> listNotice() {
		List<NoticeDTO> list = new ArrayList<NoticeDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(" SELECT n.notice_id, subject, hit_count, TO_CHAR(reg_date, 'YYYY-MM-DD') reg_date, n.member_id, m.name ");
			sb.append(" FROM notice n ");
			sb.append(" JOIN admin a ON n.member_id = a.member_id");
			sb.append(" JOIN member m ON a.member_id = m.member_id");
			sb.append(" ORDER BY n.notice_id DESC ");
			
			pstmt = conn.prepareStatement(sb.toString());
						
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				NoticeDTO dto = new NoticeDTO();
				
				dto.setNotice_id(rs.getLong("notice_id"));
				dto.setSubject(rs.getString("subject"));
				dto.setHit_count(rs.getInt("hit_count"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setMember_id(rs.getString("member_id"));
				dto.setName(rs.getString("name"));
				
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
	
	public NoticeDTO findById(long notice_id) {
		NoticeDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = " SELECT notice_id, is_notice, subject, content, hit_count, reg_date, modify_date, is_visible, n.member_id "
			    + " FROM notice n "
			    + " JOIN admin a ON n.member_id = a.member_id "
			    + " WHERE notice_id = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, notice_id);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new NoticeDTO();
				
				dto.setNotice_id(rs.getLong("notice_id"));
				dto.setIs_notice(rs.getInt("is_notice"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setHit_count(rs.getInt("hit_count"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setModify_date(rs.getString("modify_date"));
				dto.setIs_visible(rs.getInt("is_visible"));
				dto.setMember_id(rs.getString("member_id"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return dto;
	}
	
	// 이전글
	public NoticeDTO findByPrev(long notice_id, String schType, String kwd) {
		NoticeDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			if (kwd != null && kwd.length() != 0) {
				sb.append(" SELECT notice_id, subject ");
				sb.append(" FROM notice n ");
				sb.append(" JOIN admin a ON n.member_id = a.member_id ");
				sb.append(" WHERE ( notice_id > ? ) ");
				if (schType.equals("all")) {
					sb.append("   AND ( INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 ) ");
				} else if (schType.equals("reg_date")) {
					kwd = kwd.replaceAll("(\\-|\\/|\\.)", "");
					sb.append("   AND ( TO_CHAR(reg_date, 'YYYYMMDD') = ? ) ");
				} else {
					sb.append("   AND ( INSTR(" + schType + ", ?) >= 1 ) ");
				}
				sb.append(" ORDER BY notice_id ASC ");
				sb.append(" FETCH FIRST 1 ROWS ONLY ");

				pstmt = conn.prepareStatement(sb.toString());
				
				pstmt.setLong(1, notice_id);
				pstmt.setString(2, kwd);
				if (schType.equals("all")) {
					pstmt.setString(3, kwd);
				}
			} else {
				sb.append(" SELECT notice_id, subject FROM notice ");
				sb.append(" WHERE notice_id > ? ");
				sb.append(" ORDER BY notice_id ASC ");
				sb.append(" FETCH FIRST 1 ROWS ONLY ");

				pstmt = conn.prepareStatement(sb.toString());
				
				pstmt.setLong(1, notice_id);
			}

			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new NoticeDTO();
				
				dto.setNotice_id(rs.getLong("notice_id"));
				dto.setSubject(rs.getString("subject"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return dto;
	}

	// 다음글
	public NoticeDTO findByNext(long notice_id, String schType, String kwd) {
		NoticeDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			if (kwd != null && kwd.length() != 0) {
				sb.append(" SELECT notice_id, subject ");
				sb.append(" FROM notice n ");
				sb.append(" JOIN admin a ON n.member_id = a.member_id ");
				sb.append(" WHERE ( notice_id < ? ) ");
				if (schType.equals("all")) {
					sb.append("   AND ( INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 ) ");
				} else if (schType.equals("reg_date")) {
					kwd = kwd.replaceAll("(\\-|\\/|\\.)", "");
					sb.append("   AND ( TO_CHAR(reg_date, 'YYYYMMDD') = ? ) ");
				} else {
					sb.append("   AND ( INSTR(" + schType + ", ?) >= 1 ) ");
				}
				sb.append(" ORDER BY notice_id DESC ");
				sb.append(" FETCH FIRST 1 ROWS ONLY ");

				pstmt = conn.prepareStatement(sb.toString());
				
				pstmt.setLong(1, notice_id);
				pstmt.setString(2, kwd);
				if (schType.equals("all")) {
					pstmt.setString(3, kwd);
				}
			} else {
				sb.append(" SELECT notice_id, subject FROM notice ");
				sb.append(" WHERE notice_id < ? ");
				sb.append(" ORDER BY notice_id DESC ");
				sb.append(" FETCH FIRST 1 ROWS ONLY ");

				pstmt = conn.prepareStatement(sb.toString());
				
				pstmt.setLong(1, notice_id);
			}

			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new NoticeDTO();
				
				dto.setNotice_id(rs.getLong("notice_id"));
				dto.setSubject(rs.getString("subject"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return dto;
	}
	
	
	public void updateHitCount(long notice_id) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "UPDATE notice SET hitCount = hit_count + 1 WHERE notice_id = ?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, notice_id);
			
			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}

	}
	
	public void updateNotice(NoticeDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			
			sql = "UPDATE notice SET is_notice = ?, subject = ?, content = ?, modify_date = SYSDATE "
					+ " WHERE notice_id = ?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, dto.getIs_notice());
			pstmt.setString(2, dto.getSubject());
			pstmt.setString(3, dto.getContent());
			pstmt.setLong(4, dto.getNotice_id());
			
			pstmt.executeUpdate();
			
			pstmt.close();
			pstmt = null;

			if (dto.getListFile() != null && dto.getListFile().size() != 0) {
				sql = "INSERT INTO noticeFile(file_num, notice_id, file_size, save_filename, original_filename) "
						+ " VALUES (noticeFile_seq.NEXTVAL, ?, ?, ?, ?)";
				pstmt = conn.prepareStatement(sql);
				
				for (MyMultipartFile mf: dto.getListFile()) {
					pstmt.setLong(1, dto.getNotice_id());
					pstmt.setLong(2, mf.getSize());
					pstmt.setString(3, mf.getSaveFilename());
					pstmt.setString(4, mf.getOriginalFilename());
					
					pstmt.executeUpdate();
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}

	}

	public void deleteNotice(long notice_id) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "DELETE FROM notice WHERE notice_id = ?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, notice_id);
			
			pstmt.executeUpdate();
		
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}
	
	public void deleteNotice(long[] notice_ids) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {


			sql = "DELETE FROM notice WHERE notice_id = ? ";
			pstmt = conn.prepareStatement(sql);
			
			for(long num : notice_ids) {
				pstmt.setLong(1, num);
				pstmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}

	}
	
	public List<NoticeDTO> listNoticeFile(long notice_id) {
		List<NoticeDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT file_id, file_size, save_Filename, original_Filename, notice_id "
					+ " FROM notice_file WHERE notice_id = ?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, notice_id);
			
			rs = pstmt.executeQuery();

			while (rs.next()) {
				NoticeDTO dto = new NoticeDTO();

				dto.setFile_id(rs.getLong("file_id"));
				dto.setFile_size(rs.getInt("file_size"));
				dto.setSave_filename(rs.getString("save_Filename"));
				dto.setOriginal_filename(rs.getString("original_Filename"));
				dto.setNotice_id(rs.getInt("notice_id"));
				
				list.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return list;
	}
	
	public NoticeDTO findByFileId(long file_id) {
		NoticeDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT file_id, file_size, save_Filename, original_Filename, notice_id "
					+ " FROM notice_file WHERE file_id = ?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, file_id);
			
			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new NoticeDTO();

				dto.setFile_id(rs.getLong("file_id"));
				dto.setFile_size(rs.getInt("file_size"));
				dto.setSave_filename(rs.getString("save_Filename"));
				dto.setOriginal_filename(rs.getString("original_Filename"));
				dto.setNotice_id(rs.getInt("notice_id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return dto;
	}
	
	public void deleteNoticeFile(String mode, long file_id) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			if (mode.equals("all")) {
				sql = "DELETE FROM notice_file WHERE notice_id = ?";
			} else {
				sql = "DELETE FROM notice_file WHERE file_id = ?";
			}
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, file_id);

			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}

	}
	
	
}
