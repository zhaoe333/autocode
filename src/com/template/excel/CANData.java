package com.template.excel;

import java.util.Arrays;

import com.template.util.ByteUtils;

/**
 * 恒领can数据封装类
 * @author yunlu
 *
 */
public class CANData {
	
	private String canName;
	
	private String canId;
	
	private String name;
	
	private String des;
	
	private int start;
	
	private int startBit;
	
	private int length;
	
	private int der;
	
	private int diff;
	
	private double max;
	
	private double min;
	
	private String unit;
	
	private String des2;
	
	private String byteName;
	
	private String type;
	
	private String id;

	public String getCanId() {
		return canId;
	}

	public void setCanId(String canId) {
		this.canId = canId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getDer() {
		return der;
	}

	public void setDer(int der) {
		this.der = der;
	}

	public int getDiff() {
		return diff;
	}

	public void setDiff(int diff) {
		this.diff = diff;
	}

	public double getMax() {
		return max;
	}

	public void setMax(double max) {
		this.max = max;
	}

	public double getMin() {
		return min;
	}

	public void setMin(double min) {
		this.min = min;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getDes2() {
		return des2;
	}

	public void setDes2(String des2) {
		this.des2 = des2;
	}

	public String getCanName() {
		return canName;
	}

	public void setCanName(String canName) {
		this.canName = canName;
	}

	public int getStartBit() {
		return startBit;
	}

	public void setStartBit(int startBit) {
		this.startBit = startBit;
	}
	
	public String getByteName() {
		return byteName;
	}

	public void setByteName(String byteName) {
		this.byteName = byteName;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode(){
		int i = length/8;
		int begin=(startBit-start*8)-length+1;
		if(length%8==0){
			if(i>4){
				//过长数据，特殊处理
				return "error";
			}
			if(begin%8==0){
				//不跨字节
				if(der>1){
					//double类型
					return toDouble(i);
				}else if(der==1){
					//int类型
					return handleDiff(toInteger(i));
				}else{
					//未知情况
					return "error";
				}
			}else{
				//跨字节
				return handleCrossByte(i);
			}
		}else{
			String result = "";
			if(i>0||begin<0){
				//跨字节
				result= handleCrossByte(i);
			}else{
				//不跨字节
				String temp=getHex(length);
				if(begin==0){
					result=temp+" & bytes["+start+"] ";
				}else{
					result= temp+" & (bytes["+start+"] "+">>> "+begin+")";
				}
			}
			if(der>1){
				//double类型
				return toDouble(result);
			}else if(der==1){
				//int类型
				return handleDiff(result);
			}else{
				//未知情况
				return "error";
			}
		}
	}
	
	public String getCode2(){
		int i = length/8;
		int begin=startBit-length+1;
		if(length%8==0){
			if("string".equals(type)){
				return "new String(Arrays.copyOfRange(bytes, "+start+","+(start+i)+"))";
			}
			if("date".equals(type)){
				return "ByteUtils.convertByteArrtDatetime(Arrays.copyOfRange(bytes, "+start+","+(start+i)+"))";
			}
			if(i>4){
				//过长数据，特殊处理
				return "error";
			}
			if(begin%8==0){
				//不跨字节
				if(der>1){
					//double类型
					return toDouble(i);
				}else if(der==1){
					//int类型
					return handleDiff(toInteger(i));
				}else{
					//未知情况
					return "error";
				}
			}else{
				//跨字节
				return handleCrossByte(i);
			}
		}else{
			String result = "";
			if(i>0||begin<0){
				//跨字节
				result= handleCrossByte(i);
			}else{
				//不跨字节
				String temp=getHex(length);
				if(begin==0){
					result=temp+" & bytes["+start+"] ";
				}else{
					result= temp+" & (bytes["+start+"] "+">>> "+begin+")";
				}
			}
			if(der>1){
				//double类型
				return toDouble(result);
			}else if(der==1){
				//int类型
				return handleDiff(result);
			}else{
				//未知情况
				return "error";
			}
		}
	}
	public String getCode3(){
		int i = length/8;
		if("string".equals(type)){
			return name+".getBytes()";
		}
		if("date".equals(type)){
			return "ByteUtils.convertDatetime2ByteArr("+name+")";
		}
		if(der==1){
			return IntToByte(i);
		}else{
			return DoubleToByte(i);
		}
	}
	
	public String getCode4(){
		if(length%8==0){
			int i=length/8;
			if(type.contains("double")){
				return toDouble2(i);
			}else if(type.contains("int")){
				return toInteger2(i);
			}
		}else{
			return getHex(length)+" & (bytes[position+i] >>> "+startBit+")";
		}
		return "error";
	}
	public String getJavaType(){
		if("string".equals(type)){
			return "String";
		}else if("date".equals(type)){
			return "Date";
		}else if("byte".equals(type)){
			if(der>1){
				return "Double";
			}else{
				return "Integer";
			}
		}else if("arr.int".equals(type)){
			return "List<Integer>";
		}else if("arr.double".equals(type)){
			return "List<Double>";
		}else{
			return "error";
		}
	}
	public String IntToByte(int length){
		if(length==1){
			return name+".byteValue()";
		}else if(length>1&&length<=4){
			return "ByteUtils.intToByteArray("+name+","+length+")";
		}
		return "error";
	}
	
	public String DoubleToByte(int length){
		if(length==1){
			return "(byte)ByteUtils.DoubleToInt("+name+","+der+","+diff+")";
		}else if(length>1&&length<=4){
			return "ByteUtils.intToByteArray(ByteUtils.DoubleToInt("+name+","+der+","+diff+"),"+length+")";
		}
		return "error";
	}
	
	private String toInteger(int i){
		if(i==1){
			return "0xff & bytes["+start+"]";
		}else if(i==2){
			return "ByteUtils.byteArrayToInt(new byte[]{0x00,0x00,bytes["+start+"],bytes["+(start+1)+"]}, 0)";
		}else if(i==3){
			return "ByteUtils.byteArrayToInt(new byte[]{0x00,bytes["+start+"],bytes["+(start+1)+"],bytes["+(start+2)+"]}, 0)";
		}else if(i==4){
			return "ByteUtils.byteArrayToInt(new byte[]{bytes["+start+"],bytes["+(start+1)+"],bytes["+(start+2)+"],bytes["+(start+3)+"]}, 0)";
		}else{
			return "error";
		}
	}
	
	private String toInteger2(int i){
		if(i==1){
			return "0xff & bytes[position+i*"+i+"]";
		}else if(i==2){
			return "ByteUtils.byteArrayToInt(new byte[]{0x00,0x00,bytes[position+i*"+i+"],bytes[position+1+i*"+i+"]}, 0)";
		}else if(i==3){
			return "ByteUtils.byteArrayToInt(new byte[]{0x00,bytes[position+i*"+i+"],bytes[position+1+i*"+i+"],bytes[position+2+i*"+i+"]}, 0)";
		}else if(i==4){
			return "ByteUtils.byteArrayToInt(new byte[]{bytes[position+i*"+i+"],bytes[position+1+i*"+i+"],bytes[position+2+i*"+i+"],bytes[position+3+i*"+i+"]}, 0)";
		}else{
			return "error";
		}
	}
	
	private String toDouble(String result){
		return "ByteUtils.intToDouble(("+result+"),"+der+","+diff+")";
	}
	
	private String toDouble(int i){
		if(i==1){
			return "ByteUtils.toDouble(bytes["+start+"], "+der+", "+diff+")";
		}else if(i==2){
			return "ByteUtils.toDouble(bytes["+start+"], bytes["+(start+1)+"],"+der+", "+diff+")";
		}else if(i==3){
			return "ByteUtils.toDouble((byte)0x00,bytes["+start+"], bytes["+(start+1)+"],bytes["+(start+2)+"],"+der+", "+diff+")";
		}else if(i==4){
			return "ByteUtils.toDouble(bytes["+start+"], bytes["+(start+1)+"],bytes["+(start+2)+"],bytes["+(start+3)+"],"+der+", "+diff+")";
		}else{
			return "error";
		}
	}
	
	private String toDouble2(int i){
		if(i==1){
			return "ByteUtils.toDouble(bytes[position+i*"+i+"], "+der+", "+diff+")";
		}else if(i==2){
			return "ByteUtils.toDouble(bytes[position+i*"+i+"], bytes[position+1+i*"+i+"],"+der+", "+diff+")";
		}else if(i==3){
			return "ByteUtils.toDouble((byte)0x00,bytes[position+i*"+i+"], bytes[position+1+i*"+i+"],bytes[position+2+i*"+i+"],"+der+", "+diff+")";
		}else if(i==4){
			return "ByteUtils.toDouble(bytes[position+i*"+i+"], bytes[position+1+i*"+i+"],bytes[position+2+i*"+i+"],bytes[position+3+i*"+i+"],"+der+", "+diff+")";
		}else{
			return "error";
		}
	}
	
	private String handleDiff(String result){
		if(diff>0){
			result=result+"-"+diff;
		}else if(diff<0){
			result=result+"+"+(-diff);
		}
		return result;
	}
	
	private String handleCrossByte(int i){
		String temp="";
		int endLength=startBit-start*8+1;
		endLength=endLength==0?8:endLength;
		int startLength=(length-endLength)%8;
		startLength=startLength==0?8:startLength;
		for(int x=start+i;x>=start;x--){
			if(x==start){
				temp=temp+"(("+getHex(endLength)+" & bytes["+x+"]) << "+(startLength+8*(x-start))+")";
			}else if(start<x&&x<start+i){
				temp=temp+"(bytes["+x+"] << "+(startLength+8*(x-start))+") + ";
			}else if(x==start+i){
				temp=temp+"("+getHex(startLength)+" & (bytes["+x+"] >> "+(8-startLength)+")) + ";
			}
		} 
		return temp;
	}
	
	private String getHex(int length){
		return ByteUtils.binaryString2hexString(getBinary(length));
	}
	
	private String getBinary(int length){
		String temp="";
		for(int x=0;x<8;x++){
			if(x<length){
				temp="1"+temp;
			}else{
				temp="0"+temp;
			}
		}
		return temp; 
	}
}
