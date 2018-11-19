package ${packagePath}.${prex}.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cm.common.utils.BeanUtil;
import com.cm.common.query.PageFinder;
import com.cm.common.utils.UUIDUtils;
import ${packagePath}.${prex}.mapper.${entityName}Mapper;
import ${packagePath}.${prex}.entity.${entityName};
import ${packagePath}.${prex}.query.vo.${entityName}Vo;
import ${packagePath}.${prex}.query.${entityName}Query;
import ${packagePath}.${prex}.service.I${entityName}Service;
import java.util.Date;
import java.util.List;
/**
 * ${commentInfo}Service实现类
 * @author ${author}
 * @date ${.now?date}
 */
 @Service
public class ${className} implements ${interfaceName} {
	
	@Resource
	private ${entityName}Mapper ${entityName?uncap_first}Mapper;

	@Override
	@Transactional
	public ${entityName} save(${entityName}Vo ${entityName?uncap_first}Vo)throws Exception {
		${entityName} ${entityName?uncap_first} = new ${entityName}();
		<#--  ${entityName?uncap_first}.setCreateTime(new Date());
		${entityName?uncap_first}.setUpdateTime(new Date());-->
		String id = UUIDUtils.generateUUID();
		${entityName?uncap_first}.setId(id);
		BeanUtil.copyPropertiesNotNull(${entityName?uncap_first}Vo, ${entityName?uncap_first});
		${entityName?uncap_first}Mapper.create(${entityName?uncap_first});
		return ${entityName?uncap_first};
	}
	
	@Override
	@Transactional
	public String update(${entityName}Vo ${entityName?uncap_first}Vo)throws Exception {
		${entityName} ${entityName?uncap_first} = new ${entityName}();
		BeanUtil.copyPropertiesNotNull(${entityName?uncap_first}Vo, ${entityName?uncap_first});
		<#-- ${entityName?uncap_first}.setUpdateTime(new Date());-->
		${entityName?uncap_first}Mapper.update(${entityName?uncap_first});
		return ${entityName?uncap_first}Vo.getId();
	}
	
	@Override
	@Transactional
	public void removeById(String id)throws Exception {
		${entityName?uncap_first}Mapper.deleteById(id);
	}
	
	@Override
	@Transactional(readOnly=true)
	public ${entityName} getById(String id) throws Exception {
		return ${entityName?uncap_first}Mapper.getById(id);
	}
	
	@Override
	@Transactional(readOnly=true)
	public PageFinder<${entityName}> queryPage(${entityName}Query query) throws Exception{
		List<${entityName}> ${entityName?uncap_first}s = ${entityName?uncap_first}Mapper.selectByPage(query);
		PageFinder<${entityName}> pageFinder = new PageFinder<>(query, ${entityName?uncap_first}s);
		return pageFinder;
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<${entityName}> query(${entityName}Query query) throws Exception{
		List<${entityName}> ${entityName?uncap_first}s = ${entityName?uncap_first}Mapper.selectByQuery(query);
		return ${entityName?uncap_first}s;
	}
}