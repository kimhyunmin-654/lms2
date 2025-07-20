package com.lms2.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.lms2.model.MemberDTO;
import com.lms2.util.DBConn;
import com.lms2.util.DBUtil;

public class MemberDAO {
	private Connection conn = DBConn.getConnection();

	public MemberDTO loginMember(String member_id, String password) {
		MemberDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT  m1.member_id, name, m1.password, role, create_date, modify_date, avatar, email, phone, birth, addr1, addr2, zip, a1.division "
					+ " FROM member m1 " + " LEFT OUTER JOIN student s1 ON m1.member_id = s1.member_id "
					+ " LEFT OUTER JOIN professor p1 ON m1.member_id = p1.member_id "
					+ " LEFT OUTER JOIN admin a1 ON m1.member_id = a1.member_id "
					+ " WHERE m1.member_id = ? AND m1.password = ?";

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, member_id);
			pstmt.setString(2, password);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new MemberDTO();

				dto.setMember_id(rs.getString("member_id"));
				dto.setName(rs.getString("name"));
				dto.setPassword(rs.getString("password"));
				dto.setRole(rs.getInt("role"));
				dto.setCreate_date(rs.getString("create_date"));
				dto.setModify_date(rs.getString("modify_date"));
				dto.setAvatar(rs.getString("avatar"));
				dto.setEmail(rs.getString("email"));
				dto.setPhone(rs.getString("phone"));
				dto.setBirth(rs.getString("birth"));
				dto.setAddr1(rs.getString("addr1"));
				dto.setAddr2(rs.getString("addr2"));
				dto.setZip(rs.getString("zip"));
				dto.setDivision(rs.getString("division"));

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return dto;
	}

	public MemberDTO findById(String member_id) {
		MemberDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT member_id, name, password, role, create_date, modify_date, avatar, phone, email, birth, addr1, addr2, zip "
					+ " FROM member "
					+ " WHERE member_id = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, member_id);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new MemberDTO();
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
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return dto;
	}
	
	public void updateMember(MemberDTO dto) throws SQLException {
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

	public List<MemberDTO> listProfessor() throws SQLException {
		List<MemberDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT member_id, name FROM member WHERE role = 51 ORDER BY name ASC";

			pstmt = conn.prepareStatement(sql);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				MemberDTO dto = new MemberDTO();
				dto.setMember_id(rs.getString("member_id"));
				dto.setName(rs.getString("name"));
				list.add(dto);

			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return list;
	}

	public void deleteAvatar(String member_id) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "UPDATE member SET avatar = null WHERE member_id = ? ";

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