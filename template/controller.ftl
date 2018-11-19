package ${packagePath}.${prex}.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;

import com.cm.common.constant.CommonConstants;
import com.cm.common.constant.CommonConstants.LOG_TYPE;
import com.cm.common.query.PageFinder;
import com.cm.common.spring.BaseController;
import com.cm.common.utils.LogicUtil;
import java.util.List;
import ${packagePath}.${prex}.entity.${entityName};
import ${packagePath}.${prex}.query.vo.${entityName}Vo;
import ${packagePath}.${prex}.query.${entityName}Query;
import ${packagePath}.${prex}.service.I${entityName}Service;

/**
 * ${commentInfo}Controller类
 * @author ${author}
 * @date ${.now?date}
 */
@Controller
@RequestMapping("/${prex}/${ftlPath}")
public class ${className} extends BaseController {

	private static final Logger log = LoggerFactory.getLogger(${className}.class);

	@Resource
	private I${entityName}Service ${entityName?uncap_first}Service;
	
	/**
	 * 新增${commentInfo}
	 * @param ${entityName}Vo
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/add",method=RequestMethod.POST,consumes="application/json;charset=UTF-8")
	@ResponseBody
	public String add${entityName}(@RequestBody ${entityName}Vo ${entityName?uncap_first}Vo, HttpServletRequest request) throws Exception{
		
		${entityName} ${entityName?uncap_first}=${entityName?uncap_first}Service.save(${entityName?uncap_first}Vo);
		this.saveLog(${entityName?uncap_first}Vo,request,LOG_TYPE.ADD,"${commentInfo},id:"+${entityName?uncap_first}.getId());
		return  response(${entityName?uncap_first}.getId());
	}
	
	/**
	 * 修改${commentInfo}
	 * 
	 * @param ${entityName}Vo
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/update",method=RequestMethod.POST,consumes="application/json;charset=UTF-8")
	@ResponseBody
	public String update${entityName}(@RequestBody ${entityName}Vo ${entityName?uncap_first}Vo,  HttpServletRequest request) throws Exception {
		if(LogicUtil.isNullOrEmpty(${entityName?uncap_first}Vo.getId())){
			paramsError();
		}
		${entityName?uncap_first}Service.update(${entityName?uncap_first}Vo);
		this.saveLog(${entityName?uncap_first}Vo,request,LOG_TYPE.UPDATE,"${commentInfo},id:"+${entityName?uncap_first}Vo.getId());
		return response(${entityName?uncap_first}Vo.getId());
	}
	
	/**
	 * 删除${commentInfo}
	 * 
	 * @param query
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/remove",method=RequestMethod.POST,consumes="application/json;charset=UTF-8")
	@ResponseBody
	public String remove(@RequestBody ${entityName}Query query,HttpServletRequest request) throws Exception {
		if(LogicUtil.isNullOrEmpty(query.getId())){
			paramsError();
		}
		${entityName?uncap_first}Service.removeById(query.getId());
		/*保存日志*/
		this.saveLog(query,request,LOG_TYPE.DELETE,"${commentInfo},id:"+query.getId());
		return response(null);
	}
	
	
	/**
	 * 分页查询${commentInfo}列表
	 * @param query
	 * @param modelMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/pageQuery",method=RequestMethod.POST,consumes="application/json;charset=UTF-8")
	@ResponseBody
	public String pageQuery${entityName}(@RequestBody ${entityName}Query query,HttpServletRequest request)throws Exception {
	
		PageFinder<${entityName}> pageFinder = ${entityName?uncap_first}Service.queryPage(query);
		/*保存日志*/
		this.saveLog(query,request,LOG_TYPE.QUERY,"${commentInfo}");
		return response(pageFinder);
	}
	
	/**
	 * 非分页查询${commentInfo}列表
	 * @param query
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/query",method=RequestMethod.POST,consumes="application/json;charset=UTF-8")
	@ResponseBody
	public String query${entityName}(@RequestBody ${entityName}Query query,HttpServletRequest request)throws Exception {
	
		List<${entityName}> list = ${entityName?uncap_first}Service.query(query);
		/*保存日志*/
		this.saveLog(query,request,LOG_TYPE.QUERY,"${commentInfo}");
		return response(list);
	}
}