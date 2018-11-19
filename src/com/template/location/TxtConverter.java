package com.template.location;

import java.io.File;
import java.util.ArrayList; 
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.template.util.DateTimeUtils;
import com.template.util.JacksonUtil;

public class TxtConverter {
	public static void main(String[] args) throws Exception {
		String fileStr=FileUtils.readFileToString(new File("d://15153938.txt"));
		fileStr=fileStr.replace("   ", " ");
		String[] contents=fileStr.split("\r");
		List<Point> list = new ArrayList<Point>();
		Point lastPoint=null;
		for(int i=0;i<contents.length;i++){
			String content = contents[i];
			if(content==null||content.trim().equals("")){
				continue;
			}
			String[] datas = content.split(" ");
			Point point=new Point(Double.parseDouble(datas[1].trim()),
					Double.parseDouble(datas[3].trim()),
					DateTimeUtils.getDateObjectFromString("2016-04-15 "+datas[0].trim(),"yyyy-MM-dd HH:mm:ss").getTime()/1000);
			if(!point.equals(lastPoint)){
				list.add(point);
				lastPoint=point;
			}
		}
		for(Point point : list){
			point.build("1");
		}
		System.out.println(list.size());
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("myOptions", list);
		String resultStr= JacksonUtil.get().readAsString(result);
		File file = new File("d://tsconfig-mobile.json");
		if(!file.exists()){
			file.createNewFile();
		}
		FileUtils.writeStringToFile(file, resultStr);
	}
}
