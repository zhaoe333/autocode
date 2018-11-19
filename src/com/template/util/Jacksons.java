package com.template.util;

import java.io.IOException;
import java.util.List;
import java.util.Map;


import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TreeTraversingParser;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

public class Jacksons {
	
	private ObjectMapper objectMapper;  
	  
    public Jacksons(ObjectMapper objectMapper) {  
        this.objectMapper = objectMapper;  
    }  
    /**
     * 过滤字段
     * @param filterName 过滤器名称（key） 用来标示过滤器
     * @param properties 需要过滤的字段
     * @return
     */
    public Jacksons filter(String filterName, String... properties) {  
        FilterProvider filterProvider = new SimpleFilterProvider().addFilter(filterName,  
                SimpleBeanPropertyFilter.serializeAllExcept(properties));  
        objectMapper.setFilterProvider(filterProvider);  
        return this;  
    }  
  
   /**
    * json字符串转对象
    * @param json
    * @param clazz
    * @return
    */
    public <T> T json2Obj(String json, Class<T> clazz) {  
        try {  
            return objectMapper.readValue(json, clazz);  
        } catch (Exception e) {  
            e.printStackTrace();  
            throw new RuntimeException("解析json错误");  
        }  
    }  
    /**
     * jsonnode转对象
     * @param node
     * @param clazz
     * @return
     */
    public <T> T node2Obj(JsonNode node, Class<T> clazz) {  
        try {  
            return objectMapper.readValue(new TreeTraversingParser(node), clazz);  
        } catch (Exception e) {  
            e.printStackTrace();  
            throw new RuntimeException("解析json错误");  
        }  
    } 
    /**
     * json字符串转jsonnode
     * @param json
     * @return
     */
    public JsonNode json2Node(String json){
    	 try {  
             return objectMapper.readTree(json);  
         } catch (Exception e) {  
             e.printStackTrace();  
             throw new RuntimeException("解析json错误");  
         } 
    }
    /**
     * jsonnode 转 对象
     * @param node
     * @param typeReference
     * @return
     */
    public <T> T node2Obj(JsonNode node,TypeReference<T> typeReference) {  
        try {  
            return objectMapper.readValue(new TreeTraversingParser(node), typeReference);  
        } catch (Exception e) {  
            e.printStackTrace();  
            throw new RuntimeException("解析json错误");  
        }  
    } 
    /**
     * json字符串转对象
     * @param json
     * @param typeReference
     * @return
     */
    public <T> T json2Obj(String json, TypeReference<T> typeReference) {  
        try {  
            return objectMapper.readValue(json, typeReference);  
        } catch (Exception e) {  
            e.printStackTrace();  
            throw new RuntimeException("解析json错误");  
        }  
    } 
    /**
     * 对象转json
     * @param obj
     * @return
     */
    public String readAsString(Object obj) {  
        try {  
            return objectMapper.writeValueAsString(obj);  
        } catch (Exception e) {  
            e.printStackTrace();  
            throw new RuntimeException("解析对象错误");  
        }  
    }  
    /**
     * json to list
     * @param json
     * @return
     */
    @SuppressWarnings("unchecked")  
    public List<Map<String, Object>> json2List(String json) {  
        try {  
            return objectMapper.readValue(json, List.class);  
        } catch (Exception e) {  
            e.printStackTrace();  
            throw new RuntimeException("解析json错误");  
        }  
    }  
    
    public ObjectNode createNode(){
    	return objectMapper.createObjectNode();
    }
    
//    public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
//    	ObjectMapper objectMapper = new ObjectMapper();  
//        // 设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性  
//        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);  
//        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false);  
//        TSPRequest request=objectMapper.readValue("{\"id\":123,\"body\":[123,123]}", TSPRequest.class);
//        System.out.println(request.getBody());
//        JsonNode node = Jacksons.me().json2Node("{\"id\":123,\"body\":[123,123]}");
//        System.out.println(Jacksons.me().node2Obj(node, TSPRequest.class).getBody());
//	}
}
