package com.lms2.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
//import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.lms2.model.DepartmentDTO;
import com.lms2.model.LectureDTO;
import com.lms2.model.ProfessorDTO;
import com.lms2.util.DBConn;
import com.lms2.util.DBUtil;

public class ProfessorDAO {
	private Connection conn = DBConn.getConnection();
	
	// 교수 등록
	public void insertProfessor(ProfessorDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			conn.setAutoCommit(false);
			
			sql = " INSERT INTO member(member_id, name, password, role, create_date, modify_date, avatar, email, phone, birth, addr1, addr2, zip) "
					+ " VALUES(?, ?, ?, 51, SYSDATE, SYSDATE, ?, ?, ?, TO_DATE(?, 'YYYY-MM-DD'), ?, ?, ?) ";
			
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
			pstmt.setString(10, dto.getZip());
			
			pstmt.executeUpdate();
			pstmt.close();
			pstmt = null;
			
			sql = "INSERT INTO professor(member_id, position, department_id) "
					+ "	VALUES(?, ? ,?) ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getMember_id());
			pstmt.setString(2, dto.getPosition());
			pstmt.setString(3, dto.getDepartment_id());
			
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
	
	// 교수 검색
	public ProfessorDTO findById(String member_id){
		ProfessorDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = " SELECT m.member_id, name, password, role ,create_date, modify_date, avatar, email, phone, TO_CHAR(birth, 'YYYY-MM-DD') birth, addr1, addr2, zip, p.position, p.department_id, d.department_name"
					+ " FROM member m "
					+ " LEFT OUTER JOIN professor p ON m.member_id = p.member_id "
					+ " LEFT OUTER JOIN department d ON p.department_id = d.department_id "
					+ " WHERE m.member_id = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, member_id);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new ProfessorDTO();
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
				dto.setZip(rs.getString("zip"));
				
				dto.setPosition(rs.getString("position"));
				dto.setDepartment_name(rs.getString("department_name"));
				dto.setDepartment_id(rs.getString("department_id"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return dto;
	}
	
	// 교수 개수
		public int dataCount() {
			int result = 0;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql;
			
			try {
				sql  = "SELECT COUNT(*) FROM member WHERE role = 51";
				
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
		
		// 검색에서 교수 개수
		public int dataCount(String schType, String kwd) {
			int result = 0;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql;
			
			try {
				sql = "SELECT COUNT(*) "
						+ " FROM member m "
						+ " JOIN professor p ON m.member_id = p.member_id "
						+ " WHERE role = 51";
				
				if(schType.equals("all")) { // 교번 또는 이름
					sql += " AND ( INSTR(member_id, ?) >= 1 OR INSTR(name, ?) >= 1 ) ";
				} else if(schType.equals("reg_date")) { // 등록일자 검색(YYYYMMDD 형식)
					kwd = kwd.replaceAll("(\\-|\\.|\\/)", "");
					sql += "AND TO_CHAR(reg_date, 'YYYYMMDD') = ? ";
				} else if (schType.equals("name")) {
					sql += " WHERE INSTR(m.name, ?) >= 1";
				} else if (schType.equals("department_id")) {
					sql += " WHERE INSTR(p.department_id, ?) >= 1";		
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
		
	

	
	// 교수  리스트 [관리자]
	public List<ProfessorDTO> listProfessor(int offset, int size){
		List<ProfessorDTO> list = new ArrayList<ProfessorDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(" SELECT m.member_id, m.name, TO_CHAR(birth, 'YYYY-MM-DD') birth, m.email, m.phone, p.position, d.department_name, p.department_id");
			sb.append(" FROM member m");
			sb.append(" left outer JOIN professor p ON p.member_id = m.member_id");
			sb.append(" left outer JOIN department d ON p.department_id = d.department_id");
			sb.append(" WHERE role = 51");
			sb.append(" ORDER BY member_id DESC ");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setInt(1, offset);
			pstmt.setInt(2, size);
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				ProfessorDTO dto = new ProfessorDTO();
				
				dto.setMember_id(rs.getString("member_id"));
				dto.setName(rs.getString("name"));
				dto.setBirth(rs.getString("birth"));
				dto.setEmail(rs.getString("email"));
				dto.setPhone(rs.getString("phone"));
				dto.setPosition(rs.getString("position"));
				dto.setDepartment_name(rs.getString("department_name"));
				dto.setDepartment_id(rs.getString("department_id"));
				
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
	
	// 검색에서 교수 리스트
	public List<ProfessorDTO> listProfessor(int offset, int size, String schType, String kwd) {
		List<ProfessorDTO> list = new ArrayList<ProfessorDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(" SELECT m.member_id, m.name, TO_CHAR(m.birth, 'YYYY-MM-DD') birth, m.email, m.phone, p.position, p.department_id ");
			sb.append(" FROM member m ");
			sb.append(" JOIN professor p ON m.member_id = p.member_id");
			sb.append(" WHERE role = 51");			
			if(schType.equals("all")) { // 교번 또는 이름
				sb.append(" AND ( INSTR(m.member_id, ?) >= 1 OR INSTR(m.name, ?) >= 1 ) ");
			} else if(schType.equals("reg_date")) { // 등록일자 검색(YYYYMMDD 형식)
				kwd = kwd.replaceAll("(\\-|\\.|\\/)", "");
				sb.append("AND TO_CHAR(m.create_date, 'YYYYMMDD') = ? ");
			} else if (schType.equals("name")) {
				sb.append(" AND INSTR(m.name, ?) >= 1");
			} else if (schType.equals("department_id")) {
				sb.append(" AND INSTR(p.department_id, ?) >= 1");		
			} else { // 기타 email, phone, birth, addr 등
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
				ProfessorDTO dto = new ProfessorDTO();
				
				dto.setMember_id(rs.getString("member_id"));
				dto.setName(rs.getString("name"));
				dto.setBirth(rs.getString("birth"));
				dto.setEmail(rs.getString("email"));
				dto.setPhone(rs.getString("phone"));
				dto.setPosition(rs.getString("position"));
				dto.setDepartment_id(rs.getString("department_id"));
				
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
	
	// 담당 교수 강의실 목록
	public List<LectureDTO> plistLecture(String member_id) {
		List<LectureDTO> list = new ArrayList<LectureDTO>();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		

		try {
			sb.append(" SELECT lecture_code, grade, subject, classroom, division, capacity, credit ");
			sb.append(" FROM LECTURE ");
			sb.append(" WHERE member_id = ? ");
			sb.append(" ORDER BY Lecture_year DESC ");

			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, member_id);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				LectureDTO dto = new LectureDTO();
				
				dto.setLecture_code(rs.getString("lecture_code"));
				dto.setGrade(rs.getInt("grade"));
				dto.setSubject(rs.getString("subject"));
				dto.setClassroom(rs.getString("classroom"));
				dto.setDivision(rs.getString("division")); // 분류
				dto.setCapacity(rs.getInt("capacity")); // 수강정원
				dto.setCredit(rs.getDouble("credit"));
				list.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}
	
	// lecture로 옮겨야함 강의 상세 정보
	public List<LectureDTO> lectureDetail() {
	    List<LectureDTO> list = new ArrayList<LectureDTO>();
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;

	    try {
	    	String sql = "SELECT member_id, lecture_code, grade, subject, classroom, division, capacity "
	    	           + "FROM LECTURE WHERE lecture_code = ?";

	        pstmt = conn.prepareStatement(sql);
	        rs = pstmt.executeQuery();

	        while (rs.next()) {
	        	
	            LectureDTO dto = new LectureDTO();
	            
	            dto.setLecture_code(rs.getString("lecture_code"));
	            dto.setGrade(rs.getInt("grade"));
	            dto.setSubject(rs.getString("subject"));
	            dto.setClassroom(rs.getString("classroom"));
	            dto.setDivision(rs.getString("division"));
	            dto.setCapacity(rs.getInt("capacity"));
	            
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
	
	// lecture로 옮겨야함(임시)
	public LectureDTO findById1(String lecture_code) {
		LectureDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			  sql = "SELECT lecture_code, grade, subject, classroom, division, capacity "
	                   + "FROM LECTURE WHERE lecture_code = ?";
			  
			  pstmt = conn.prepareStatement(sql);
			  
			  pstmt.setString(1, lecture_code);
			  
			  rs = pstmt.executeQuery();
			  
			  if(rs.next()) {
				  dto = new LectureDTO();
				  
		          dto.setLecture_code(rs.getString("lecture_code"));
		          dto.setGrade(rs.getInt("grade"));
		          dto.setSubject(rs.getString("subject"));
		          dto.setClassroom(rs.getString("classroom"));
		          dto.setDivision(rs.getString("division"));
		          dto.setCapacity(rs.getInt("capacity"));
			  }
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return dto;
	}
	
	// 강의
	public List<DepartmentDTO> listDepartment(){
		List<DepartmentDTO> list = new ArrayList<DepartmentDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(" SELECT department_id, department_name ");
			sb.append(" FROM DEPARTMENT");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				DepartmentDTO dto = new DepartmentDTO();
				
				dto.setDepartment_id(rs.getString("department_id"));
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
	
	
	// 교수 수정
	public void updateProfessor(ProfessorDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			conn.setAutoCommit(false);
			
			sql = "UPDATE member SET name = ?, password = ?, avatar = ?, email = ?, phone = ?, birth = TO_DATE(?, 'YYYY-MM-DD'), addr1 = ?, addr2 = ?, zip = ? "
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
			pstmt.setString(9, dto.getZip());
			pstmt.setString(10, dto.getMember_id());
			pstmt.executeUpdate();
			pstmt.close();

			sql = "UPDATE professor SET position = ?, department_id = ? WHERE member_id = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getPosition());
			pstmt.setString(2, dto.getDepartment_id());
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

	
	// 교수 삭제
	public void deleteProfessor(String member_id) throws SQLException{
		PreparedStatement pstmt = null;
		String sql;
		
		try {				
				sql = " DELETE FROM professor WHERE member_id = ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, member_id);
				
				pstmt.executeUpdate();
				pstmt.close();
				pstmt = null;
				
				sql = " DELETE FROM member WHERE  member_id = ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, member_id);
				
				pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
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
}
