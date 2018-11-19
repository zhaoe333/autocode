package com.template.location;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.template.Constant;
import com.template.JDBCUtils;
import com.template.util.DateTimeUtils;
import com.template.util.JacksonUtil;

public class JDBCConverter {
	public static void main(String[] args) throws Exception {
		Connection conn = null;
		Statement stmt =null;
		ResultSet rs = null;
		try{
			conn = JDBCUtils.getInstance().getConnection();
			String sql = "select  rr.update_time,lr.lon,lr.lat from cm_realtime_record rr join cm_car_location_record lr on lr.realtime_record_id = rr.id where rr.vin ='CE201510010010016' and rr.update_time between '2016-04-13 08:45:00' and '2016-04-13 09:15:00' order by update_time desc";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			List<Point> list = new ArrayList<Point>();
			Point lastPoint=null;
			int i =0;
			while(rs.next()){
				String time = rs.getString("update_time");
				double lat  = Double.parseDouble(rs.getString("lat"));
				double lon  = Double.parseDouble(rs.getString("lon"));
				long loc_time=DateTimeUtils.getDateObjectFromString(time,"yyyy-MM-dd HH:mm:ss").getTime()/1000;
				Point point = new Point(lon,lat,loc_time);
//				if(list.size()==0){
//					list.add(point);
//				}else if(!list.contains(point)){
//					list.add(point);
//				}
				if(!point.equals(lastPoint)){
					list.add(point);
					lastPoint=point;
				}
				i++;
			}
			System.out.println(list.size());
			System.out.println(i);
			for(Point point : list){
				point.build("2");
			}
			Map<String,Object> result = new HashMap<String,Object>();
			result.put("myOptions", list);
			String resultStr= JacksonUtil.get().readAsString(result);
			File file = new File("d://tsconfig-16.json");
			if(!file.exists()){
				file.createNewFile();
			}
			FileUtils.writeStringToFile(file, resultStr);
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
		
	}
}
