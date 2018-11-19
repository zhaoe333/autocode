<#list datas?keys as key>
case ${key}:
	<#list datas[key] as data>
	<#if data.des != '保留数据字节' && data.length <48>
	canDataMap.put("${data.name}",${data.code});
	</#if>
	</#list>
	break;
</#list>