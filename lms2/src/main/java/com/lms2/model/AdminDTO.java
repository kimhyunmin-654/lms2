package com.lms2.model;

public class AdminDTO extends MemberDTO {
	
	
	private String position; // 직책
	private String division; // 부서


	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}

}
