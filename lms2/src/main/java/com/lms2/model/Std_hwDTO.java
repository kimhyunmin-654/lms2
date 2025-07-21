package com.lms2.model;

public class Std_hwDTO {
	private int assign_id;
	private int course_id;
	private int assign_status;
	private String submit_date;
	private String assign_name;
	private String assign_content;
	private int file_id;
	private String save_filename;
	private String original_filename;
	private long file_size;
	public int getAssign_id() {
		return assign_id;
	}
	public void setAssign_id(int assign_id) {
		this.assign_id = assign_id;
	}
	public int getCourse_id() {
		return course_id;
	}
	public void setCourse_id(int course_id) {
		this.course_id = course_id;
	}
	public int getAssign_status() {
		return assign_status;
	}
	public void setAssign_status(int assign_status) {
		this.assign_status = assign_status;
	}
	public String getSubmit_date() {
		return submit_date;
	}
	public void setSubmit_date(String submit_date) {
		this.submit_date = submit_date;
	}
	public String getAssign_name() {
		return assign_name;
	}
	public void setAssign_name(String assign_name) {
		this.assign_name = assign_name;
	}
	public String getAssign_content() {
		return assign_content;
	}
	public void setAssign_content(String assign_content) {
		this.assign_content = assign_content;
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
	public long getFile_size() {
		return file_size;
	}
	public void setFile_size(long file_size) {
		this.file_size = file_size;
	}

}
