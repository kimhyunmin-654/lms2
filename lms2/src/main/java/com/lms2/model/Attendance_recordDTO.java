package com.lms2.model;

public class Attendance_recordDTO {
	private int attend_id;
	private String attend_date;
	private String checkin_time;
	private String checkout_time;
	private int status;
	private int course_id;

	private String member_id;
	private String name;
	private String lecture_code;

	private int week;

	public String getMember_id() {
		return member_id;
	}

	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLecture_code() {
		return lecture_code;
	}

	public void setLecture_code(String lecture_code) {
		this.lecture_code = lecture_code;
	}

	public int getAttend_id() {
		return attend_id;
	}

	public void setAttend_id(int attend_id) {
		this.attend_id = attend_id;
	}

	public String getAttend_date() {
		return attend_date;
	}

	public void setAttend_date(String attend_date) {
		this.attend_date = attend_date;
	}

	public String getCheckin_time() {
		return checkin_time;
	}

	public void setCheckin_time(String checkin_time) {
		this.checkin_time = checkin_time;
	}

	public String getCheckout_time() {
		return checkout_time;
	}

	public void setCheckout_time(String checkout_time) {
		this.checkout_time = checkout_time;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getCourse_id() {
		return course_id;
	}

	public void setCourse_id(int course_id) {
		this.course_id = course_id;
	}

	public int getWeek() {
		return week;
	}

	public void setWeek(int week) {
		this.week = week;
	}

}
