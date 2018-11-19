package com.template.excel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.template.Constant;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;


public class AutoBeanUtil {
	public static void main(String[] args) throws Exception {
		JavaBean bean = new JavaBean();
		bean.setName("ReservationOrder");
		bean.setPath("com.cm.tuan.dianping.entity");
		List<JavaBeanProperty> pros =  new ArrayList<JavaBeanProperty>();
		ExcelConfigUtils config = new ExcelConfigUtils("D:\\test.xls");
		Vector<Vector> vectors= config.getDataBySN("sheet1", 1, false);
		for(Vector v : vectors){
			if(v.size()>0){
				pros.add(convert(v));
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
	
	private static JavaBeanProperty convert(Vector v)throws Exception{
		JavaBeanProperty pro = new JavaBeanProperty();
		String name=v.get(0).toString();
		
		if(name.contains("_")){
			pro.setAnn("@JsonProperty(\""+name+"\")");
			name=StringUtil.toHump(name, "_");
		}		
		String type="";
		switch (v.get(1).toString()) {
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
		default:
			break;
		}
		pro.setName(name);
		pro.setComment(v.get(2).toString());
		pro.setType(type);
		return pro;
	}
}
