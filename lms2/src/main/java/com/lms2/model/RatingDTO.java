package com.lms2.model;

public class RatingDTO {
	private String member_id;
	private int course_id;
	private int middletest_rating;
	private int finaltest_rating;
	private int attendance_rating;
	private int homework_rating;
	private int total_rating;
	private String raiting;
	private String reg_date;
	private String modify_date;
	
	public String getMember_id() {
		return member_id;
	}
	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}
	public int getCourse_id() {
		return course_id;
	}
	public void setCourse_id(int course_id) {
		this.course_id = course_id;
	}
	public int getMiddletest_rating() {
		return middletest_rating;
	}
	public void setMiddletest_rating(int middletest_rating) {
		this.middletest_rating = middletest_rating;
	}
	public int getFinaltest_rating() {
		return finaltest_rating;
	}
	public void setFinaltest_rating(int finaltest_rating) {
		this.finaltest_rating = finaltest_rating;
	}
	public int getAttendance_rating() {
		return attendance_rating;
	}
	public void setAttendance_rating(int attendance_rating) {
		this.attendance_rating = attendance_rating;
	}
	public int getHomework_rating() {
		return homework_rating;
	}
	public void setHomework_rating(int homework_rating) {
		this.homework_rating = homework_rating;
	}
	public int getTotal_rating() {
		return total_rating;
	}
	public void setTotal_rating(int total_rating) {
		this.total_rating = total_rating;
	}
	public String getRaiting() {
		return raiting;
	}
	public void setRaiting(String raiting) {
		this.raiting = raiting;
	}
	public String getReg_date() {
		return reg_date;
	}
	public void setReg_date(String reg_date) {
		this.reg_date = reg_date;
	}
	public String getModify_date() {
		return modify_date;
	}
	public void setModify_date(String modify_date) {
		this.modify_date = modify_date;
	}
	
	
}
