package com.lms2.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.lms2.model.Std_hwDTO;
import com.lms2.util.DBConn;
import com.lms2.util.DBUtil;


public class Stuhw_DAO {
	private Connection conn = DBConn.getConnection();
	

    // 과제 목록 조회 (페이징 포함)
	public List<Std_hwDTO> listAssignment(String memberId, int offset, int size, String schType, String kwd) throws SQLException {
	    List<Std_hwDTO> list = new ArrayList<>();
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    String sql;

	    try {
	        kwd = "%" + kwd + "%";

	        if (schType.equals("all")) {
	            sql = "SELECT hw.assign_id, hw.lecture_code, hw.assign_name, hw.assign_content, " +
	                  "TO_CHAR(hw.deadline_date, 'YYYY-MM-DD') AS submit_date, " +
	                  "f.file_id, f.save_filename, f.original_filename, f.file_size " +
	                  "FROM pro_hw hw " +
	                  "JOIN course_application ca ON hw.lecture_code = ca.lecture_code " +
	                  "LEFT JOIN pro_hw_file f ON hw.assign_id = f.assign_id " +
	                  "WHERE ca.member_id = ? AND (hw.assign_name LIKE ? OR hw.assign_content LIKE ?) " +
	                  "ORDER BY hw.assign_id DESC OFFSET ? ROWS FETCH FIRST ? ROWS ONLY";

	            pstmt = conn.prepareStatement(sql);
	            pstmt.setString(1, memberId);
	            pstmt.setString(2, kwd);
	            pstmt.setString(3, kwd);
	            pstmt.setInt(4, offset);
	            pstmt.setInt(5, size);
	        } else {
	            sql = "SELECT hw.assign_id, hw.lecture_code, hw.assign_name, hw.assign_content, " +
	                  "TO_CHAR(hw.deadline_date, 'YYYY-MM-DD') AS submit_date, " +
	                  "f.file_id, f.save_filename, f.original_filename, f.file_size " +
	                  "FROM pro_hw hw " +
	                  "JOIN course_application ca ON hw.lecture_code = ca.lecture_code " +
	                  "LEFT JOIN pro_hw_file f ON hw.assign_id = f.assign_id " +
	                  "WHERE ca.member_id = ? AND hw." + schType + " LIKE ? " +
	                  "ORDER BY hw.assign_id DESC OFFSET ? ROWS FETCH FIRST ? ROWS ONLY";

	            pstmt = conn.prepareStatement(sql);
	            pstmt.setString(1, memberId);
	            pstmt.setString(2, kwd);
	            pstmt.setInt(3, offset);
	            pstmt.setInt(4, size);
	        }

	        rs = pstmt.executeQuery();

	        while (rs.next()) {
	            Std_hwDTO dto = new Std_hwDTO();
	            dto.setAssign_id(rs.getInt("assign_id"));
	            dto.setCourse_id(0); // 필요 시 수정
	            dto.setAssign_name(rs.getString("assign_name"));
	            dto.setAssign_content(rs.getString("assign_content"));
	            dto.setSubmit_date(rs.getString("submit_date"));
	            dto.setFile_id(rs.getInt("file_id"));
	            dto.setSave_filename(rs.getString("save_filename"));
	            dto.setOriginal_filename(rs.getString("original_filename"));
	            dto.setFile_size(rs.getInt("file_size"));
	            
	            list.add(dto);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        DBUtil.close(rs);
	        DBUtil.close(pstmt);
	    }

	    return listAssignment(memberId, offset, size, "all", "");
	}
	public List<Std_hwDTO> listAssignment(String memberId, int offset, int size) throws SQLException {
	    return listAssignment(memberId, offset, size, "all", "");
	}

    // 데이터 개수 조회
    public int dataCount(String memberId) {
        int result = 0;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql;

        try {
            sql = "SELECT COUNT(*) FROM pro_hw hw " +
                  "JOIN course_application ca ON hw.lecture_code = ca.lecture_code " +
                  "WHERE ca.member_id = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, memberId);
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

    // 검색 포함 데이터 개수
    public int dataCount(String memberId, String schType, String kwd) {
        int result = 0;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql;

        try {
            kwd = "%" + kwd + "%";
            if (schType.equals("all")) {
                sql = "SELECT COUNT(*) FROM pro_hw hw " +
                      "JOIN course_application ca ON hw.lecture_code = ca.lecture_code " +
                      "WHERE ca.member_id = ? AND (hw.assign_name LIKE ? OR hw.assign_content LIKE ?)";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, memberId);
                pstmt.setString(2, kwd);
                pstmt.setString(3, kwd);
            } else {
                sql = "SELECT COUNT(*) FROM pro_hw hw " +
                      "JOIN course_application ca ON hw.lecture_code = ca.lecture_code " +
                      "WHERE ca.member_id = ? AND hw." + schType + " LIKE ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, memberId);
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

    // 과제 상세보기
    public Std_hwDTO findById(int assignId) throws SQLException {
        Std_hwDTO dto = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql;

        try {
            sql = "SELECT hw.assign_id, hw.lecture_code, hw.assign_name, hw.assign_content, " +
                  "TO_CHAR(hw.deadline_date, 'YYYY-MM-DD') AS submit_date, " +
                  "f.file_id, f.save_filename, f.original_filename, f.file_size " +
                  "FROM pro_hw hw " +
                  "LEFT JOIN pro_hw_file f ON hw.assign_id = f.assign_id " +
                  "WHERE hw.assign_id = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, assignId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                dto = new Std_hwDTO();
                dto.setAssign_id(rs.getInt("assign_id"));
                dto.setCourse_id(0); // 필요시 lecture_code를 dto에 추가해서 처리
                dto.setAssign_name(rs.getString("assign_name"));
                dto.setAssign_content(rs.getString("assign_content"));
                dto.setSubmit_date(rs.getString("submit_date"));
                dto.setFile_id(rs.getInt("file_id"));
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
}

