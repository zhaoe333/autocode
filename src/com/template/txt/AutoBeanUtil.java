package com.template.txt;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.template.Constant;
import com.template.excel.ExcelConfigUtils;
import com.template.excel.JavaBean;
import com.template.excel.JavaBeanProperty;
import com.template.excel.StringUtil;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;


public class AutoBeanUtil {
	public static void main(String[] args) throws Exception {
		JavaBean bean = new JavaBean();
		bean.setName("HighWay");
		bean.setPath("com.cm.road.entity");
		List<JavaBeanProperty> pros =  new ArrayList<JavaBeanProperty>();
		
		FileInputStream fs = new FileInputStream("D:\\test.txt");
        InputStreamReader isr = new InputStreamReader(fs, "utf-8");
        BufferedReader br = new BufferedReader(isr);
        try {
            String data = "";
            while ((data = br.readLine()) != null) {
            	if(!data.trim().equals("")){
            		pros.add(convert(data));
            	}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
        	if(br!=null){
        		br.close();
        	}
        	if(isr!=null){
        		isr.close();
        	}
        	if(fs!=null){
        		fs.close();
        	}
        }
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("bean", bean);
		map.put("pros", pros);
		create(map,bean.getName()+".java");
	}
	
	private static void create(Map<String,Object> map,String fileName)throws Exception{
		Configuration config = new Configuration();
		config.setDirectoryForTemplateLoading(new File("javabean"));
		config.setObjectWrapper(new DefaultObjectWrapper());
		//生成文件
		Template template = config.getTemplate("entity.ftl", Constant.fileEncoding);
		File file = new File("D:/javabean/" + fileName);
		if(!file.getParentFile().exists())
			file.getParentFile().mkdirs();
		file.createNewFile();
		Writer out = new BufferedWriter(new FileWriter(file));
		template.process(map, out);
		out.flush();
		out.close();
	}
	
	private static JavaBeanProperty convert(String data)throws Exception{
		JavaBeanProperty pro = new JavaBeanProperty();
		String[] datas=data.split("  ");
		String name = datas[0];
		if(name.contains("_")){
			pro.setAnn("@JsonProperty(\""+name+"\")");
			name=StringUtil.toHump(name, "_");
		}		
		String type="";
		if(datas.length>3){
			switch (datas[1]) {
			case "Integer":
				type="Integer";
				break;
			case "String":
				type="String";
				break;
			case "List":
				type="List<String>";	
				break;
			case "Float":
				type="Double";	
				break;
			case "Long":
				type="Long";	
				break;
			case "int":
				type="Integer";
				break;
			case "string":
				type="String";
				break;
			case "list":
				type="List<String>";	
				break;
			case "float":
				type="Double";	
				break;
			case "long":
				type="Long";	
				break;
			default:
				type="String";
				break;
			}
		}else{
			type="String";
		}
		pro.setName(name);
		pro.setComment(datas[2]+(datas.length>3?("  例："+datas[3]):""));
		pro.setType(type);
		return pro;
	}
}
