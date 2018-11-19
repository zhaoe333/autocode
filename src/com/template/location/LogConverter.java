package com.template.location;

import java.io.File;

import org.apache.commons.io.FileUtils;


public class LogConverter {
	public static void main(String[] args) throws Exception {
		String fileStr=FileUtils.readFileToString(new File("d://catalina_prefixfo"));
		String[] contents=fileStr.split("\n");
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<contents.length;i++){
			if(contents[i].contains("CE201510010010018")){
				sb.append(contents[i]+"\n");
			}
		}
		File file = new File("d://log-18.json");
		if(!file.exists()){
			file.createNewFile();
		}
		FileUtils.writeStringToFile(file, sb.toString());
	}
}
