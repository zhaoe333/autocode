package com.template.util;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * jackson util 
 * @author yunlu
 *
 */
public class JacksonUtil {
	
	/**缓存常用的acksons对象,不允许向此map写入其他值*/
	private static final Map<String,Jacksons> jacksonsMap = new HashMap<String,Jacksons>();
	
	/**缓存自定义的acksons对象*/
	private static final Map<String,Jacksons> newJacksonsMap = new HashMap<String,Jacksons>();
	
	public enum TimeType{
		/** yyyy-MM-dd HH:mm:ss.SSS*/
		yyyyMMddHHmmssSSS("yyyy-MM-dd HH:mm:ss.SSS"),
		/** yyyy-MM-dd HH:mm:ss*/
		yyyyMMddHHmmss("yyyy-MM-dd HH:mm:ss"),
		/** HH:mm:ss*/
		HHmmss("HH:mm:ss"),
		/** yyyy-MM-dd*/
		yyyyMMdd("yyyy-MM-dd"),
		/** yyyyMMddHHmmssSSS*/
		yyyyMMddHHmmssSSS_t("yyyyMMddHHmmssSSS"),
		/** yyyyMMddHHmmss*/
		yyyyMMddHHmmss_t("yyyyMMddHHmmss"),
		/** HHmmss*/
		HHmmss_t("HHmmss"),
		/** yyyyMMdd*/
		yyyyMMdd_t("yyyyMMdd"),
		/** 毫秒数*/
		millisecond("");
		private String type;
		
		private TimeType(String type) {
			this.type = type;
		}

		public String toString(){
			return this.type;
		}
	}
	
	private static ObjectMapper getMapper()throws Exception{
		ObjectMapper objectMapper = new ObjectMapper();
		// 设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性  
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);  
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false);  
        objectMapper.setSerializationInclusion(Include.NON_NULL);
        return objectMapper;
	}
	/**
	 * 针对常用时间类型，日记记录，第三方对接常用
	 * 获取默认解析json的jacksons
	 * 忽略不存在属性
	 * 忽略为null的属性
	 * 注解有效
	 * 默认时间类型 yyyy-MM-dd HH:mm:ss
	 * @return
	 * @throws Exception 
	 */
	public static Jacksons getReader()throws Exception{
		Jacksons jacksons = jacksonsMap.get("reader");
		if(jacksons==null){
			ObjectMapper mapper = getMapper();
			mapper.setDateFormat(new SimpleDateFormat(TimeType.yyyyMMddHHmmss.toString()));
			jacksons=new Jacksons(mapper);
			jacksonsMap.put("reader", jacksons);
		}
		return jacksons;
	}
	/**
	 * tsp平台交互建议使用
	 * 获取默认解析对象的jacksons
	 * 忽略不存在属性
	 * 忽略为null的属性
	 * 默认时间类型为毫秒数
	 * 默认注解无效
	 * @return
	 * @throws Exception
	 */
	public static Jacksons get()throws Exception{
		Jacksons jacksons = jacksonsMap.get("tsp");
		if(jacksons==null){
			ObjectMapper mapper =  getMapper();
			mapper.configure(MapperFeature.USE_ANNOTATIONS, false);
			jacksons=new Jacksons(mapper);
			jacksonsMap.put("tsp", jacksons);
		}
		return jacksons;
	}
	/**
	 * 根据时间format类型获取jacksons
	 * 忽略不存在属性
	 * 忽略为null的属性
	 * 注解有效
	 * @param timeType
	 * @return
	 * @throws Exception
	 */
	public static Jacksons getReaderByTime(TimeType timeType)throws Exception{
		Jacksons jacksons = jacksonsMap.get(timeType.toString());
		if(jacksons==null){
			ObjectMapper mapper = getMapper();
			if(timeType!=TimeType.millisecond){
				mapper.setDateFormat(new SimpleDateFormat(timeType.toString()));
			}
			jacksons=new Jacksons(mapper);
			jacksonsMap.put(timeType.toString(), jacksons);
		}
		return jacksons;
	}
	/**
	 * 根据时间format获取jacksons
	 * 忽略不存在属性
	 * 忽略为null的属性
	 * 注解有效
	 * @param format
	 * @return
	 * @throws Exception
	 */
	public static Jacksons getReaderByTime(String format)throws Exception{
		Jacksons jacksons = jacksonsMap.get(format);
		if(jacksons==null){
			ObjectMapper mapper = getMapper();
			mapper.setDateFormat(new SimpleDateFormat(format));
			jacksons=new Jacksons(mapper);
			jacksonsMap.put(format, jacksons);
		}
		return jacksons;
	}
	
	/**
	 * 自定义jacksons保存
	 * @param key
	 * @param jacksons
	 * @throws Exception
	 */
	public static void setJacksons(String key,Jacksons jacksons)throws Exception{
		newJacksonsMap.put(key, jacksons);
	}
	/**
	 *  自定义jacksons保存
	 * @param key
	 * @param objectMapper
	 * @throws Exception
	 */
	public static void setJacksons(String key,ObjectMapper objectMapper)throws Exception{
		newJacksonsMap.put(key, new Jacksons(objectMapper));
	}
	/**
	 * 此方法用来获取自定义的jacksons
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static Jacksons get(String key)throws Exception{
		Jacksons jacksons=newJacksonsMap.get(key);
		return jacksons;
	}
}
