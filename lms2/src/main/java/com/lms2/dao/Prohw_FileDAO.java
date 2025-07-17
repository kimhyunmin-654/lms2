package com.lms2.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.lms2.model.Pro_hwDTO;
import com.lms2.util.DBConn;
import com.lms2.util.DBUtil;

public class Prohw_FileDAO {

	private Connection conn = DBConn.getConnection();
	public void insertFile(Pro_hwDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "INSERT INTO homework_file (file_id, save_filename, original_filename, file_size, homework_id)"
					+ " VALUES(HOMEWORK_FILE_SEQ.NEXTAVAL, ?, ?, ?, ?)";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getSave_filename());
			pstmt.setString(2, dto.getOriginal_filename());
			pstmt.setInt(3, dto.getFile_size());
			pstmt.setInt(4, dto.getHomework_id());
			
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(pstmt);
		}
		
	}
}
