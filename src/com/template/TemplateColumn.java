package com.template;

public class TemplateColumn {
	//表名
	private String tableName;
	//字段名
	private String columnName;
	//字段名(java)
	private String name;
	//字段类型
	private String type;
	//是否可以为空 0可以 1不可以
	private byte isNullAble;
	//字符长度
	private long charLength;
	//字节长度
	private long byteLength;
	//数字长度
	private long numLength;
	//小数点后长度
	private long pointLength;
	//字符编码类型
	private String charSetCode;
	//注释
	private String comment;
	//是否主键 0是1不是
	private byte isKey;
	//对于mybatis使用的
	private String jdbcName;
	//0唯一约束 1无唯一约束
	private byte unique;
	public TemplateColumn() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public byte getIsNullAble() {
		return isNullAble;
	}
	public void setIsNullAble(byte isNullAble) {
		this.isNullAble = isNullAble;
	}
	public long getCharLength() {
		return charLength;
	}
	public void setCharLength(long charLength) {
		this.charLength = charLength;
	}
	public long getByteLength() {
		return byteLength;
	}
	public void setByteLength(long byteLength) {
		this.byteLength = byteLength;
	}
	public long getNumLength() {
		return numLength;
	}
	public void setNumLength(long numLength) {
		this.numLength = numLength;
	}
	public long getPointLength() {
		return pointLength;
	}
	public void setPointLength(long pointLength) {
		this.pointLength = pointLength;
	}
	public String getCharSetCode() {
		return charSetCode;
	}
	public void setCharSetCode(String charSetCode) {
		this.charSetCode = charSetCode;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public byte getIsKey() {
		return isKey;
	}
	public void setIsKey(byte isKey) {
		this.isKey = isKey;
	}
	
	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public String getJdbcName() {
		return jdbcName;
	}

	public void setJdbcName(String jdbcName) {
		this.jdbcName = jdbcName;
	}
	
	public byte getUnique() {
		return unique;
	}

	public void setUnique(byte unique) {
		this.unique = unique;
	}

	@Override
	public String toString() {
		return "TemplateColumn [tableName=" + tableName + ", columnName="
				+ columnName + ", name=" + name + ", type=" + type
				+ ", isNullAble=" + isNullAble + ", charLength=" + charLength
				+ ", byteLength=" + byteLength + ", numLength=" + numLength
				+ ", pointLength=" + pointLength + ", charSetCode="
				+ charSetCode + ", comment=" + comment + ", isKey=" + isKey
				+ "]";
	}
	
}
