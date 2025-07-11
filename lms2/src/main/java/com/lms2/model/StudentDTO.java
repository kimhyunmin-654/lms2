package com.lms2.model;

public class StudentDTO {
	
	private MemberDTO member;
	
	private int grade; // 학년
	private String admission_date; // 입학일
	private String graduate_date; // 졸업일
	private String department_id; // 학과 코드
	
	public MemberDTO getMember() {
		return member;
	}
	public void setMember(MemberDTO member) {
		this.member = member;
	}
	public int getGrade() {
		return grade;
	}
	public void setGrade(int grade) {
		this.grade = grade;
	}
	public String getAdmission_date() {
		return admission_date;
	}
	public void setAdmission_date(String admission_date) {
		this.admission_date = admission_date;
	}
	public String getGraduate_date() {
		return graduate_date;
	}
	public void setGraduate_date(String graduate_date) {
		this.graduate_date = graduate_date;
	}
	public String getDepartment_id() {
		return department_id;
	}
	public void setDepartment_id(String department_id) {
		this.department_id = department_id;
	}

	
	
}
