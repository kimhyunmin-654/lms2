package com.lms2.model;

public class DeparmentDTO {
	private String department_id; // 학과 코드
	private String department_name; // 학과 명칭
	
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
}
