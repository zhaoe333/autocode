package com.template.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.Assert;

/**
 * 对象转换工具
 * 
 * 
 */
public class BeanUtil {

	/**
	 * 复制Properties(包含fieldValue为null的属性)
	 * 
	 * @param source
	 * @param target
	 * @return
	 * @throws Exception
	 */
	public static Object copyProperties(Object source, Object target)
			throws Exception {
		if (null == source)
			return source;

		List<Field> fields = getAllObjectFields(source);
		for (Field field : fields) {
			String fieldName = field.getName();
			Object fieldValue = getFieldValue(source, fieldName);
			setFieldValue(target, fieldName, fieldValue);
		}
		return target;
	}

	/**
	 * 复制属性(不包含fieldValue为null的属性)
	 * 
	 * @param source
	 * @param target
	 * @return
	 * @throws Exception
	 */
	public static Object copyPropertiesNotNull(Object source, Object target)
			throws Exception {
		if (null == source || target == null)
			return null;

		List<Field> fields = getAllObjectFields(source);
		for (Field field : fields) {
			String fieldName = field.getName();
			try{
				Object fieldValue = BeanUtil.get(source, fieldName);
				if (fieldValue != null) {
					setFieldValue(target, fieldName, fieldValue);
				}
			}catch(NoSuchMethodException e){
				continue;
			}
		}
		return target;
	}

	/**
	 * 直接设置对象属性值, 无视private/protected修饰符, 不经过setter函数.
	 */
	public static void setFieldValue(final Object object,
			final String fieldName, final Object value) throws Exception {
		Field field = getDeclaredField(object, fieldName);

		if (null != field) {
			makeAccessible(field);
			try {
				// 非final属性则修改该属性的值
				if (!Modifier.isFinal(field.getModifiers()))
					field.set(object, value);
			} catch (IllegalAccessException e) {
				// throw e;
			} catch (IllegalArgumentException e2) {
			}
		}
	}

	/**
	 * 直接读取对象属性值, 无视private/protected修饰符, 不经过getter函数.
	 * 
	 * @throws NoSuchFieldException
	 */
	public static Object getFieldValue(final Object object,
			final String fieldName) throws IllegalAccessException,
			NoSuchFieldException {
		Field field = getDeclaredField(object, fieldName);

		if (field == null) {
			throw new IllegalArgumentException("Could not find field ["
					+ fieldName + "] on target [" + object + "]");
		}

		makeAccessible(field);

		Object value = null;
		try {
			value = field.get(object);
		} catch (IllegalAccessException e) {
			throw e;
		}
		return value;
	}

	/**
	 * 循环向上转型, 获取对象的DeclaredField.
	 * 
	 * 如向上转型到Object仍无法找到, 返回null.
	 */
	public static Field getDeclaredField(final Object object,
			final String fieldName) throws NoSuchFieldException {
		Assert.hasText(fieldName, "fieldName");
		
//		for (Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass
//				.getSuperclass()) {
//			try {
//				return superClass.getDeclaredField(fieldName);
//			} catch (NoSuchFieldException e) {// NOSONAR
//				// throw e;
//			}
//		}
		Class<?> clazz = object.getClass();
		Field field = null;
		while(!clazz.equals(Object.class))
		{	
			try
			{
				field = clazz.getDeclaredField(fieldName);
				if(field != null)
					break;
			}
			catch(NoSuchFieldException e)
			{
				clazz = clazz.getSuperclass();
			}
		}
		return field;
	}

	/**
	 * 强行设置Field可访问.
	 */
	protected static void makeAccessible(final Field field) {
		if (!Modifier.isPublic(field.getModifiers())
				|| !Modifier.isPublic(field.getDeclaringClass().getModifiers())) {
			field.setAccessible(true);
		}
	}

	public static void convertObjToMap(Object obj, Map map) throws Exception {
		if (obj != null) {
			List<Field> fields = getAllObjectFields(obj);
			for (Field field:fields) {
				field.setAccessible(true);
				Object o = field.get(obj);
				if(o!=null){
					map.put(field.getName(), o);
				}
				field.setAccessible(false);
			}
		}
	}
	
	public static Map convertObjToMap(Object obj) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		if (obj != null) {
			List<Field> fields = getAllObjectFields(obj);
			for (Field field:fields) {
				field.setAccessible(true);
				Object o = field.get(obj);
				map.put(field.getName(), o);
				field.setAccessible(false);
			}
		}
		return map;
	}
	
	public static Object get(Object obj,String param)throws Exception{
		return obj.getClass().getMethod("get"+StringUtil.toUpperCaseFirstOne(param)).invoke(obj);
	}
	/**
	 * 判断方法是否存在
	 * @param obj
	 * @param param
	 * @return
	 */
	public static boolean isMethodExist(Object obj,String param){
		boolean exist=true;
		try {
		if(!param.contains(".")){
			get(obj,param);
		}else{
			Object result=obj;
			for(String subParam:param.split("\\.")){
				result=get(result,subParam);
			}
		}
		}catch (NoSuchMethodException e) {
			exist= false;
			System.out.println("--------不存在"+"get"+StringUtil.toUpperCaseFirstOne(param));
			return exist;
		} catch (Exception e) {
			e.printStackTrace();
		}
			return exist;
	}
	public static Object getEx(Object obj,String param)throws Exception{
		if(!param.contains(".")){
			return get(obj,param);
		}else{
			Object result=obj;
			for(String subParam:param.split("\\.")){
				result=get(result,subParam);
			}
			return result;
		}
	}
	/**
	 *获取所有的对象的属性包括继承的属性
	 * @param object
	 * @return
	 */
	public static List<Field> getAllObjectFields(Object object)
	{
		List<Field> fields = new ArrayList<>();
		
		Class<?> clazz = object.getClass();
		fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
		
		clazz = clazz.getSuperclass();
		
		while(!clazz.equals(Object.class))
		{
			fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
			clazz = clazz.getSuperclass();
		}
		return fields;
	}
	
	public static void set(Object obj,String paramName,Object... params)throws Exception{
		Class[] paramsTypes = new Class[params.length];
		for(int i=0;i<params.length;i++){
			paramsTypes[i]=params[i].getClass();
		}
		Method method= obj.getClass().getMethod("set"+StringUtil.toUpperCaseFirstOne(paramName),paramsTypes);
		method.invoke(obj, params);
	}
	
	public static void setChildren(Object obj,String paramName,List params)throws Exception{
		Method method= obj.getClass().getMethod("set"+StringUtil.toUpperCaseFirstOne(paramName),List.class);
		method.invoke(obj, params);
	}
}
