<#if table?exists>
    <#list table?keys as key> 
       ${key}:${table.method(key)}
   </#list>
</#if>
