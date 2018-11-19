package com.template;

public class TemplateTable {
	//表名
	private String tableName;
	//注解
	private String comment;
	//判断是否有delete_flag字段
	private boolean hasDeleteFlag=false;
	
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public boolean isHasDeleteFlag() {
		return hasDeleteFlag;
	}
	public void setHasDeleteFlag(boolean hasDeleteFlag) {
		this.hasDeleteFlag = hasDeleteFlag;
	}
	
	
}
