package com.lms2.model;

public class LectureDTO {
	private String lecture_code;
	private String subject;
	private int grade;
	private String classroom;
	private String division;
	private int lecture_year;
	private String semester;
	private int capacity;
	private String credit;
	private String department_id;
	private String member_id;
	private int file_id;
	private String save_filename;
	private String original_filename;
	private int file_size;
	private String reg_date;
	
	public String getLecture_code() {
		return lecture_code;
	}
	public void setLecture_code(String lecture_code) {
		this.lecture_code = lecture_code;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public int getGrade() {
		return grade;
	}
	public void setGrade(int grade) {
		this.grade = grade;
	}
	public String getClassroom() {
		return classroom;
	}
	public void setClassroom(String classroom) {
		this.classroom = classroom;
	}
	public String getDivision() {
		return division;
	}
	public void setDivision(String division) {
		this.division = division;
	}
	public int getLecture_year() {
		return lecture_year;
	}
	public void setLecture_year(int lecture_year) {
		this.lecture_year = lecture_year;
	}
	public String getSemester() {
		return semester;
	}
	public void setSemester(String semester) {
		this.semester = semester;
	}
	public int getCapacity() {
		return capacity;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	public String getCredit() {
		return credit;
	}
	public void setCredit(String credit) {
		this.credit = credit;
	}
	public String getDepartment_id() {
		return department_id;
	}
	public void setDepartment_id(String department_id) {
		this.department_id = department_id;
	}
	public String getMember_id() {
		return member_id;
	}
	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}
	public int getFile_id() {
		return file_id;
	}
	public void setFile_id(int file_id) {
		this.file_id = file_id;
	}
	public String getSave_filename() {
		return save_filename;
	}
	public void setSave_filename(String save_filename) {
		this.save_filename = save_filename;
	}
	public String getOriginal_filename() {
		return original_filename;
	}
	public void setOriginal_filename(String original_filename) {
		this.original_filename = original_filename;
	}
	public int getFile_size() {
		return file_size;
	}
	public void setFile_size(int file_size) {
		this.file_size = file_size;
	}
	public String getReg_date() {
		return reg_date;
	}
	public void setReg_date(String reg_date) {
		this.reg_date = reg_date;
	}
}
