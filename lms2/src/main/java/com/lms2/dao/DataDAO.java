package com.lms2.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import com.lms2.model.DataDTO;
import com.lms2.model.Data_CommentDTO;
import com.lms2.util.DBConn;
import com.lms2.util.DBUtil;

public class DataDAO {
	private Connection conn = DBConn.getConnection();

	public int insertData(DataDTO dto) throws SQLException {
	    int dataId = 0;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    String sql;

	    try {
	        sql = "INSERT INTO data(data_id, subject, content, hit_count, reg_date, modify_date, lecture_code) "
	            + "VALUES(DATA_SEQ.NEXTVAL, ?, ?, 0, SYSDATE, SYSDATE, ?)";
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, dto.getSubject());
	        pstmt.setString(2, dto.getContent());
	        pstmt.setString(3, dto.getLecture_code());
	        pstmt.executeUpdate();
	        pstmt.close();
	        
	        sql = "SELECT DATA_SEQ.CURRVAL FROM dual";
	        pstmt = conn.prepareStatement(sql);
	        rs = pstmt.executeQuery();
	        if (rs.next()) {
	            dataId = rs.getInt(1);
	        }
	        rs.close();
	        pstmt.close();

	        if (dto.getOriginal_filename() != null && dto.getSave_filename() != null) {
	            sql = "INSERT INTO data_file(file_id, save_filename, original_filename, file_size, data_id) "
	                + "VALUES(DATA_FILE_SEQ.NEXTVAL, ?, ?, ?, ?)";
	            pstmt = conn.prepareStatement(sql);
	            pstmt.setString(1, dto.getSave_filename());
	            pstmt.setString(2, dto.getOriginal_filename());
	            pstmt.setInt(3, dto.getFile_size());
	            pstmt.setInt(4, dataId);
	            pstmt.executeUpdate();
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	        throw e;
	    } finally {
	        DBUtil.close(pstmt);
	        DBUtil.close(rs);
	    }

	    return dataId;
	}

