package com.lms2.model;

public class StudentDTO extends MemberDTO {

	private int grade;
	private String admission_date;
	private String graduate_date;
	private String department_id;
	private String department_name;
	private String academic_status;

	private String lecture_code;
	private String apply_status;

	private int status_id;

	public String getLecture_code() {
		return lecture_code;
	}

	public void setLecture_code(String lecture_code) {
		this.lecture_code = lecture_code;
	}

	public String getApply_status() {
		return apply_status;
	}

	public void setApply_status(String apply_status) {
		this.apply_status = apply_status;
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

	public String getDepartment_name() {
		return department_name;
	}

	public void setDepartment_name(String department_name) {
		this.department_name = department_name;
	}

	public String getAcademic_status() {
		return academic_status;
	}

	public void setAcademic_status(String academic_status) {
		this.academic_status = academic_status;
	}
	

	public int getStatus_id() {
	    return status_id;
	}

	public void setStatus_id(int status_id) {
	    this.status_id = status_id;
	}
	
}