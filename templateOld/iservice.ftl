package ${packagePath}.service;

import com.cm.common.basedao.PageFinder;
import ${packagePath}.model.pojo.${entityName};
import ${packagePath}.model.vo.${entityName}QueryVo;
import ${packagePath}.model.vo.${entityName}Vo;
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
	public ${entityName} save${entityName}(${entityName}Vo ${entityName?uncap_first}Vo)throws Exception ; 
	
	/**
	 * 修改${commentInfo}
	 * @param ${entityName?uncap_first}Vo
	 * @return id 
	 * @throws Exception
	 */
	public String update${entityName}(${entityName}Vo ${entityName?uncap_first}Vo)throws Exception ; 
	
	/**
	 * 删除${commentInfo}
	 * @param ${entityName?uncap_first}Vo
	 * @return id 
	 * @throws Exception
	 */
	public String remove${entityName}(${entityName}Vo ${entityName?uncap_first}Vo)throws Exception ; 
	
	/**
	 * 删除${commentInfo}
	 * @param id
	 * @throws Exception
	 */
	public void remove${entityName}(String id)throws Exception ; 
	
	/**
	 * 获取${commentInfo}
	 * @param id
	 * @return ${entityName}
	 * @throws Exception
	 */
	public ${entityName} getId(String id) throws Exception;
	
	/**
	 * 分页查询${commentInfo}
	 * @param queryVo
	 * @return pageFinder
	 * @throws Exception
	 */
	public PageFinder<${entityName}> queryPage${entityName}(${entityName}QueryVo queryVo) throws Exception;
	
	/**
	 * 不分页查询${commentInfo}
	 * @param queryVo
	 * @return list
	 * @throws Exception
	 */
	public List<${entityName}> query${entityName}(${entityName}QueryVo queryVo) throws Exception;
	
	/**
	 * 批量删除
	 * @param ids
	 * @throws Exception
	 */
	public void remove${entityName}Batch(String[] ids) throws Exception;
			
}