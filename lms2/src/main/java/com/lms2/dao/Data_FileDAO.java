package com.lms2.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.lms2.model.DataDTO;
import com.lms2.util.DBConn;
import com.lms2.util.DBUtil;

public class Data_FileDAO {

	private Connection conn = DBConn.getConnection();
	public void insertFile(DataDTO dto) throws SQLException {
	    PreparedStatement pstmt = null;
	    String sql;

	    try {
	        sql = "INSERT INTO data_file (file_id, save_filename, original_filename, file_size, data_id) "
	            + "VALUES (data_file_seq.NEXTVAL, ?, ?, ?, ?)";

	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, dto.getSave_filename());
	        pstmt.setString(2, dto.getOriginal_filename());
	        pstmt.setInt(3, dto.getFile_size());
	        pstmt.setInt(4, dto.getData_id());

	        pstmt.executeUpdate();
	    } catch (Exception e) {
	    	e.printStackTrace();
	    } finally {
	        DBUtil.close(pstmt);
	    }
	}
}
