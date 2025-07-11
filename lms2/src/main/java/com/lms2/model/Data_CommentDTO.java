package com.lms2.model;

public class Data_CommentDTO {
	private int comment_id;
	private String content;
	private String reg_date;
	private int parent_comment_id;  
	private int showReply;
	private int block;
	private int data_id; 
	
	public int getComment_id() {
		return comment_id;
	}
	public void setComment_id(int comment_id) {
		this.comment_id = comment_id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getReg_date() {
		return reg_date;
	}
	public void setReg_date(String reg_date) {
		this.reg_date = reg_date;
	}
	public int getParent_comment_id() {
		return parent_comment_id;
	}
	public void setParent_comment_id(int parent_comment_id) {
		this.parent_comment_id = parent_comment_id;
	}
	public int getShowReply() {
		return showReply;
	}
	public void setShowReply(int showReply) {
		this.showReply = showReply;
	}
	public int getBlock() {
		return block;
	}
	public void setBlock(int block) {
		this.block = block;
	}
	public int getData_id() {
		return data_id;
	}
	public void setData_id(int data_id) {
		this.data_id = data_id;
	}
	
	

}
