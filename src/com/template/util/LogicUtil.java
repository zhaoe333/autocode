package com.template.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.ArrayUtils;




/**
 * 
 * @ClassName: LogicUtil
 * @Description: 简单逻辑判断
 * @author: zyl
 * @date 2013-3-7 下午05:30:13
 * 
 */
public class LogicUtil {
	/**
	 * 简单校验常用类型参数，符合以下条件则返回true 
	 * String 非null 非空
	 * int等基本数据类型 暂不验证
	 * Collection 长度大于0
	 * map 暂不验证
	 * 其他 非null
	 * @param objs
	 * @return
	 */
	public static boolean checkParamsNullAndEmpty(Object... objs){
		for(Object obj:objs){
			if(obj instanceof String){
				String str = (String)obj;
				if(isNullOrEmpty(str)){
					return false;
				}
			}else if(obj instanceof Collection){
				Collection str = (Collection)obj;
				if(isNullOrEmpty(str)){
					return false;
				}
			}else if(obj instanceof Map){
				//TODO
				Map str = (Map)obj;
				if(isNullOrEmpty(str)){
					return false;
				}
			}else{
				if(obj==null){
					return false;
				}
			}
		}
		return true;
	}
	
	public static boolean checkParamsNull(Object... objs){
		for(Object obj:objs){
			if(obj==null){
				return false;
			}
		}
		return true;
	}
	
	
    @SuppressWarnings("unchecked")
    public static boolean isNullOrEmpty(Collection collection) {
        if (null == collection || 0 == collection.size()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isNotNullAndEmpty(Collection collection) {
        return (!isNullOrEmpty(collection));
    }
    
    @SuppressWarnings("unchecked")
    public static boolean isNullOrEmpty(String... params) {
        if (null == params || 0 == params.length) {
            return true;
        } else {
        	for(String param:params){
        		if(isNullOrEmpty(param)){
        			return true;
        		}
        	}
            return false;
        }
    }
    
    @SuppressWarnings("unchecked")
    public static boolean isNullOrEmpty(Map map) {
        if (null == map || 0 == map.size()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isNotNullAndEmpty(Map map) {
        return (!isNullOrEmpty(map));
    }

    public static boolean isNull(Object object) {
        if (object == null) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isNotNull(Object Object) {
        return (!isNull(Object));
    }

    public static boolean isNullOrEmpty(String subject) {
        if (null == subject || "".equals(subject.trim())) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isNotNullAndEmpty(String subject) {
        return (!isNullOrEmpty(subject));
    }
    
    /**
     * 校验当前obj的属性（如果properties不填则检查所有属性）
     * 如果全部不为null返回true
     * 否则返回false
     * @param obj
     * @param poerperties
     * @return
     * @throws Exception 
     */
    public static boolean checkBeanNull(Object obj,String... properties) throws Exception{
    	if(obj==null){
    		return false;
    	}
    	if(properties.length>0){
    		for(String property:properties){
        		if(BeanUtil.get(obj, property)==null){
        			return false;
        		}
        	}
    	}else{
    		for(Method method:obj.getClass().getMethods()){
    			if(method.getName().startsWith("get")){
    				if(method.invoke(obj)==null){
    					return false;
    				}
    			}
    		}
    	}
    	return true;
    }
    /**
     * 校验当前obj的属性（如果properties不填则检查所有属性）
     * 如果全部不为null且不为空字符串返回true
     * 否则返回false 
     * @param obj
     * @param properties
     * @return
     * @throws Exception
     */
    public static boolean checkBeanNullAndEmpty(Object obj,String... properties) throws Exception{
    	if(obj==null){
    		return false;
    	}
    	List list = new ArrayList();
    	if(properties.length>0){
        	for(String property:properties){
        		list.add(BeanUtil.get(obj, property));
        	}
    	}else{
    		for(Method method:obj.getClass().getMethods()){
    			if(method.getName().startsWith("get")){
    				list.add(method.invoke(obj));
    			}
    		}
    	}
    	return checkParamsNullAndEmpty(list.toArray());
    }
    
    /**
     * 校验当前obj的属性（如果properties不填则检查所有属性）
     * properties为不需要检查的属性
     * 如果全部不为null返回true
     * 否则返回false
     * @param obj
     * @param poerperties
     * @return
     * @throws Exception 
     */
    public static boolean checkBeanNullEx(Object obj,String... properties) throws Exception{
    	if(obj==null){
    		return false;
    	}
    	if(properties.length>0){
    		List<Field> fields = BeanUtil.getAllObjectFields(obj);
    		for(Field field : fields){
    			if(!ArrayUtils.contains(properties, field.getName())&&
    					BeanUtil.get(obj, field.getName())==null){
    				return false;
    			}
    		}
    	}else{
    		for(Method method:obj.getClass().getMethods()){
    			if(method.getName().startsWith("get")){
    				if(method.invoke(obj)==null){
    					return false;
    				}
    			}
    		}
    	}
    	return true;
    }
    /**
     * 校验当前obj的属性（如果properties不填则检查所有属性）
     * properties为不需要检查的属性
     * 如果全部不为null且不为空字符串返回true
     * 否则返回false 
     * @param obj
     * @param properties
     * @return
     * @throws Exception
     */
    public static boolean checkBeanNullAndEmptyEx(Object obj,String... properties) throws Exception{
    	if(obj==null){
    		return false;
    	}
    	List list = new ArrayList();
    	if(properties.length>0){
        	List<Field> fields = BeanUtil.getAllObjectFields(obj);
    		for(Field field : fields){
    			if(!ArrayUtils.contains(properties, field.getName())){
    				list.add(BeanUtil.get(obj, field.getName()));
    			}
    		}
    	}else{
    		for(Method method:obj.getClass().getMethods()){
    			if(method.getName().startsWith("get")){
    				list.add(method.invoke(obj));
    			}
    		}
    	}
    	return checkParamsNullAndEmpty(list.toArray());
    }
}
