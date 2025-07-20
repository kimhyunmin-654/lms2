package com.lms2.model;

import java.util.List;

import com.lms2.util.MyMultipartFile;

public class LectureDTO {
	private String lecture_code; // 강의 코드
	private String subject; // 강의 명
	private int grade; // 학년
	private String classroom; // 강의실
	private String division; // 분류
	private int lecture_year; // 개설연도
	private String semester; // 학기
	private int capacity; // 수강 정원
	private double credit; // 학점
	private String department_id; // 학과 코드
	private String member_id; // 학번
	private String department_name; // 학과 이름
	
	private List<MyMultipartFile> listFile;
	
	private long file_id; // 파일 번호
	private String save_filename; // 저장된 파일명
	private String original_filename; // 업로드한 원래 파일명
	private int file_size; // 파일 크기
	private String reg_date; // 등록일시
	
	private String name;
	
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
	public double getCredit() {
		return credit;
	}
	public void setCredit(double credit) {
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
	
	public long getFile_id() {
		return file_id;
	}
	public void setFile_id(long file_id) {
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<MyMultipartFile> getListFile() {
		return listFile;
	}
	public void setListFile(List<MyMultipartFile> listFile) {
		this.listFile = listFile;
	}
	public String getDepartment_name() {
		return department_name;
	}
	public void setDepartment_name(String department_name) {
		this.department_name = department_name;
	}
	
	
	
	

}
