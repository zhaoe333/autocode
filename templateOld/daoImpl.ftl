package ${packagePath}.dao.impl;

import org.springframework.stereotype.Repository;

import com.cm.common.basedao.HibernateBaseDao;
import ${packagePath}.dao.I${entityName}Dao;
import ${packagePath}.model.pojo.${entityName};

/**
 * ${commentInfo}Dao实现类
 * @author ${author}
 * @date ${.now?date}
 */
@Repository
public class ${className} extends HibernateBaseDao<${entityName}> implements ${interfaceName} {

}