package com.lms2.model;

import java.util.List;

import com.lms2.util.MyMultipartFile;

public class NoticeDTO {
	private long notice_id;
	private int is_notice;
	private String subject;
	private String content;
	private int hit_count;
	private String reg_date;
	private String modify_date;
	private int is_visible;
	private String member_id;
	private String name;
	
	
	private List<MyMultipartFile> listFile;
	
	private long file_id;
	private String save_filename;
	private String original_filename;
	private long file_size;
	
	private long gap;
		
	public long getNotice_id() {
		return notice_id;
	}
	public void setNotice_id(long notice_id) {
		this.notice_id = notice_id;
	}
	public int getIs_notice() {
		return is_notice;
	}
	public void setIs_notice(int is_notice) {
		this.is_notice = is_notice;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getHit_count() {
		return hit_count;
	}
	public void setHit_count(int hit_count) {
		this.hit_count = hit_count;
	}
	public String getReg_date() {
		return reg_date;
	}
	public void setReg_date(String reg_date) {
		this.reg_date = reg_date;
	}
	public String getModify_date() {
		return modify_date;
	}
	public void setModify_date(String modify_date) {
		this.modify_date = modify_date;
	}
	public int getIs_visible() {
		return is_visible;
	}
	public void setIs_visible(int is_visible) {
		this.is_visible = is_visible;
	}
						
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
	public List<MyMultipartFile> getListFile() {
		return listFile;
	}
	public void setListFile(List<MyMultipartFile> listFile) {
		this.listFile = listFile;
	}
	

	public long getFile_id() {
		return file_id;
	}
	public void setFile_id(long file_id) {
		this.file_id = file_id;
	}
	public long getFile_size() {
		return file_size;
	}
	public void setFile_size(long file_size) {
		this.file_size = file_size;
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
	public long getGap() {
		return gap;
	}
	public void setGap(long gap) {
		this.gap = gap;
	}

	
	
}
