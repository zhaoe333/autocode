package com.template.ann;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;


public class AddAnn {
	
	private static char x='a';
	
	public static void main(String[] args) throws Exception {
		List<String> paths=new ArrayList<String>();
		paths.add("D:\\tspworkspace\\gb-tbox\\src\\main\\java\\com\\cm\\common\\convert\\realtime");
		paths.add("D:\\tspworkspace\\gb-tbox\\src\\main\\java\\com\\cm\\common\\convert\\warn");
		
//		String history="/Users/zyl_home/Documents/workspace/gb-tbox/src/main/java/com/cm/mongo/entity/CarInfoHistoryMongo";
		add(paths);
		String history="D:\\tspworkspace\\gb-tbox\\src\\main\\java\\com\\cm\\mongo\\entity\\CarInfoHistoryMongo.java";
		String local="D:\\tspworkspace\\gb-tbox\\src\\main\\java\\com\\cm\\mongo\\entity\\CarInfoMongo.java";
//		addAnn2(new File(history));
//		addAnn2(new File(local));
//		paths.add(history);
//		paths.add(local);
		remove(paths);
	}
	
	private static void add(List<String> paths) throws Exception{
		for(String path:paths){
			doAddAnn(path);
		}
	}
	
	private static void remove(List<String> paths) throws Exception{
		for(String path:paths){
			doRemoveAnn(path);
		}
	}
	
	private static void doAddAnn(String path)throws Exception {
		File dir = new File(path);
		if(dir.isDirectory()){
			File[] listFiles = dir.listFiles();
			for(File file:listFiles){
				addAnn(file);
				x+=1;
			}
		}else{
			addAnn(dir);
			x+=1;
		}
	}
	
	private static void doRemoveAnn(String path)throws Exception {
		File dir = new File(path);
		if(dir.isDirectory()){
			File[] listFiles = dir.listFiles();
			for(File file:listFiles){
				removeAnn(file);
			}
		}else{
			removeAnn(dir);
		}
		
	}
	
	private static void addAnn(File file) throws Exception{
		char y='a'-1;
		List<String> readLines = FileUtils.readLines(file);
		List<String> newList = new ArrayList<String>();
		boolean importFlag=true;
		for(String line:readLines){
			if(importFlag&&line.startsWith("import")){
				newList.add("import org.mongodb.morphia.annotations.Property;");
				newList.add("import org.mongodb.morphia.annotations.Embedded;");
				newList.add("import org.mongodb.morphia.annotations.NotSaved;");
				importFlag=false;
			}
			if(line.contains("private")
					&&line.contains(";")){
				y=getNext(y);
				if(line.contains("Packet")){
					newList.add("	@Embedded(\""+y+"\")");
				}else if(line.contains(" length")||line.contains("flagBytes")){
					newList.add("	@NotSaved");
				}else{
					newList.add("	@Property(\""+y+"\")");
				}
				
			}
			newList.add(line);
		}
		FileUtils.writeLines(file, newList);
	}
	
	private static void addAnn2(File file) throws Exception{
		char y='a'-1;
		List<String> readLines = FileUtils.readLines(file);
		List<String> newList = new ArrayList<String>();
		boolean importFlag=true;
		for(String line:readLines){
			if(importFlag&&line.startsWith("import")){
				newList.add("import org.mongodb.morphia.annotations.Property;");
				newList.add("import org.mongodb.morphia.annotations.Embedded;");
				importFlag=false;
			}
			if(line.contains("private")
					&&line.contains(";")
					&&line.contains("Packet")){
				y=getNext(y);
				newList.add("	@Embedded(\""+y+"\")");
			}
			newList.add(line);
		}
		FileUtils.writeLines(file, newList);
	}
	
	private static char getNext(char a){
		if(a=='z'){
			return 'A';
		}
		return (char) (a+1);
	}
	
	private static void removeAnn(File file) throws Exception{
		List<String> readLines = FileUtils.readLines(file);
		List<String> newList = new ArrayList<String>();
		for(String line:readLines){
			if(line.contains("@Embedded")
					||line.contains("@Property")
					||line.contains("import org.mongodb.morphia.annotations.Property")
					||line.contains("import org.mongodb.morphia.annotations.Embedded")){
				continue;
			}
			newList.add(line);
		}
		FileUtils.writeLines(file, newList);
	}
}
