package ${packagePath}.${prex}.service;

import com.cm.common.query.PageFinder;
import ${packagePath}.${prex}.entity.${entityName};
import ${packagePath}.${prex}.query.vo.${entityName}Vo;
import ${packagePath}.${prex}.query.${entityName}Query;
import java.util.List;
/**
 * ${commentInfo}Service接口类
 * @author ${author}
 * @createTime ${.now?date}
 */
public interface ${interfaceName} {

	/**
	 * 增加${commentInfo}
	 * @param ${entityName?uncap_first}Vo
	 * @return ${entityName}
	 * @throws Exception
	 */
	${entityName} save(${entityName}Vo ${entityName?uncap_first}Vo)throws Exception ;
	
	/**
	 * 修改${commentInfo}
	 * @param ${entityName?uncap_first}Vo
	 * @return id 
	 * @throws Exception
	 */
	<#if idType=="INTEGER">Integer<#else>String</#if> update(${entityName}Vo ${entityName?uncap_first}Vo)throws Exception ;
	
	/**
	 * 删除${commentInfo}
	 * @param id
	 * @throws Exception
	 */
	void removeById(<#if idType=="INTEGER">Integer<#else>String</#if> id)throws Exception ;
	
	/**
	 * 获取${commentInfo}
	 * @param id
	 * @return ${entityName}
	 * @throws Exception
	 */
	${entityName} getById(<#if idType=="INTEGER">Integer<#else>String</#if> id) throws Exception;
	
	/**
	 * 分页查询${commentInfo}
	 * @param query
	 * @return pageFinder
	 * @throws Exception
	 */
	PageFinder<${entityName}> queryPage(${entityName}Query query) throws Exception;
	
	/**
	 * 非分页查询${commentInfo}
	 * @param query
	 * @return list
	 * @throws Exception
	 */
	List<${entityName}> query(${entityName}Query query) throws Exception;
	<#if (uniques?size>1)>
	/**
	 * 检查唯一字段
	 * @param query
	 * @return 重复个数 无重复返回0
	 * @throws Exception
	 */
	public void checkUnique(${entityName}Vo vo)throws Exception;
	</#if>		
}