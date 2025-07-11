package com.lms2.model;

public class ProfessorDTO extends MemberDTO {

	private String position;
	private String department_id;

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getDepartment_id() {
		return department_id;
	}

	public void setDepartment_id(String department_id) {
		this.department_id = department_id;
	}

}
