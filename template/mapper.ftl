package ${packagePath}.${prex}.mapper;

import com.cm.common.mybatis.SqlMapper;
import ${packagePath}.${prex}.entity.${entityName};
import ${packagePath}.${prex}.query.${entityName}Query;
import java.util.List;

/**
 * ${commentInfo}mapper接口类
 * @author ${author}
 * @date ${.now?date}
 */
public interface ${interfaceName} extends SqlMapper<${entityName}> {
	/**
	 * 查询数据
	 * @param query
	 * @return
	 * @throws Exception
	 */
	public List<${entityName}> selectByQuery(${entityName}Query query)throws Exception;
	/**
	 * 查询数据（分页）
	 * @param query
	 * @return
	 * @throws Exception
	 */
	public List<${entityName}> selectByPage(${entityName}Query query)throws Exception;
}