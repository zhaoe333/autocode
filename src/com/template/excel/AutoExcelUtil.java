package com.template.excel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class AutoExcelUtil {
	public static void main(String[] args) throws Exception {
		ExcelTool excel = new ExcelTool();
		excel.createExcel("d://gb.xls");
//		excel.createExcel("d://sohu.xls");
		File dir = new File("d://auto");
		if(dir.isDirectory()){
			File[] files = dir.listFiles();
			if(files!=null){
				for(int i=0;i<files.length;i++){
					writeToExcel(excel,files[i],i);
				}
			}
		}else{
			System.out.println("waiting to completion");
		}
//		excel.closeFos();
		excel.saveExcel();
		System.out.println("完成了！");
	}

	private static void writeToExcel(ExcelTool excel, File file, int i) throws Exception{
		excel.setCurSheet(i, file.getName().split("\\.")[0]);
//		System.out.println(file.getName().split("\\.")[0]);
		FileInputStream fs = new FileInputStream(file);
        InputStreamReader isr = new InputStreamReader(fs, "utf-8");
        BufferedReader br = new BufferedReader(isr);
        //用来标识是否读到了field位置
        int flag = 0;
        //用来标识是第几个field
        int index = 0;
        try {
            String data = "";
            while ((data = br.readLine()) != null) {
            	
            	if(!data.trim().equals("")){
            		if(data.trim().startsWith("public class")){
            			flag=1;
            		}
            		if(flag==1){
            			String str = data.trim();
            			//处理注释
            			if(str.startsWith("/**")){
            				str=str.replace("*", "").replace("/", "");
//            				System.out.print(str+"\t");
            				//写入描述
            				excel.setCellText(index, 4, str);
            				//写入属性名中文
            				String nameCN=getNameCN(str);
            				excel.setCellText(index, 0, nameCN);
            			}
            			//处理名称和类型
            			if(str.startsWith("private")){
            				String[] strs = str.split(" ");
//            				System.out.print(strs[1]+"\t");
//            				System.out.print(strs[2].replace(";", "")+"\t");
            				//写入类型
            				excel.setCellText(index, 2, getType(strs[1]));
            				//写入属性名
            				excel.setCellText(index, 1, strs[2].split("=")[0].replace(";", ""));
//            				System.out.println(index);
            				index++;
            			}
            			//处理子类
            			if(str.startsWith("class")){
            				index++;
            			}
            		}
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
	}

	private static String getNameCN(String str) {
		str=str.replace("，", ",");
		return str.split(",")[0].split(" ")[0];
	}

	private static String getType(String string) {
		String type="";
		switch (string.trim()) {
		case "Integer":
			type="int";
			break;
		case "java.lang.Integer":
			type="int";
			break;
		case "String":
			type="string";
			break;
		case "java.lang.String":
			type="string";
			break;
		case "List<String>":
			type="array";	
			break;
		case "Double":
			type="double";	
			break;
		case "java.lang.Double":
			type="double";	
			break;
		case "Float":
			type="double";	
			break;
		case "java.lang.Float":
			type="double";	
			break;
		case "Long":
			type="long";	
			break;
		case "java.long.Long":
			type="long";	
			break;
		case "Date":
			type="string";	
			break;
		case "java.util.Date":
			type="string";	
			break;
		case "int":
			type="int";	
			break;
		case "double":
			type="double";	
			break;
		case "long":
			type="int";	
			break;
		default:
			type="object";
			break;
		}
		return type;
	}
	
	
}
