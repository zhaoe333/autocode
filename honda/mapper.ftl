package ${packagePath}.${prex}.mapper;

import com.cm.common.mybatis.SqlMapper;
import ${packagePath}.${prex}.entity.${entityName};
import ${packagePath}.${prex}.query.${entityName}Query;
import java.util.List;

/**
 * ${commentInfo}mapper接口类
 * @author ${author}
 * @createTime ${.now?date}
 */
public interface ${interfaceName} extends SqlMapper<${entityName}> {
	/**
	 * 查询数据
	 * @param query
	 * @return
	 * @throws Exception
	 */
	List<${entityName}> selectByQuery(${entityName}Query query)throws Exception;
	/**
	 * 查询数据（分页）
	 * @param query
	 * @return
	 * @throws Exception
	 */
	List<${entityName}> selectByPage(${entityName}Query query)throws Exception;
	/**
	* 删除数据
	* @param id
	* @return
	* @throws Exception
	*/
	void deleteById(<#if idType=="INTEGER">Integer<#else>String</#if> id)throws Exception;
	/**
	* 删除数据
	* @param ids
	* @return
	* @throws Exception
	*/
	void deleteByIds(List<<#if idType=="INTEGER">Integer<#else>String</#if>> ids)throws Exception;
	/**
	* 获取单个数据
	* @param id
	* @return
	* @throws Exception
	*/
	${entityName} getById(<#if idType=="INTEGER">Integer<#else>String</#if> id)throws Exception;
	<#if (uniques?size>1)>
	/**
	 * 检查唯一字段
	 * @param ${entityName}
	 * @return 无重复返回0 
	 * @throws Exception
	 */
	int checkUnique(${entityName}Query query)throws Exception;
	</#if>
}