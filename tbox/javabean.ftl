package com.cm.tbox.gb.packet.realtime;

import com.cm.common.utils.ByteUtils;
import com.cm.tbox.gb.packet.base.GBBasePacket;
import java.util.Date;
import java.util.Arrays;
/**
 * 
 * @author yunlu
 *
 */
public class ${name} implements GBBasePacket{
	<#list datas as data>
	/** ${data.des} <#if data.des2??&&data.des2!=" ">${data.des2} </#if><#if data.unit??&&data.unit!=" ">单元：${data.unit} </#if><#if (data.max>0) >范围：${data.min?c}至${data.max?c}</#if>*/
	private ${data.javaType} ${data.name}; 
	</#list>
	
	@Override
	public void build(byte[] bytes) throws Exception {
	<#list datas as data>
		${data.name} = ${data.code2};
	</#list>
	}
	@Override
	public byte[] unbuild() throws Exception {
	<#list datas as data>
	<#if data.length==8>
		byte ${data.byteName} = ${data.code3};
	<#else>
		byte[] ${data.byteName} = ${data.code3};
	</#if>
	</#list>
		return ByteUtils.addAll(<#list datas as data><#if data_index==0>${data.byteName}<#else>,${data.byteName}</#if></#list>);
	}
	@Override
	public Integer length() throws Exception {
		return ${length};
	}
	<#list datas as data>
	public ${data.javaType} get${data.name?cap_first}() {
        return this.${data.name};
    }
    public void set${data.name?cap_first}(${data.javaType} ${data.name}) {
        this.${data.name} = ${data.name};
    }
	</#list>
}
