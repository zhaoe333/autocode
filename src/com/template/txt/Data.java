//package com.template.txt;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.Id;
//import javax.persistence.Table;
//
//import org.hibernate.annotations.GenericGenerator;
//
///**
// * Entity类
// * @author zyl
// * @date 2016-5-12
// */
//@Entity
//@Table(name = "cm_data")
//public class Data implements java.io.Serializable {
//	
//	/***/
//	private java.lang.String id;
//	/**key*/
//	private java.lang.String key;
//	/**value*/
//	private java.lang.String value;
//	/**数据类型 1告警code*/
//	private java.lang.Integer type;
//    
//    public Data() {
//
//    }
//
//    @GenericGenerator(name = "generator", strategy = "uuid.hex")
//    @GeneratedValue(generator = "generator")
//    @Id		
//	@Column(name = "id",  unique = true, nullable = false)		
//	public java.lang.String getId() {
//        return this.id;
//    }
//    public void setId(java.lang.String id) {
//        this.id = id;
//    }
//	@Column(name = "key",   nullable = false)	
//	public java.lang.String getKey() {
//        return this.key;
//    }
//    public void setKey(java.lang.String key) {
//        this.key = key;
//    }
//	@Column(name = "value",   nullable = false)	
//	public java.lang.String getValue() {
//        return this.value;
//    }
//    public void setValue(java.lang.String value) {
//        this.value = value;
//    }
//	@Column(name = "type",   nullable = false)	
//	public java.lang.Integer getType() {
//        return this.type;
//    }
//    public void setType(java.lang.Integer type) {
//        this.type = type;
//    }
//}