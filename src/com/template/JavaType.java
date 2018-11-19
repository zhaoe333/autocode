package com.template;

import java.util.HashMap;
import java.util.Map;

import com.template.util.ByteUtils;

public class JavaType {
	
	public static Map<String, String> preferredJavaTypeForSqlType = new HashMap<String, String>();
	public static Map<String, String> preferredJavaTypeToSqlType = new HashMap<String, String>();
	static {
		preferredJavaTypeForSqlType.put("tinyint", "java.lang.Integer");
		preferredJavaTypeForSqlType.put("smallint", "java.lang.Integer");
		preferredJavaTypeForSqlType.put("mediumint", "java.lang.Integer");
		preferredJavaTypeForSqlType.put("int", "java.lang.Integer");
		preferredJavaTypeForSqlType.put("integer", "java.lang.Integer");
		preferredJavaTypeForSqlType.put("bigint", "java.lang.Long");

		preferredJavaTypeForSqlType.put("float", "java.lang.Double");
		preferredJavaTypeForSqlType.put("double", "java.lang.Double");
		preferredJavaTypeForSqlType.put("real", "java.lang.Double");

		preferredJavaTypeForSqlType.put("decimal", "java.math.BigDecimal");
		preferredJavaTypeForSqlType.put("numeric", "java.math.BigDecimal");

		preferredJavaTypeForSqlType.put("char", "java.lang.String");
		preferredJavaTypeForSqlType.put("varchar", "java.lang.String");
		preferredJavaTypeForSqlType.put("longvarchar", "java.lang.String");
		preferredJavaTypeForSqlType.put("text", "java.lang.String");
		preferredJavaTypeForSqlType.put("longtext", "java.lang.String");
		preferredJavaTypeForSqlType.put("mediumtext", "java.lang.String");

		preferredJavaTypeForSqlType.put("bit", "java.lang.Boolean");

		preferredJavaTypeForSqlType.put("binary", "byte[]");
		preferredJavaTypeForSqlType.put("varbinary", "byte[]");
		preferredJavaTypeForSqlType.put("longvarbinary", "java.io.InputStream");
		preferredJavaTypeForSqlType.put("date", "java.util.Date");
		preferredJavaTypeForSqlType.put("time", "java.util.Time");
		preferredJavaTypeForSqlType.put("datetime", "java.util.Date");
		preferredJavaTypeForSqlType.put("timestamp", "java.sql.Timestamp");
		preferredJavaTypeForSqlType.put("clob", "java.sql.Clob");
		preferredJavaTypeForSqlType.put("blob", "java.sql.Blob");
		preferredJavaTypeForSqlType.put("array", "java.sql.Array");
		preferredJavaTypeForSqlType.put("ref", "java.sql.Ref");
		preferredJavaTypeForSqlType.put("struct", "java.lang.Object");
		preferredJavaTypeForSqlType.put("java_object", "java.lang.Object");
		
		//
		preferredJavaTypeToSqlType.put("java.lang.Integer", "INTEGER");
		preferredJavaTypeToSqlType.put("java.lang.String", "VARCHAR");
		preferredJavaTypeToSqlType.put("java.lang.Long", "BIGINT");
		preferredJavaTypeToSqlType.put("java.lang.Double", "DOUBLE");
		preferredJavaTypeToSqlType.put("java.util.Date", "TIMESTAMP");
	}
	public static void main(String[] args) {
		System.out.println(ByteUtils.asHex(new byte[]{35, 35, -125, -2, 67, 69, 50, 48, 49, 53, 49, 48, 48, 49, 48, 48, 49, 48, 48, 50, 48, 0, 1, -127}));
		System.out.println(ByteUtils.asHex(new byte[]{16, 1, 1, 8, 28, 2, 1, 10, 20, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 4, 10, 20, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 5, 10, 20, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 6, 10, 20, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 35, 35, 5, -2, 67, 69, 50, 48, 49, 53, 49, 48, 48, 49, 48, 48, 49, 48, 48, 50, 48, 0, 0, -105, 16, 1, 1, 8, 23, 0, 3, -1, -1, 2, -113, 92, 40, 47, 100, -1, 2, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 0, 0, 0, 0, 0, 0, 4, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -61, -1, -1, -61, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 0, 0, 0, 0, 6, 0, 0, 0, 0, 19, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -123, 1, 11, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 121, 35, 35, -125, -2, 67, 69, 50, 48, 49, 53, 49, 48, 48, 49, 48, 48, 49, 48, 48, 50, 48, 0, 1, -127, 16, 1, 1, 8, 28, 12, 1, 10, 20, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1}));
	}
}
