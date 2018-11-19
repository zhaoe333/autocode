package com.template.location;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.template.util.JacksonUtil;

public class Converter {
	public static void main(String[] args) throws Exception {
		String content=FileUtils.readFileToString(new File("d://tsconfig.json"));
		JsonNode node = JacksonUtil.get().json2Node(content);
		long seconds = new Date().getTime()/1000-24*60*60;
		JsonNode arr=node.get("myOptions");
		List<Point> list = new ArrayList<Point>();
		Point lastPoint=null;
		for(int i=0;i<arr.size();i++){
			Point point=new Point(new double[]{
					Double.parseDouble(arr.get(i).get("lat").textValue().trim()), 
					Double.parseDouble(arr.get(i).get("lon").textValue().trim())},seconds+5L*i);
			
			if(!point.equals(lastPoint)){
				list.add(point);
				lastPoint=point;
			}
		}
		for(Point point : list){
			point.build("1");
		}
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("myOptions", list);
		String resultStr= JacksonUtil.get().readAsString(result);
		File file = new File("d://tsconfig2.json");
		if(!file.exists()){
			file.createNewFile();
		}
		FileUtils.writeStringToFile(file, resultStr);
	}
}	
