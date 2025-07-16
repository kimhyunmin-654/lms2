package com.lms2.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.lms2.model.LectureDTO;
import com.lms2.util.DBConn;

public class SidebarDAO {
	private Connection conn = DBConn.getConnection();

    public List<LectureDTO> getAllLectureSubjects() {
        List<LectureDTO> list = new ArrayList<>();
        String sql = "SELECT lecture_code, subject FROM lecture ORDER BY subject ASC";

        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
             
            while (rs.next()) {
                LectureDTO dto = new LectureDTO();
                dto.setLecture_code(rs.getString("lecture_code"));
                dto.setSubject(rs.getString("subject"));
                list.add(dto);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}