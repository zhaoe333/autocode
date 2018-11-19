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
import com.template.util.JacksonUtil;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

public class AutoCANUtil {
	
	public static void main(String[] args) throws Exception {
		try{
			ExcelConfigUtils config = new ExcelConfigUtils("D:\\12345.xls");
			Vector<Vector> vectors= config.getDataBySN("Matrix", 1, false);
			Map<String,List<CANData>> map = new HashMap<String,List<CANData>>();
			for(Vector v:vectors){
				if(v.size()>0){
					add(map,v);
				}
			}
			//生成代码
			create(map,"code.txt");
			createExcel(map,"vehicle.xls");
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	private static void createExcel(Map<String, List<CANData>> map, String fileName) throws Exception{
		ExcelSheet sheet = new ExcelSheet();
		sheet.setSheetName("can上报数据");
		List<String> header = new ArrayList<String>();
		header.add("canId");
		header.add("key");
		header.add("描述");
		header.add("描述2");
		header.add("类型");
		header.add("最大值");
		header.add("最小值");
		header.add("单位");
		
		sheet.setHeaderList(header);
		
		List<List> dataList = new ArrayList<List>();
		sheet.setDataList(dataList);
		for(String key:map.keySet()){
			for(CANData data:map.get(key)){
				List<Object> list = new ArrayList<Object>();
				list.add(data.getCanName());
				list.add(data.getName());
				list.add(data.getDes());
				list.add(data.getDes2().replace("0x", "  "));
				list.add(data.getDer()==1?"int":"double");
				list.add(data.getMax());
				list.add(data.getMin());
				list.add(data.getUnit());
				dataList.add(list);
			}
		}
		List<ExcelSheet> sheets = new ArrayList<ExcelSheet>();
		sheets.add(sheet);
		ListToExcel.exportExcelForSheet(sheets, new File("d://can//"+fileName));
	}

	private static void create(Map<String,List<CANData>> map,String fileName)throws Exception{
		Configuration config = new Configuration();
		config.setDirectoryForTemplateLoading(new File("can"));
		config.setObjectWrapper(new DefaultObjectWrapper());
		//生成文件
		Template template = config.getTemplate("code.ftl", Constant.fileEncoding);
		File file = new File("D:/can/" + fileName);
		if(!file.getParentFile().exists())
			file.getParentFile().mkdirs();
		file.createNewFile();
		Writer out = new BufferedWriter(new FileWriter(file));
		Map<String,Object> tempMap = new HashMap<String,Object>();
		tempMap.put("datas", map);
		template.process(tempMap, out);
		out.flush();
		out.close();
	}
	
	private static void add(Map<String, List<CANData>> map, Vector v) {
		CANData data=getCANData(v);
		List<CANData> list = map.get(data.getCanId());
		if(list==null){
			list = new ArrayList<CANData>();
		}
		list.add(data);
		map.put(data.getCanId(), list);
	}

	private static CANData getCANData(Vector v) {
		CANData data = new CANData();
		data.setCanId(v.get(2).toString());
		data.setCanName(v.get(0).toString());
		data.setDes(v.get(7).toString());
		data.setDes2(v.get(24).toString());
		data.setName(v.get(6).toString());
		data.setDer((int)(1.0/(double)v.get(14)));
		data.setDiff(-((Double)v.get(15)).intValue());
		data.setLength(((Double)v.get(12)).intValue());
		data.setMax(Double.parseDouble(v.get(17).toString()));
		data.setMin((double)v.get(16));
		data.setUnit(v.get(23).toString());
		data.setStart(((Double)v.get(9)).intValue());
		data.setStartBit(((Double)v.get(10)).intValue());
		return data;
	}
}
