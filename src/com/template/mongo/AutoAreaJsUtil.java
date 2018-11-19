package com.template.mongo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.template.CodeAutoGenerateOld;
import com.template.Constant;
import com.template.JDBCUtils;
import com.template.TemplateTable;
import com.template.util.JacksonUtil;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;


public class AutoAreaJsUtil {
	
	public static void main(String[] args) throws Exception {
		Map<String,DataCity> citymap = getCity();
		List<String> cityjs=new ArrayList<String>();
		File dir = new File("d://polyline");
		for(File file:dir.listFiles()){
			Map<String,Object> 	map = getMap();
			String code = file.getName().split("\\.")[0];
			map.put("citycode", code);
			DataCity city = citymap.get(code);
			map.put("cityname", city.getName());
			map.put("citylon", city.getLon());
			map.put("citylat", city.getLat());
			map.put("parentCode", city.getParentCode());
			System.out.println(code+":"+citymap.get(code));
			List<List<double[]>> polylines = readPolygons(file);
			List polygon= new LinkedList<>();
			if(polylines.size()>1){
				map.put("type","MultiPolygon");
				for (List<double[]> polyline : polylines) {  
		            List<List<double[]>> temp = new LinkedList<>();  
		            temp.add(polyline);  
		            polygon.add(temp);  
		        } 
			}else if(polylines.size()==1){
				map.put("type","Polygon");
				polygon=polylines;
			}
			map.put("polygon", JacksonUtil.get().readAsString(polygon));
			String filename ="area"+code+".js";
			create(map,filename,"js.ftl");
			cityjs.add(filename);
		}
		Map<String,Object> 	map = new HashMap<String,Object>();
		map.put("citys", cityjs);
//		create(map,"init.js","loadJs.ftl");
	}
	
	private static Map<String,DataCity> getCity()throws Exception{
		Connection conn = null;
		Statement stmt =null;
		ResultSet rs = null;
		Map<String,DataCity> citys = new HashMap<String,DataCity>();
		
		try{
			conn = JDBCUtils.getInstance().getConnection();
			String sql = "select * from cm_data_city";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				DataCity city=new DataCity();
				city.setCode(rs.getString("code"));
				city.setLat(rs.getDouble("lat"));
				city.setLon(rs.getDouble("lon"));
				city.setName(rs.getString("name"));
				city.setParentCode(rs.getString("parent_code"));
				citys.put(city.getCode(), city);
			}
			rs.close();
			stmt.close();
			conn.close();
			/**
			 * 生成文件
			 */
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(rs!=null&&!rs.isClosed()){
				rs.close();
			}
			if(stmt!=null&&!stmt.isClosed()){
				stmt.close();
			}
			if(conn!=null&&!conn.isClosed()){
				conn.close();
			}
		}
		return citys;
	}
	
	private static Map<String,Object> getMap() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("dbname", "monitor");
		map.put("auth", 0);//0为授权
		map.put("username", "monitor");
		map.put("password", "futuremove");
		return map;
	}
	
	private static void create(Map<String,Object> map,String fileName,String templateName)throws Exception{
		Configuration config = new Configuration();
		config.setDirectoryForTemplateLoading(new File("mongo"));
		config.setObjectWrapper(new DefaultObjectWrapper());
		//生成文件
		Template template = config.getTemplate(templateName, Constant.fileEncoding);
		File file = new File("D:/mongojs2/" + fileName);
		if(!file.getParentFile().exists())
			file.getParentFile().mkdirs();
		file.createNewFile();
		Writer out = new BufferedWriter(new FileWriter(file));
		template.process(map, out);
		out.flush();
		out.close();
	}
	
	private static List<List<double[]>> readPolygons(File file) throws Exception {
		String locations=FileUtils.readFileToString(file);
		List<List<double[]>> polygons = new LinkedList<>();
		String[] areas = locations.split("\\|");
		int limit = 100;
        for(String area:areas){
        	List<double[]> polygon = new LinkedList<>();
        	String[] split = area.split(";");
        	if(areas.length>10&&split.length<50){
    			continue;
    		}
        	int x=1;
    		if(limit!=0){
    			x=split.length/limit+1;
    		}
	        for(int i=0;i<split.length;i++){
	        	if(i==0||i==split.length-1||i%x==0){
	        		String[] lonlatStr = split[i].split(",");
		        	polygon.add(new double[]{Double.parseDouble(lonlatStr[0]),Double.parseDouble(lonlatStr[1])});
	        	}
	        }
	        polygons.add(polygon);
        }
        if(areas.length!=polygons.size()){
        	System.out.println(areas.length+"----"+polygons.size());
        }
		return polygons;
	}
}
