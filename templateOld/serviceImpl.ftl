package ${packagePath}.service.impl;

import javax.annotation.Resource;

import org.hibernate.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cm.common.utils.BeantUtil;
import com.cm.common.basedao.PageFinder;
import ${packagePath}.dao.I${entityName}Dao;
import ${packagePath}.model.pojo.${entityName};
import ${packagePath}.model.vo.${entityName}Vo;
import ${packagePath}.model.vo.${entityName}QueryVo;
import ${packagePath}.service.I${entityName}Service;
import java.util.List;
/**
 * ${commentInfo}Service实现类
 * @author ${author}
 * @date ${.now?date}
 */
 @Service
public class ${className} implements ${interfaceName} {
	
	@Resource
	private I${entityName}Dao ${entityName?uncap_first}DaoImpl;

	@Override
	@Transactional
	public ${entityName} save${entityName}(${entityName}Vo ${entityName?uncap_first}Vo)throws Exception {
		${entityName} ${entityName?uncap_first} = new ${entityName}();
		BeantUtil.copyPropertiesNotNull(${entityName?uncap_first}Vo, ${entityName?uncap_first});
		return ${entityName?uncap_first}DaoImpl.saveOrUpdate(${entityName?uncap_first});
	}
	
	@Override
	@Transactional
	public String update${entityName}(${entityName}Vo ${entityName?uncap_first}Vo)throws Exception {
		${entityName} ${entityName?uncap_first} = new ${entityName}();
		BeantUtil.copyPropertiesNotNull(${entityName?uncap_first}Vo, ${entityName?uncap_first});
		${entityName?uncap_first}DaoImpl.update(${entityName?uncap_first});
		return ${entityName?uncap_first}Vo.getId();
	}
	
	@Override
	@Transactional
	public String remove${entityName}(${entityName}Vo ${entityName?uncap_first}Vo)throws Exception {
		${entityName} ${entityName?uncap_first} = new ${entityName}();
		BeantUtil.copyPropertiesNotNull(${entityName?uncap_first}Vo, ${entityName?uncap_first});
		${entityName?uncap_first}DaoImpl.delete(${entityName?uncap_first});
		return ${entityName?uncap_first}Vo.getId();
	}
	
	@Override
	@Transactional
	public void remove${entityName}(String id)throws Exception {
		${entityName?uncap_first}DaoImpl.delete(id);
	}
	
	@Override
	@Transactional(readOnly=true)
	public ${entityName} getId(String id) throws Exception {
		return ${entityName?uncap_first}DaoImpl.get(id);
	}
	
	@Override
	@Transactional(readOnly=true)
	public PageFinder<${entityName}> queryPage${entityName}(${entityName}QueryVo queryVo) throws Exception{
		Criteria criteria = ${entityName?uncap_first}DaoImpl.createCriteria();
		return ${entityName?uncap_first}DaoImpl.pagedByCriteria(criteria, queryVo.getPageNo(), queryVo.getPageSize());
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<${entityName}> query${entityName}(${entityName}QueryVo queryVo)
			throws Exception {
		Criteria criteria = ${entityName?uncap_first}DaoImpl.createCriteria();
		return ${entityName?uncap_first}DaoImpl.getListByCriteria(criteria);
	}

	@Override
	@Transactional
	public void remove${entityName}Batch(String[] ids) throws Exception {
		${entityName?uncap_first}DaoImpl.delete(ids);
	}
}