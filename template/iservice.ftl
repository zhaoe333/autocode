package ${packagePath}.${prex}.service;

import com.cm.common.query.PageFinder;
import ${packagePath}.${prex}.entity.${entityName};
import ${packagePath}.${prex}.query.vo.${entityName}Vo;
import ${packagePath}.${prex}.query.${entityName}Query;
import java.util.List;
/**
 * ${commentInfo}Service接口类
 * @author ${author}
 * @date ${.now?date}
 */
public interface ${interfaceName} {

	/**
	 * 增加${commentInfo}
	 * @param ${entityName?uncap_first}Vo
	 * @return ${entityName}
	 * @throws Exception
	 */
	public ${entityName} save(${entityName}Vo ${entityName?uncap_first}Vo)throws Exception ; 
	
	/**
	 * 修改${commentInfo}
	 * @param ${entityName?uncap_first}Vo
	 * @return id 
	 * @throws Exception
	 */
	public String update(${entityName}Vo ${entityName?uncap_first}Vo)throws Exception ; 
	
	/**
	 * 删除${commentInfo}
	 * @param id
	 * @throws Exception
	 */
	public void removeById(String id)throws Exception ; 
	
	/**
	 * 获取${commentInfo}
	 * @param id
	 * @return ${entityName}
	 * @throws Exception
	 */
	public ${entityName} getById(String id) throws Exception;
	
	/**
	 * 分页查询${commentInfo}
	 * @param queryVo
	 * @return pageFinder
	 * @throws Exception
	 */
	public PageFinder<${entityName}> queryPage(${entityName}Query query) throws Exception;
	
	/**
	 * 非分页查询${commentInfo}
	 * @param query
	 * @return list
	 * @throws Exception
	 */
	public List<${entityName}> query(${entityName}Query query) throws Exception;		
}