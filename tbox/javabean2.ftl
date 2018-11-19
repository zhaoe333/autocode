package com.cm.tbox.packet;

import com.cm.common.utils.ByteUtils;
import com.cm.tbox.packet.base.BasePacket;
import java.util.Date;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
/**
 * 
 * @author yunlu
 *
 */
public class CarMSDataPacket implements BasePacket{
	
	/**时间周期*/
	private Date uploadTime;
	
	private Integer length;
	
	<#list datas as data>
	/** ${data.des} <#if data.des2??&&data.des2!=" ">${data.des2} </#if><#if data.unit??&&data.unit!=" ">单元：${data.unit} </#if><#if (data.max>0) >范围：${data.min?c}至${data.max?c}</#if>*/
	private CarMSDataItemPacket ${data.name}; 
	</#list>
	
	@Override
	public void build(byte[] bytes) throws Exception {
		int position=0;
		while(position<bytes.length){
			position=convert(position,bytes);
		}
		length=position;
	}
	
	public int convert(int position,byte[] bytes)throws Exception{
		int dataId = bytes[position];
		int timePeriod = bytes[position+1];
		int dataLength= bytes[position+2];
		position+=3;
		switch (dataId){
			<#list ids as key>
			case ${key}:
			<#list dataIds[key] as data>
				${data.name}= new CarMSDataItemPacket(dataLength,timePeriod);
			</#list>
				for(int i=0;i<dataLength;i++){
				<#list dataIds[key] as data>
					${data.name}.put(${data.code4});
				</#list>
				}
				break;	
			</#list>
			default:
				break;
		}
		return position;
	}
	
	@Override
	public byte[] unbuild() throws Exception {
		
	}
	@Override
	public Integer length() throws Exception {
		return length;
	}
	<#list datas as data>
	public CarMSDataItemPacket get${data.name?cap_first}() {
        return this.${data.name};
    }
    public void set${data.name?cap_first}(CarMSDataItemPacket ${data.name}) {
        this.${data.name} = ${data.name};
    }
	</#list>
}
