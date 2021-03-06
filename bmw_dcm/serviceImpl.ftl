package ${packagePath}.${prex}.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cm.common.utils.BeanUtil;
import com.cm.common.query.PageFinder;
import com.cm.common.utils.UUIDUtils;
import lombok.extern.slf4j.Slf4j;
<#if (uniques?size>1)>
import com.cm.common.http.FMResponseCode;
import com.cm.common.exception.FMException;
import com.cm.common.utils.LogicUtil;
</#if>
import ${packagePath}.${prex}.mapper.${entityName}Mapper;
import ${packagePath}.${prex}.entity.${entityName};
import ${packagePath}.${prex}.query.vo.${entityName}Vo;
import ${packagePath}.${prex}.query.${entityName}Query;
import ${packagePath}.${prex}.service.I${entityName}Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import java.util.Date;
import java.util.List;
/**
 * ${commentInfo}Service实现类
 * @author ${author}
 * @createTime ${.now?date}
 */
@Service
@Slf4j
public class ${className} implements ${interfaceName} {
	
	@Resource
	private ${entityName}Mapper ${entityName?uncap_first}Mapper;

	@Override
	@Transactional
	public ${entityName} save(${entityName}Vo ${entityName?uncap_first}Vo)throws Exception {
		${entityName} ${entityName?uncap_first} = new ${entityName}();
		<#if hasTime>
		${entityName?uncap_first}.setCreateTime(new Date());
		${entityName?uncap_first}.setUpdateTime(new Date());
		</#if>
		<#if idType=="String">
			String id = UUIDUtils.generateUUID();
			${entityName?uncap_first}.setId(id);
		</#if>
		BeanUtil.copyPropertiesNotNull(${entityName?uncap_first}Vo, ${entityName?uncap_first});
		${entityName?uncap_first}Mapper.create(${entityName?uncap_first});
		return ${entityName?uncap_first};
	}
	
	@Override
	@Transactional
	public <#if idType=="INTEGER">Integer<#else>String</#if> update(${entityName}Vo ${entityName?uncap_first}Vo)throws Exception {
		${entityName} ${entityName?uncap_first} = new ${entityName}();
		BeanUtil.copyPropertiesNotNull(${entityName?uncap_first}Vo, ${entityName?uncap_first});
		<#if hasTime>
		${entityName?uncap_first}.setUpdateTime(new Date());
		</#if>
		${entityName?uncap_first}Mapper.update(${entityName?uncap_first});
		return ${entityName?uncap_first}Vo.getId();
	}
	
	@Override
	@Transactional
	public void removeById(<#if idType=="INTEGER">Integer<#else>String</#if> id)throws Exception {
		${entityName?uncap_first}Mapper.deleteById(id);
	}
	
	@Override
	@Transactional(readOnly=true)
	public ${entityName} getById(<#if idType=="INTEGER">Integer<#else>String</#if> id) throws Exception {
		return ${entityName?uncap_first}Mapper.getById(id);
	}
	
	@Override
	@Transactional(readOnly=true)
	public PageFinder<${entityName}> queryPage(${entityName}Query query) throws Exception{
	    PageHelper.startPage(query.getPageNo(), query.getPageSize());
		List<${entityName}> ${entityName?uncap_first}s = ${entityName?uncap_first}Mapper.selectByPage(query);
		return new PageFinder<${entityName}>((Page)${entityName?uncap_first}s);
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<${entityName}> query(${entityName}Query query) throws Exception{
		List<${entityName}> ${entityName?uncap_first}s = ${entityName?uncap_first}Mapper.selectByQuery(query);
		return ${entityName?uncap_first}s;
	}
	<#if (uniques?size>1)>
	@Override
	@Transactional(readOnly=true)
	public void checkUnique(${entityName}Vo vo)throws Exception{
		${entityName}Query query = new ${entityName}Query();
		query.setId(vo.getId());
		<#if hasDeleteFlag>
		query.setDeleteFlag(0);
		</#if>
		<#list uniques as column>
		if(LogicUtil.isNotNullAndEmpty(vo.get${column.name?cap_first}())){
			<#if column.name != "id">
			query.set${column.name?cap_first}(vo.get${column.name?cap_first}());
			if(${entityName?uncap_first}Mapper.checkUnique(query)>0){
				FMException.throwEx(FMResponseCode.propertyUsed,"${column.comment}已存在");
			}
			query.set${column.name?cap_first}(null);
			</#if>
		}
		</#list>
	}
	</#if>	
}