package com.template.c;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.io.FileUtils;

import com.template.excel.ExcelConfigUtils;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class AutoC {
	
	private static Vector<Vector> vectors;
	
	public static void main(String[] args) throws IOException {
		ExcelConfigUtils config = new ExcelConfigUtils("D:\\国标数据对照表.xls");
		vectors= config.getDataBySN("Sheet1",1, false);
		int position=0;
		Map<String,Object> tempMap = new HashMap<String,Object>();
		w:while(position<vectors.size()){
			if(vectors.get(position).size()==0){
				System.out.println("解析完成  行"+(position+2));
				break w;
			}
			s:switch(vectors.get(position).get(0).toString()){
				case "车辆登入":
					position++;
					doCarLogin(position);
					while(checkHead(vectors.get(position))){
						position++;
					}
					break s;
				case "实时数据(整车数据)":
					System.out.println("do 实时数据(整车数据)");
					position++;
					while(checkHead(vectors.get(position))){
						position++;
					}
					break s;
				case "实时数据(驱动电机数据)":
					System.out.println("do 实时数据(驱动电机数据)");
					position++;
					while(checkHead(vectors.get(position))){
						position++;
					}
					break s;
				case "实时数据(燃料电池数据)--针对燃料电池车(纯电动车不需要填写)":
					System.out.println("do 实时数据(燃料电池数据)--针对燃料电池车(纯电动车不需要填写)");
					position++;
					while(checkHead(vectors.get(position))){
						position++;
					}
					break s;
				case "实时数据(发动机数据)--针对燃油车(纯电动车不需要填写)":
					System.out.println("do 实时数据(发动机数据)--针对燃油车(纯电动车不需要填写)");
					position++;
					while(checkHead(vectors.get(position))){
						position++;
					}
					break s;
				case "实时数据(车辆位置数据)-":
					System.out.println("do 实时数据(车辆位置数据)-");
					position++;
					while(checkHead(vectors.get(position))){
						position++;
					}
					break s;
				case "实时数据(极值数据)":
					System.out.println("do 实时数据(极值数据)");
					position++;
					while(checkHead(vectors.get(position))){
						position++;
					}
					break s;
				case "实时数据(报警数据)":
					System.out.println("do 实时数据(报警数据)");
					position++;
					while(checkHead(vectors.get(position))){
						position++;
					}
					break s;
				case "实时数据(可充电储能装置电压数据)":
					System.out.println("do 实时数据(可充电储能装置电压数据)");
					position++;
					while(checkHead(vectors.get(position))){
						position++;
					}
					break s;
				case "实时数据(可充电储能装置温度数据)":
					System.out.println("do 实时数据(可充电储能装置温度数据)");
					position++;
					while(checkHead(vectors.get(position))){
						position++;
					}
					break s;
				default:
					System.out.println("无法识别  行"+(position+2));
					break w;
			}
		}
	}
	
	private static void doCarLogin(int position) {
		System.out.println("do 车辆登入");
		switch(vectors.get(position).get(1).toString()){
			case "可充电储能子系统数":
				
				position++;
				break;
			case "可充电储能系统编码长度":
				position++;
				break;
			case "可充电储能系统编码":
				position++;
				break;
		}
	}

	private static boolean checkHead(Vector vector){
		if(vector.size()==0){
			return false;
		}
		String head=vector.get(0).toString().trim();
		if(head==null||head.equals("")||head.equals("驱动电机总成信息列表")||head.equals("通用报警标志")
				||head.equals("可充电储能子系统电压信息列表")||head.equals("单体电池电压列表")||head.equals("可充电储能子系统温度信息列表")
				||head.equals("温度值列表")){
			return true;
		}else{
			return false;
		}
	}
}
