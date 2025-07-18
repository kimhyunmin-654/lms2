package com.lms2.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.lms2.model.LectureDTO;
import com.lms2.model.NoticeDTO;
import com.lms2.util.DBConn;
import com.lms2.util.DBUtil;
import com.lms2.util.MyMultipartFile;

public class LectureDAO {
	private Connection conn = DBConn.getConnection();
	
	// 강의 등록
	public void insertLecture(LectureDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			conn.setAutoCommit(false);
			
			sql = "INSERT INTO LECTURE(lecture_code, subject, grade, classroom, division, lecture_year, semester, capacity, credit, department_id, member_id)"
					+ " VALUES(?,?,?,?,?,?,?,?,?,?,?)"; 
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getLecture_code());
			pstmt.setString(2, dto.getSubject());
			pstmt.setInt(3, dto.getGrade());
			pstmt.setString(4, dto.getClassroom());
			pstmt.setString(5, dto.getDivision());
			pstmt.setInt(6, dto.getLecture_year());
			pstmt.setString(7, dto.getSemester());
			pstmt.setInt(8, dto.getCapacity());
			pstmt.setDouble(9, dto.getCredit());
			pstmt.setString(10, dto.getDepartment_id());
			pstmt.setString(11, dto.getMember_id());
			
			pstmt.executeUpdate();
			
			pstmt.close();
			pstmt = null;
			
