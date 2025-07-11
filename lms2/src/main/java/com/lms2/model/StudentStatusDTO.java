package com.lms2.model;

public class StudentStatusDTO {

	private int history_id; // 이력 ID
	private String year; // 년도
	private String semester; // 학기
	private int grade; // 학년
	private String academic_status; // 학적 상태
	private String reg_date; // 생성 일시
	private String member_id; // 학번

	public int getHistory_id() {
		return history_id;
	}

	public void setHistory_id(int history_id) {
		this.history_id = history_id;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getSemester() {
		return semester;
	}

	public void setSemester(String semester) {
		this.semester = semester;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public String getAcademic_status() {
		return academic_status;
	}

	public void setAcademic_status(String academic_status) {
		this.academic_status = academic_status;
	}

	public String getReg_date() {
		return reg_date;
	}

	public void setReg_date(String reg_date) {
		this.reg_date = reg_date;
	}

	public String getMember_id() {
		return member_id;
	}

	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}

}
