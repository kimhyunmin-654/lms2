package com.lms2.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.lms2.model.RatingDTO;
import com.lms2.util.DBConn;
import com.lms2.util.DBUtil;

public class RatingDAO {
    private Connection conn = DBConn.getConnection();

    public void insertRating(RatingDTO dto) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql;

        try {
            conn.setAutoCommit(false);

            // 성적 입력 여부 확인
            sql = "SELECT member_id FROM RATING WHERE COURSE_ID = ? AND MEMBER_ID = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, dto.getCourse_id());
            pstmt.setString(2, dto.getMember_id());
            rs = pstmt.executeQuery();

            boolean status = rs.next(); // 입력 여부

            DBUtil.close(rs);
            DBUtil.close(pstmt);

            if (status) {
                sql = "UPDATE rating SET "
                        + " middletest_rating = ?, finaltest_rating = ?, attendance_rating = ?, "
                        + " homework_rating = ?, total_rating = ?, rating = ?, modify_date = SYSDATE "
                        + " WHERE member_id = ? AND course_id = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, dto.getMiddletest_rating());
                pstmt.setInt(2, dto.getFinaltest_rating());
                pstmt.setInt(3, dto.getAttendance_rating());
                pstmt.setInt(4, dto.getHomework_rating());
                pstmt.setInt(5, dto.getTotal_rating());
                pstmt.setString(6, dto.getRating());
                pstmt.setString(7, dto.getMember_id());
                pstmt.setInt(8, dto.getCourse_id());

            } else {
                sql = "INSERT INTO rating (member_id, course_id, middletest_rating, finaltest_rating, attendance_rating, "
                        + "homework_rating, total_rating, rating, reg_date) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, SYSDATE)";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, dto.getMember_id());
                pstmt.setInt(2, dto.getCourse_id());
                pstmt.setInt(3, dto.getMiddletest_rating());
                pstmt.setInt(4, dto.getFinaltest_rating());
                pstmt.setInt(5, dto.getAttendance_rating());
                pstmt.setInt(6, dto.getHomework_rating());
                pstmt.setInt(7, dto.getTotal_rating());
                pstmt.setString(8, dto.getRating());
            }

            pstmt.executeUpdate();
            conn.commit();

        } catch (SQLException e) {
            DBUtil.rollback(conn);
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(pstmt);
            DBUtil.close(rs);
            try {
                conn.setAutoCommit(true);
            } catch (Exception e2) {
            }
        }
    }

    public List<RatingDTO> listrating(int offset, int size, String lecture_code) {
        List<RatingDTO> list = new ArrayList<RatingDTO>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        StringBuilder sb = new StringBuilder();

        try {
            sb.append("SELECT c.member_id, m.name, c.course_id, ");
            sb.append(" NVL(r.middletest_rating, 0) AS middletest_rating, ");
            sb.append(" NVL(r.finaltest_rating, 0) AS finaltest_rating, ");
            sb.append(" NVL(r.attendance_rating, 0) AS attendance_rating, ");
            sb.append(" NVL(r.homework_rating, 0) AS homework_rating, ");
            sb.append(" NVL(r.total_rating, 0) AS total_rating, ");
            sb.append(" NVL(r.rating, '-') AS rating ");
            sb.append("FROM course_application c ");
            sb.append("JOIN member m ON c.member_id = m.member_id ");
            sb.append("LEFT JOIN rating r ON c.course_id = r.course_id AND c.member_id = r.member_id ");
            sb.append("WHERE c.lecture_code = ? ");
            sb.append("AND c.apply_status = '신청' ");
            sb.append("ORDER BY c.member_id DESC ");
            sb.append("OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");

            pstmt = conn.prepareStatement(sb.toString());
            pstmt.setString(1, lecture_code);
            pstmt.setInt(2, offset);
            pstmt.setInt(3, size);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                RatingDTO dto = new RatingDTO();
                dto.setMember_id(rs.getString("member_id"));
                dto.setName(rs.getString("name"));
                dto.setCourse_id(rs.getInt("course_id"));
                dto.setMiddletest_rating(rs.getInt("middletest_rating"));
                dto.setFinaltest_rating(rs.getInt("finaltest_rating"));
                dto.setAttendance_rating(rs.getInt("attendance_rating"));
                dto.setHomework_rating(rs.getInt("homework_rating"));
                dto.setTotal_rating(rs.getInt("total_rating"));
                dto.setRating(rs.getString("rating"));

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
    
    public List<RatingDTO> std_listrating(int offset, int size, String member_id) {
        List<RatingDTO> list = new ArrayList<RatingDTO>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        StringBuilder sb = new StringBuilder();

        try {
        	sb.append("SELECT r.member_id, m1.name AS student_name, l.lecture_code, l.subject, ");
        	sb.append("r.course_id, middletest_rating, finaltest_rating, attendance_rating, homework_rating, ");
        	sb.append("total_rating, rating, m.name AS professor_name ");
        	sb.append("FROM RATING r ");
        	sb.append("JOIN COURSE_APPLICATION c ON r.course_id = c.course_id ");
        	sb.append("JOIN student s ON r.member_id = s.member_id ");
        	sb.append("JOIN LECTURE l ON c.lecture_code = l.lecture_code ");
        	sb.append("JOIN professor p ON l.member_id = p.member_id ");
        	sb.append("JOIN member m ON p.member_id = m.member_id ");
        	sb.append("JOIN member m1 ON r.member_id = m1.member_id ");
        	sb.append("WHERE r.member_id = ? ");
        	sb.append("ORDER BY l.lecture_code DESC ");
        	sb.append("OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");

            pstmt = conn.prepareStatement(sb.toString());
            pstmt.setString(1, member_id);
            pstmt.setInt(2, offset);
            pstmt.setInt(3, size);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                RatingDTO dto = new RatingDTO();
                
                dto.setMember_id(rs.getString("member_id"));
                dto.setStudentName(rs.getString("student_name"));   
                dto.setProfessorName(rs.getString("professor_name")); 
                dto.setCourse_id(rs.getInt("course_id"));
                dto.setMiddletest_rating(rs.getInt("middletest_rating"));
                dto.setFinaltest_rating(rs.getInt("finaltest_rating"));
                dto.setAttendance_rating(rs.getInt("attendance_rating"));
                dto.setHomework_rating(rs.getInt("homework_rating"));
                dto.setTotal_rating(rs.getInt("total_rating"));
                dto.setRating(rs.getString("rating"));
                dto.setSubject(rs.getString("subject"));
                dto.setLectureCode(rs.getString("lecture_code"));

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