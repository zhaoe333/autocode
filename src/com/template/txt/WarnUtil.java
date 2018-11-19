package com.template.txt;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.template.JDBCUtils;

public class WarnUtil {
	public static void main(String[] args) throws Exception{
		FileInputStream fs = new FileInputStream("D:\\cccc.txt");
        InputStreamReader isr = new InputStreamReader(fs, "utf-8");
        BufferedReader br = new BufferedReader(isr);
        Connection conn = null;
		PreparedStatement stmt =null;
		ResultSet rs = null;
        try {
            String data = "";
            conn = JDBCUtils.getInstance().getConnection();
			String sql = "insert into cm_data(id,key,value,type) values(?,?,?,1)";
			stmt = conn.prepareStatement(sql);
            while ((data = br.readLine()) != null) {
            	if(!data.trim().equals("")){
            		System.out.println(data);
            		String[] datas = data.split("  ");
            		stmt.setString(1, UUID.randomUUID().toString().replace("-", ""));
    				stmt.setString(2, datas[0]);
    				stmt.setString(3, new String(datas[1].getBytes("GBK"),"UTF-8"));
    				stmt.addBatch();
            	}
            }
            stmt.execute();
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
}

