package com.template.excel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.template.Constant;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

public class AutoTboxUtil3 {
	
	private static int length=0;
	
	public static void main(String[] args) {
		try{
			ExcelConfigUtils config = new ExcelConfigUtils("D:\\msdata2.xls");
			List sheetNames = config.getSheetNamesOfExcel();
			
			for(Object sheetName:sheetNames){
				String name = sheetName.toString();
				Vector<Vector> vectors= config.getDataBySN(name, 1, false);
				List<CANData> list = new ArrayList<CANData>();
				for(Vector v:vectors){
					if(v.size()>0&&null!=v.get(0)&&!v.get(0).toString().trim().equals("")){
						list.add(convert(v));
					}
				}
				Map<String,List<CANData>> dataIds= new HashMap<String,List<CANData>>();
				for(CANData data:list){
					List<CANData> dataList = dataIds.get(data.getId());
					if(dataList==null){
						dataList = new ArrayList<CANData>();
						dataIds.put(data.getId(), dataList);
					}
					dataList.add(data);
				}
				String[] array = dataIds.keySet().toArray(new String[]{});
				Arrays.sort(array);
				Map<String,Object> tempMap = new HashMap<String,Object>();
				tempMap.put("datas", list);
				tempMap.put("name", name);
				tempMap.put("dataIds", dataIds);
				tempMap.put("length", length+1);
				tempMap.put("ids", array);
				//生成代码
				create(tempMap,name+".java");
			}
//			createExcel(map,"vehicle.xls");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private static void create(Map<String,Object> tempMap,String fileName)throws Exception{
		Configuration config = new Configuration();
		config.setDirectoryForTemplateLoading(new File("tbox"));
		config.setObjectWrapper(new DefaultObjectWrapper());
		//生成文件
		Template template = config.getTemplate("javabean2.ftl", Constant.fileEncoding);
		File file = new File("D:/tbox/" + fileName);
		if(!file.getParentFile().exists())
			file.getParentFile().mkdirs();
		file.createNewFile();
		Writer out = new BufferedWriter(new FileWriter(file));
		template.process(tempMap, out);
		out.flush();
		out.close();
	}
	
	private static CANData convert(Vector v) {
		CANData data = new CANData();
		data.setDes(v.get(1).toString());
		data.setDes2(v.get(2).toString());
		data.setName(v.get(0).toString());
		data.setDer(((Double)v.get(6)).intValue());
		data.setDiff(-((Double)v.get(7)).intValue());
		data.setLength(((Double)v.get(5)).intValue());
		data.setMax((double)v.get(9));
		data.setMin((double)v.get(8));
		data.setUnit(v.get(10).toString());
//		data.setStart(((Double)v.get(3)).intValue());
		data.setId(v.get(3).toString());
		data.setStartBit(((Double)v.get(4)).intValue());
		data.setType(v.get(11).toString());
		if(data.getLength()==8){
			data.setByteName(data.getName()+"Byte");
		}else{
			data.setByteName(data.getName()+"Bytes");
		}
		if(length<data.getStart()){
			length=data.getStart();
		}
		return data;
	}
}
