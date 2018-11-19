<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper
  PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
  "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${packagePath}.${prex}.mapper.${entityName}Mapper">
  <resultMap id="BaseResultMap" type="${entityName?lower_case}" >
    <id column="id" property="id" jdbcType="INTEGER" />
    <#list columns as column>
    <result column="${column.columnName}" property="${column.name}" jdbcType="${column.jdbcName}" />
    </#list>
  </resultMap>
    <insert id="create" parameterType="${entityName?lower_case}" useGeneratedKeys="true" keyProperty="id">    
        insert into ${tableName}(
        <#list columns as column>
        	<#if column_has_next >
        		${column.columnName},
        	<#else>
        		${column.columnName}
        	</#if>
        </#list>
        )values(
        <#list columns as column>
        	<#if column_has_next >
        		${"#{"+column.name+"}"},
        	<#else>
        		${"#{"+column.name+"}"}
        	</#if>
        </#list>
        )
    </insert>
    <update id="update" parameterType="${entityName?lower_case}" >
    	update ${tableName}
    	<set>
    		<#list columns as column>
       			<#if column.isKey != 0>
        		<#if column_has_next >
        			<#if column.jdbcName=="INTEGER">
        			<if test="${column.name} != -1">
        			<#else>
        			<if test="${column.name} != null">
        			</#if>
    					${column.columnName} = ${"#{"+column.name+"}"},
    				</if>
        		<#else>
        			<#if column.jdbcName=="INTEGER">
        			<if test="${column.name} != -1">
        			<#else>
        			<if test="${column.name} != null">
        			</#if>
    					${column.columnName} = ${"#{"+column.name+"}"}
    				</if>
        		</#if>
        	</#if>
        	</#list>
    	</set>
    	where id = ${"#"}{id}
    </update>
    <#if hasDeleteFlag>
     <update id="deleteById" parameterType="string" >
    	update ${tableName}
    	<set>
    		delete_flag = ${"#"}{deleteFlag}
    	</set>
    	where id = ${"#"}{id}
    </update>
    <update id="deleteByIds" parameterType="list" >
    	update ${tableName}
    	<set>
    		delete_flag = ${"#"}{deleteFlag}
    	</set>
    	where id in
    	<foreach item="item" index="index" collection="ids"
      					open="(" separator="," close=")">
        	${"#"}{item}
  		</foreach>
    </update>
    <#else>
    <delete id="deleteById" parameterType="string">
        	delete from ${tableName} where id = ${"#"}{id}
    </delete>
    <delete id="deleteByIds" parameterType="list" >
    	delete from ${tableName}
    	where id in
    	<foreach item="item" index="index" collection="ids"
      					open="(" separator="," close=")">
        	${"#"}{item}
  		</foreach>
    </delete>
    </#if>
    <select id="getById" resultMap="BaseResultMap" parameterType="string">
    	select * from ${tableName} where id = ${"#"}{id}
    </select>
   	<select id="selectByQuery" resultMap="BaseResultMap" parameterType="${entityName?lower_case}query">
    	select * from ${tableName}
    	<where>
    		<#list columns as column>
    			<#if column_index == 0 >
    				<#if column.jdbcName=="INTEGER">
        			<if test="${column.name} != -1">
        			<#else>
        			<if test="${column.name} != null">
        			</#if>
    					${column.columnName} = ${"#{"+column.name+"}"}
    				</if>
    			<#else>
    				<#if column.jdbcName=="INTEGER">
        			<if test="${column.name} != -1">
        			<#else>
        			<if test="${column.name} != null">
        			</#if>
    					and ${column.columnName} = ${"#{"+column.name+"}"}
    				</if>
    			</#if>
    		</#list>
    	</where>
    </select>
    
    <select id="selectByPage" resultMap="BaseResultMap" parameterType="${entityName?lower_case}query">
    	select * from ${tableName}
    	<where>
    		<#list columns as column>
    			<#if column_index == 0 >
    				<#if column.jdbcName=="INTEGER">
        			<if test="${column.name} != -1">
        			<#else>
        			<if test="${column.name} != null">
        			</#if>
    					${column.columnName} = ${"#{"+column.name+"}"}
    				</if>
    			<#else>
    				<#if column.jdbcName=="INTEGER">
        			<if test="${column.name} != -1">
        			<#else>
        			<if test="${column.name} != null">
        			</#if>
    					and ${column.columnName} = ${"#{"+column.name+"}"}
    				</if>
    			</#if>
    		</#list>
    	</where>
    	limit ${"#"}{begin},${"#"}{pageSize}
    </select>
</mapper>