			if(dto.getListFile() != null && !dto.getListFile().isEmpty()) {
				sql = "INSERT INTO LECTURE_FILE(file_id, save_filename, original_filename, file_size, lecture_code, reg_date) "
						+ "VALUES(LECTURE_FILE_SEQ.NEXTVAL, ?, ?, ?, ?, SYSDATE)";
				
				pstmt = conn.prepareStatement(sql);
				
				for(MyMultipartFile mf : dto.getListFile()) {
					pstmt.setString(1, mf.getSaveFilename());
					pstmt.setString(2, mf.getOriginalFilename());
					pstmt.setLong(3, mf.getSize());
					pstmt.setString(4, dto.getLecture_code());
					
					pstmt.executeUpdate();
					
				}				
			}			
			conn.commit();
		
		} catch (SQLException e) {
			DBUtil.rollback(conn);
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
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
	
	// 강의 전체 리스트
	public List<LectureDTO> listLecture(int offset, int size){
		List<LectureDTO> list = new ArrayList<LectureDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(" SELECT l.member_id, name, lecture_code, subject, grade, classroom, division, lecture_year, semester, capacity, credit, department_id ");	
			sb.append(" FROM lecture l ");
			sb.append(" JOIN member m ON l.member_id = m.member_id ");
			sb.append(" ORDER BY lecture_code DESC ");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");
			
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, offset); 
			pstmt.setInt(2, size);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				LectureDTO dto = new LectureDTO();
				
				dto.setLecture_code(rs.getString("lecture_code"));
				dto.setSubject(rs.getString("subject"));
				dto.setGrade(rs.getInt("grade"));
				dto.setClassroom(rs.getString("classroom"));
				dto.setDivision(rs.getString("division"));
				dto.setLecture_year(rs.getInt("lecture_year"));
				dto.setSemester(rs.getString("semester"));
				dto.setCapacity(rs.getInt("capacity"));
				dto.setCredit(rs.getDouble("credit"));
				dto.setDepartment_id(rs.getString("department_id"));
				dto.setName(rs.getString("name"));
				
				list.add(dto);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return list;
	}
	
	// 검색에서 강의 리스트
	public List<LectureDTO> listLecture(int offset, int size, String schType, String kwd) {
		List<LectureDTO> list = new ArrayList<LectureDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(" SELECT l.member_id, name, lecture_code, subject, grade, classroom, division, lecture_year, semester, capacity, credit, department_id ");
			sb.append(" FROM lecture l ");
			sb.append(" JOIN member m ON l.member_id = m.member_id ");
			if (schType.equals("all")) {
				sb.append(" WHERE INSTR(l.subject, ?) >= 1 OR INSTR(l.division, ?) >= 1");
			} else if (schType.equals("division")) {
				sb.append(" WHERE INSTR(l.division, ?) >= 1");
			} else if (schType.equals("lecture_year")) {
				sb.append( " WHERE INSTR(TO_CHAR(l.lecture_year), ?) >= 1 ");
			} else {
				sb.append(" WHERE INSTR(" + schType + ", ?) >= 1 ");
			}
			
			sb.append(" ORDER BY lecture_code DESC ");
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
				LectureDTO dto = new LectureDTO();
				
				dto.setLecture_code(rs.getString("lecture_code"));
				dto.setSubject(rs.getString("subject"));
				dto.setGrade(rs.getInt("grade"));
				dto.setClassroom(rs.getString("classroom"));
				dto.setDivision(rs.getString("division"));
				dto.setLecture_year(rs.getInt("lecture_year"));
				dto.setSemester(rs.getString("semester"));
				dto.setCapacity(rs.getInt("capacity"));
				dto.setCredit(rs.getDouble("credit"));
				dto.setDepartment_id(rs.getString("department_id"));
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
		
	// 강의수
		public int dataCount() {
			int result = 0;
			
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql;
			
			try {
				sql = " SELECT COUNT(*) FROM lecture ";
				
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
				sql = "SELECT NVL(COUNT(*), 0) FROM lecture l "
						+ " JOIN member m ON l.member_id = m.member_id ";
				if (schType.equals("all")) { // 제목 + 과목
					sql += "  WHERE INSTR(l.subject, ?) >= 1 OR INSTR(l.division, ?) >= 1 ";
				} else if (schType.equals("division")) { // 과목
					sql += " WHERE INSTR(l.division, ?) >= 1";
				} else if (schType.equals("lecture_year")) {
					sql += " WHERE INSTR(TO_CHAR(l.lecture_year), ?) >= 1";
				} else { // 나머지
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
		
		
		public LectureDTO findById(long notice_id) {
			LectureDTO dto = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql;
			
			try {
				sql = " SELECT lecture_code, subject, grade, classroom, division, lecture_year, semester, capacity, credit, department_id, l.member_id "
				    + " FROM lecture l "
				    + " JOIN member m ON l.member_id = m.member_id "
				    + " WHERE lecture_code = ?";
				
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setLong(1, notice_id);
				
				rs = pstmt.executeQuery();
				
				if(rs.next()) {
					dto = new LectureDTO();
					
					dto.setLecture_code(rs.getString("lecture_code"));
					dto.setSubject(rs.getString("subject"));
					dto.setGrade(rs.getInt("grade"));
					dto.setClassroom(rs.getString("classroom"));
					dto.setDivision(rs.getString("division"));
					dto.setLecture_year(rs.getInt("lecture_year"));
					dto.setSemester(rs.getString("semester"));
					dto.setCapacity(rs.getInt("capacity"));
					dto.setCredit(rs.getDouble("credit"));
					dto.setDepartment_id(rs.getString("department_id"));
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
		
		public void updateLecture(LectureDTO dto) throws SQLException {
			PreparedStatement pstmt = null;
			String sql;

			try {
				conn.setAutoCommit(false);
				
				sql = "UPDATE lecture SET subject = ?, grade = ?, classroom = ?, division = ?, lecture_year = ?, semester = ?, capacity = ?, credit = ?"
						+ " WHERE lecture_code = ?";
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setString(1, dto.getSubject());
				pstmt.setInt(2, dto.getGrade());
				pstmt.setString(3, dto.getClassroom());
				pstmt.setString(4, dto.getDivision());
				pstmt.setInt(5, dto.getLecture_year());
				pstmt.setString(6, dto.getSemester());
				pstmt.setInt(7, dto.getCapacity());
				pstmt.setDouble(8, dto.getCredit());
				pstmt.setString(9, dto.getLecture_code());
				
				pstmt.executeUpdate();
				
				pstmt.close();
				pstmt = null;

				if (dto.getListFile() != null && dto.getListFile().size() != 0) {
					sql = "INSERT INTO lecture_file(file_num, save_filename, original_filename, file_size ,lecture_code, reg_date) "
						     + " VALUES (lecture_file_seq.NEXTVAL, ?, ?, ?, ?, SYSDATE)";
						pstmt = conn.prepareStatement(sql);

						for (MyMultipartFile mf : dto.getListFile()) {
						    pstmt.setString(1, mf.getSaveFilename());         
						    pstmt.setString(2, mf.getOriginalFilename());     
						    pstmt.setLong(3, mf.getSize());                   
						    pstmt.setString(4, dto.getLecture_code());
						    
						    pstmt.executeUpdate();
						}
				}
				conn.commit();

			} catch (SQLException e) {
				DBUtil.rollback(conn);
				e.printStackTrace();
				throw e;
			} catch (Exception e) {
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
		
		public void deleteLecture(long lecture_code) throws SQLException {
			PreparedStatement pstmt = null;
			String sql;
			
			try {
				sql = "DELETE FROM lecture WHERE lecture_code = ?";
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setLong(1, lecture_code);
				
				pstmt.executeUpdate();
			
				
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			} finally {
				DBUtil.close(pstmt);
			}
		}
		
		public void deleteLecture(long[] lecture_codes) throws SQLException {
			PreparedStatement pstmt = null;
			String sql;

			try {


				sql = "DELETE FROM lecture WHERE lecture_code = ? ";
				pstmt = conn.prepareStatement(sql);
				
				for(long num : lecture_codes) {
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
		
		public List<LectureDTO> listLectureFile(long lecture_code) {
			List<LectureDTO> list = new ArrayList<>();
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql;

			try {
				sql = "SELECT file_num, save_filename, original_filename, file_size, lecture_code, reg_date "
						+ " FROM lecture_file WHERE lecture_code = ?";
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setLong(1, lecture_code);
				
				rs = pstmt.executeQuery();

				while (rs.next()) {
					LectureDTO dto = new LectureDTO();

					dto.setFile_id(rs.getLong("file_num"));
					dto.setSave_filename(rs.getString("save_filename"));
					dto.setOriginal_filename(rs.getString("original_filename"));
					dto.setFile_size(rs.getInt("file_size"));
					dto.setLecture_code(rs.getString("lecture_code"));
					dto.setReg_date(rs.getString("reg_date"));
					
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
		
		public LectureDTO findByFileId(long file_num) {
			LectureDTO dto = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql;

			try {
				sql = "SELECT file_num, save_filename, original_filename, file_size, lecture_code, reg_date "
						+ " FROM lecture_file WHERE file_num = ?";
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setLong(1, file_num);
				
				rs = pstmt.executeQuery();

				if (rs.next()) {
					dto = new LectureDTO();

					dto.setFile_id(rs.getLong("file_num"));
					dto.setSave_filename(rs.getString("save_filename"));
					dto.setOriginal_filename(rs.getString("original_filename"));
					dto.setFile_size(rs.getInt("file_size"));
					dto.setLecture_code(rs.getString("lecture_code"));
					dto.setReg_date(rs.getString("reg_date"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DBUtil.close(rs);
				DBUtil.close(pstmt);
			}

			return dto;
		}
		
		public void deleteNoticeFile(String mode, long file_num) throws SQLException {
			PreparedStatement pstmt = null;
			String sql;

			try {
				if (mode.equals("all")) {
					sql = "DELETE FROM lecture_file WHERE lecture_code = ?";
				} else {
					sql = "DELETE FROM lecture_file WHERE file_num = ?";
				}
				
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setLong(1, file_num);

				pstmt.executeUpdate();

			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			} finally {
				DBUtil.close(pstmt);
			}

		}
		
		// 사이드바
		public List<LectureDTO> listsidebar(String member_id) throws SQLException {
			List<LectureDTO> list = new ArrayList<>();
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				String sql = "SELECT lecture_code, subject, member_id FROM LECTURE WHERE member_id = ?";

				pstmt = conn.prepareStatement(sql);

				pstmt.setString(1, member_id);
				rs = pstmt.executeQuery();

				while (rs.next()) {
				    LectureDTO dto = new LectureDTO();
				    dto.setLecture_code(rs.getString("lecture_code"));
				    dto.setSubject(rs.getString("subject"));
				    dto.setMember_id(rs.getString("member_id"));
				    list.add(dto);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			return list;
		}
		
		public List<LectureDTO> studentListSidebar(String member_id) throws SQLException {
		    List<LectureDTO> list = new ArrayList<>();
		    PreparedStatement pstmt = null;
		    ResultSet rs = null;

		    try {
		        // 수정된 SQL: 수강신청 테이블과 강의 테이블을 조인
		        String sql = "SELECT l.lecture_code, l.subject, l.member_id " +
		                    "FROM LECTURE l " +
		                    "JOIN COURSE_APPLICATION ca ON l.lecture_code = ca.lecture_code " + 
		                    "WHERE ca.member_id = ? AND ca.status = '신청'";

		        pstmt = conn.prepareStatement(sql);
		        pstmt.setString(1, member_id);
		        rs = pstmt.executeQuery();

		        while (rs.next()) {
		            LectureDTO dto = new LectureDTO();
		            dto.setLecture_code(rs.getString("lecture_code"));
		            dto.setSubject(rs.getString("subject"));
		            dto.setMember_id(rs.getString("member_id")); // 이건 강사의 member_id
		            list.add(dto);
		        }

		    } catch (Exception e) {
		        e.printStackTrace();
		    }

		    return list;
		}
		
		
}