	// 데이터개수
	public int dataCount() {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT COUNT (*) FROM DATA";

			pstmt = conn.prepareStatement(sql);

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

	// 검색데이터 개수
	public int dataCount(String schType, String kwd) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT COUNT(*) FROM DATA WHERE 1 = 1";
			if (schType.equals("all")) {
				sql += " AND (INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 ) ";
			} else if (schType.equals("reg_date")) {
				kwd = kwd.replaceAll("(\\-|\\.|\\/)", "");
				sql += " AND TO_CHAR(reg_date, 'YYYYMMDD') = ? ";
			} else {
				sql += " AND INSTR(" + schType + ", ?) >= 1 ";
			}

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, kwd);
			if (schType.equals("all")) {
				pstmt.setString(2, kwd);
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

	// 자료실 리스트
	public List<DataDTO> listData(int offset, int size) {
		List<DataDTO> list = new ArrayList<DataDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append(" SELECT d.data_id, d.subject, TO_CHAR(d.reg_date, 'YYYY-MM-DD') reg_date, d.hit_count, ");
			sb.append(" f.save_filename, f.original_filename ");
			sb.append(" FROM DATA d ");
			sb.append(" LEFT JOIN data_file f ON d.data_id = f.data_id ");
			sb.append(" ORDER BY d.data_id DESC ");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");
			
			pstmt = conn.prepareStatement(sb.toString());

			pstmt.setInt(1, offset);
			pstmt.setInt(2, size);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				DataDTO dto = new DataDTO();

				dto.setData_id(rs.getInt("data_id"));
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

	public List<DataDTO> listData(int offset, int size, String schType, String kwd) {
		List<DataDTO> list = new ArrayList<DataDTO>();

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append(" SELECT d.data_id, d.subject, TO_CHAR(d.reg_date, 'YYYY-MM-DD') reg_date, d.hit_count, ");
			sb.append(" f.save_filename, f.original_filename ");
			sb.append(" FROM DATA d ");
			sb.append(" LEFT JOIN DATA_FILE f ON d.data_id = f.data_id ");

			if (schType.equals("all")) {
				sb.append(" WHERE INSTR(d.subject, ?) >= 1 ");
			} else if (schType.equals("reg_date")) {
				kwd = kwd.replaceAll("(\\-|\\.|\\/)", "");
				sb.append(" WHERE TO_CHAR(d.reg_date, 'YYYYMMDD') = ? ");
			} else {
				sb.append(" WHERE INSTR(d." + schType + ", ?) >= 1 ");
			}

			sb.append(" ORDER BY d.data_id DESC ");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");

			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, kwd);
			pstmt.setInt(2, offset);
			pstmt.setInt(3, size);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				DataDTO dto = new DataDTO();

				dto.setData_id(rs.getInt("data_id"));
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
	
	public List<DataDTO> listData(String lecture_code) {
	       List<DataDTO> list = new ArrayList<DataDTO>();
	       PreparedStatement pstmt = null;
	       ResultSet rs = null;
	       StringBuilder sb = new StringBuilder();

	       try {
	           sb.append(" SELECT d.data_id, d.subject, TO_CHAR(d.reg_date, 'YYYY-MM-DD') reg_date, d.hit_count, ");
	           sb.append(" f.save_filename, f.original_filename ");
	           sb.append(" FROM DATA d ");
	           sb.append(" LEFT JOIN data_file f ON d.data_id = f.data_id ");
	           sb.append(" WHERE d.lecture_code = ? ");  
	           sb.append(" ORDER BY d.data_id DESC "); 
	           
	           pstmt = conn.prepareStatement(sb.toString());
	           pstmt.setString(1, lecture_code);      

	           rs = pstmt.executeQuery();

	           while (rs.next()) {
	               DataDTO dto = new DataDTO();
	               dto.setData_id(rs.getInt("data_id"));
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

	
	// 조회수 증가
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

	// 해당 자료 보기
	public DataDTO findById(int data_id) {
		DataDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			 sql = "SELECT d.data_id, d.subject, d.content, d.hit_count, "
				    + "TO_CHAR(d.reg_date, 'YYYYMMDD') reg_date, "
				    + "TO_CHAR(d.modify_date, 'YYYYMMDD') modify_date, "
				    + "NVL(l.subject, '강의 없음') AS lecture_subject, "
				    + "f.save_filename, f.original_filename, f.file_size "
				    + "FROM DATA d "
				    + "LEFT JOIN LECTURE l ON d.lecture_code = l.lecture_code "
				    + "LEFT JOIN data_file f ON d.data_id = f.data_id "
				    + "WHERE d.data_id = ?";
			 
			pstmt = conn.prepareStatement(sql);

			pstmt.setLong(1, data_id);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new DataDTO();

				dto.setData_id(rs.getInt("data_id"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setHit_count(rs.getInt("hit_count"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setLecture_subject(rs.getString("lecture_subject"));
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

	// 이전글
	public DataDTO findByPrev(int data_id, String schType, String kwd) {
		DataDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			if (kwd != null && kwd.length() != 0) {
				sb.append(" SELECT data_id, subject ");
				sb.append(" FROM DATA ");
				sb.append(" WHERE data_id < ? ");

				if (schType.equals("all")) {
					sb.append(" AND ( INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 ) ");
				} else if (schType.equals("reg_date")) {
					kwd = kwd.replaceAll("(\\-|\\/|\\.)", "");
					sb.append(" AND ( TO_CHAR(reg_date, 'YYYYMMDD') = ? ) ");
				} else {
					sb.append(" AND ( INSTR(" + schType + ", ?) >= 1  ) ");
				}

				sb.append(" ORDER BY data_id ASC ");
				sb.append(" FETCH FIRST 1 ROWS ONLY ");

				pstmt = conn.prepareStatement(sb.toString());
				pstmt.setInt(1, data_id);

				if (schType.equals("all")) {
					pstmt.setString(2, kwd);
					pstmt.setString(3, kwd);
				} else {
					pstmt.setString(2, kwd);
				}
			} else {
				sb.append(" SELECT data_id, subject ");
				sb.append(" FROM DATA ");
				sb.append(" WHERE data_id < ? ");
				sb.append(" ORDER BY data_id DESC ");
				sb.append(" FETCH FIRST 1 ROWS ONLY ");

				pstmt = conn.prepareStatement(sb.toString());
				pstmt.setInt(1, data_id);
			}

			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new DataDTO();
				dto.setData_id(rs.getInt("data_id"));
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
	public DataDTO findByNext(int data_id, String schType, String kwd) {
		DataDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			if (kwd != null && kwd.length() != 0) {
				sb.append(" SELECT data_id, subject ");
				sb.append(" FROM DATA ");
				sb.append(" WHERE data_id > ? ");

				if (schType.equals("all")) {
					sb.append(" AND ( INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 ) ");
				} else if ("reg_date".equals(schType)) {
					kwd = kwd.replaceAll("(\\-|\\/|\\.)", "");
					sb.append(" AND TO_CHAR(reg_date, 'YYYYMMDD') = ? ");
				} else {
					sb.append(" AND INSTR(" + schType + ", ?) >= 1 ");
				}

				sb.append(" ORDER BY data_id DESC ");
				sb.append(" FETCH FIRST 1 ROWS ONLY ");

				pstmt = conn.prepareStatement(sb.toString());
				pstmt.setInt(1, data_id);

				if (schType.equals("all")) {
					pstmt.setString(2, kwd);
					pstmt.setString(3, kwd);
				} else {
					pstmt.setString(2, kwd);
				}
			} else {
				sb.append(" SELECT data_id, subject ");
				sb.append(" FROM DATA ");
				sb.append(" WHERE data_id > ? ");
				sb.append(" ORDER BY data_id ASC ");
				sb.append(" FETCH FIRST 1 ROWS ONLY ");

				pstmt = conn.prepareStatement(sb.toString());
				pstmt.setInt(1, data_id);
			}

			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new DataDTO();
				dto.setData_id(rs.getInt("data_id"));
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
	
	//게시물 수정
	public void updateData(DataDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "UPDATE DATA SET subject = ?, content = ? "
					+ " WHERE data_id = ?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getSubject());
			pstmt.setString(2, dto.getContent());
			pstmt.setInt(3, dto.getData_id());
			
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}
	
	//게시물 삭제
	public void deleteData(int data_id, String member_id) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "DELETE FROM data_file WHERE data_id = ?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, data_id);
			
			pstmt.executeUpdate();
			pstmt.close();
			
			sql = "DELETE FROM data WHERE data_id = ? AND member_id = ?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, data_id);
			pstmt.setString(2, member_id);
			
			pstmt.executeUpdate();
			pstmt.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}	
		//댓글 쓰기
		public void insertComment(Data_CommentDTO dto, String member_id) throws SQLException {
			PreparedStatement pstmt = null;
			String sql;
			
			try {
				sql = "INSERT INTO data_comment(comment_id, content, reg_date, parent_comment_id, showReply, block, data_id, member_id) "
						+ " VALUES(DATA_COMMENT_SEQ.NEXTVAL, ?, SYSDATE, ?, ?, ?, ?, ?)";
				
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setString(1, dto.getContent());
				pstmt.setInt(2, dto.getParent_comment_id());
				pstmt.setInt(3, dto.getShowReply());
				pstmt.setInt(4, dto.getBlock());
				pstmt.setInt(5, dto.getData_id());
				pstmt.setString(6, member_id);
				
				pstmt.executeUpdate();
				
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			} finally {
				DBUtil.close(pstmt);
			}
		}
		
		public int dataCountComment(int comment_id) {
			int result = 0;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql;
			
			try {
				sql = "SELECT COUNT(*) FROM data_comment "
						+ " WHERE comment_id = ? AND parent_comment_id = 0 AND block = 0";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, comment_id);
				
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
	
	//댓글리스트
	public List<Data_CommentDTO> listComment(int data_id, int offset, int size) {
		List<Data_CommentDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(" SELECT comment_id, content, ");
			sb.append(" TO_CHAR(reg_date, 'YYYYMMDD')reg_date, ");
			sb.append(" parent_comment_Id, showReply, block, data_id ");
			sb.append(" FROM data_comment ");
			sb.append(" WHERE data_id = ? ");
			sb.append(" ORDER BY comment_id ASC ");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");
			
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, data_id);
			pstmt.setInt(2, offset);
			pstmt.setInt(3, size);
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				Data_CommentDTO dto = new Data_CommentDTO();
				
				dto.setComment_id(rs.getInt("comment_id"));
				dto.setContent(rs.getString("content"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setParent_comment_id(rs.getInt("parent_comment_id"));
				dto.setShowReply(rs.getInt("showReply"));
				dto.setBlock(rs.getInt("block"));
				dto.setData_id(rs.getInt("data_id"));
				
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
	
	public void deleteComment(int comment_id, String member_id) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "DELETE FROM data_comment WHERE comment_id = ? AND member_id = ? ";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, comment_id);
			pstmt.setString(2, member_id);
			
			int result = pstmt.executeUpdate();
			
			if(result == 0) {
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}
	
	// 강의코드 - 강의명
	public List<DataDTO> listLectureByMember(String member_id) {
		List<DataDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "SELECT lecture_code, subject FROM lecture WHERE member_id = ?";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, member_id);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				DataDTO dto = new DataDTO();
				dto.setLecture_code(rs.getString("lecture_code"));
				dto.setSubject(rs.getString("subject"));
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
