package com.template.excel;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class AutoCVSUtil {
	public static String vinPre = "VINDEFGHIJK";
	public static String tnoPre = "ABCDEFGHIJK";
	public static String tsnPre = "TSNDEFGHIJK";
	public static String simPre = "SIMDEFGHIJK";
	public static BigDecimal lon = BigDecimal.valueOf(116.510737);
	public static BigDecimal lat = BigDecimal.valueOf(39.896639);
	public static BigDecimal step= BigDecimal.valueOf(0.0001);
	public static int num = 10000;
	public static int start = 40000; 
	
	public static void main(String[] args) throws Exception{
		
		List<String> params = new ArrayList<String>();
		
//		File file = new File("C:\\Users\\yunlu\\Desktop\\params"+start+"-"+num+".cvs");
		ExcelTool excel = new ExcelTool();
		excel.createExcel("C:\\Users\\yunlu\\Desktop\\test"+start+"-"+num+".xls");
		excel.setCurSheet(0, "test");
		excel.setCellText(0,0,"终端号");
		excel.setCellText(0,1,"SIM卡号");
		excel.setCellText(0,2,"SN号");
		excel.setCellText(0,3,"VIN");
		excel.setCellText(0,4,"品牌");
		for(int i=0;i<num;i++){
			String suf=getSuf(start+i);
			excel.setCellText(i+1, 0, tnoPre+suf);
			excel.setCellText(i+1, 1, simPre+suf);
			excel.setCellText(i+1, 2, tsnPre+suf);
			excel.setCellText(i+1, 3, vinPre+suf);
			excel.setCellText(i+1, 4, "jmeter");
			params.add(tnoPre+suf+","+getLon(i)+","+getLat(i));
		}
		excel.saveExcel();
//		FileUtils.writeLines(file, params);
	}
	
	private static Double getLon(int i){
		return lon.add(step.multiply(new BigDecimal(start+i))).doubleValue();
	}
	private static Double getLat(int i){
		return lat.add(step.multiply(new BigDecimal(start+i))).doubleValue();
	}
	private static String getSuf(int i) {
		String result=""+i;
		if(result.length()>6){
			throw new RuntimeException("有错误啦！");
		}
		while(result.length()<6){
			result="0"+result;
		}
		return result;
	}
}
