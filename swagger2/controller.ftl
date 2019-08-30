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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * ${commentInfo}Controller类
 * @author ${author}
 * @date ${.now?date}
 */
@Api(tags={"${commentInfo}管理"})
@Controller
@RequestMapping("/${ftlPath}")
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
	@ApiOperation("新增${commentInfo}")
	@ApiResponses(value ={@ApiResponse(code = 200, message = "${commentInfo}id", response = String.class)})
	@ApiImplicitParams({ 
			@ApiImplicitParam(name = "${required}", required = true),
			@ApiImplicitParam(name = "${norequired}", required = false) })
	@RequestMapping(value="/add",method=RequestMethod.POST,consumes="application/json;charset=UTF-8")
	@ResponseBody
	public String add${entityName}(@ApiParam(value = "${commentInfo}信息", required = true)@RequestBody ${entityName}Vo ${entityName?uncap_first}Vo, 
			HttpServletRequest request) throws Exception{
		<#if (requiredParams?length >2) >
		if(!LogicUtil.checkBeanNullAndEmpty(${entityName?uncap_first}Vo,${requiredParams})){
			paramsError();
		}
		</#if>
		<#if (uniques?size>1)>
		${entityName?uncap_first}Service.checkUnique(${entityName?uncap_first}Vo);
		</#if>
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
	@ApiOperation("修改${commentInfo}")
	@ApiResponses(value ={@ApiResponse(code = 200, message = "${commentInfo}id", response = String.class)})
	@ApiImplicitParams({ 
			@ApiImplicitParam(name = "$id", required = true),
			@ApiImplicitParam(name = "${updateNames}", required = false) })
	@RequestMapping(value="/update",method=RequestMethod.POST,consumes="application/json;charset=UTF-8")
	@ResponseBody
	public String update${entityName}(@ApiParam(value = "${commentInfo}信息", required = true)@RequestBody ${entityName}Vo ${entityName?uncap_first}Vo,  
			HttpServletRequest request) throws Exception {
		if(LogicUtil.isNullOrEmpty(${entityName?uncap_first}Vo.getId())){
			paramsError();
		}
		<#if (uniques?size>1)>
		${entityName?uncap_first}Service.checkUnique(${entityName?uncap_first}Vo);
		</#if>
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
	@ApiOperation("删除${commentInfo}")
	@ApiResponses(value ={@ApiResponse(code = 200, message = "success", response = Void.class)})
	@ApiImplicitParams({ 
			@ApiImplicitParam(name = "$id", required = true)})
	@RequestMapping(value="/remove",method=RequestMethod.POST,consumes="application/json;charset=UTF-8")
	@ResponseBody
	public String remove(@ApiParam(value = "${commentInfo}信息", required = true)@RequestBody ${entityName}Query query,
			HttpServletRequest request) throws Exception {
		if(LogicUtil.isNullOrEmpty(query.getId())){
			paramsError();
		}
		${entityName?uncap_first}Service.removeById(query.getId());
		/*保存日志*/
		this.saveLog(query,request,LOG_TYPE.DELETE,"${commentInfo},id:"+query.getId());
		return response(null);
	}
	
	/**
	 * ${commentInfo}详情
	 * 
	 * @param query
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ApiOperation("${commentInfo}详情")
	@ApiResponses(value ={@ApiResponse(code = 200, message = "success", response = ${entityName}.class)})
	@ApiImplicitParams({ 
			@ApiImplicitParam(name = "$id", required = true)})
	@RequestMapping(value="/info",method=RequestMethod.POST,consumes="application/json;charset=UTF-8")
	@ResponseBody
	public String info(@ApiParam(value = "${commentInfo}信息", required = true)@RequestBody ${entityName}Query query,
			HttpServletRequest request) throws Exception {
		if(LogicUtil.isNullOrEmpty(query.getId())){
			paramsError();
		}
		${entityName} ${entityName?uncap_first}=${entityName?uncap_first}Service.getById(query.getId());
		/*保存日志*/
		this.saveLog(query,request,LOG_TYPE.QUERY,"${commentInfo},id:"+query.getId());
		return response(${entityName?uncap_first});
	}
	
	/**
	 * 分页查询${commentInfo}列表
	 * @param query
	 * @param modelMap
	 * @return
	 * @throws Exception
	 */
	@ApiOperation("分页查询${commentInfo}列表")
	@ApiResponses(value ={@ApiResponse(code = 200, message = "${commentInfo}列表", response = ${entityName}.class,responseContainer = "list")})
	@ApiImplicitParams({ 
			@ApiImplicitParam(name = "$", required = false)})
	@RequestMapping(value="/pageQuery",method=RequestMethod.POST,consumes="application/json;charset=UTF-8")
	@ResponseBody
	public String pageQuery${entityName}(@ApiParam(value = "${commentInfo}信息", required = true)@RequestBody ${entityName}Query query,
			HttpServletRequest request)throws Exception {
	
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
	@ApiOperation("非分页查询${commentInfo}列表")
	@ApiResponses(value ={@ApiResponse(code = 200, message = "${commentInfo}列表", response = ${entityName}.class,responseContainer = "list")})
	@ApiImplicitParams({ 
			@ApiImplicitParam(name = "$", required = false)})
	@RequestMapping(value="/query",method=RequestMethod.POST,consumes="application/json;charset=UTF-8")
	@ResponseBody
	public String query${entityName}(@ApiParam(value = "${commentInfo}信息", required = true)@RequestBody ${entityName}Query query,
			HttpServletRequest request)throws Exception {
	
		List<${entityName}> list = ${entityName?uncap_first}Service.query(query);
		/*保存日志*/
		this.saveLog(query,request,LOG_TYPE.QUERY,"${commentInfo}");
		return response(list);
	}
	<#if (uniques?size>1)>
	/**
	 * 校验${commentInfo}属性
	 * @param vo
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ApiOperation("校验${commentInfo}属性")
	@ApiResponses(value ={@ApiResponse(code = 200, message = "校验结果", response = Void.class)})
	@ApiImplicitParams({ 
			@ApiImplicitParam(name = "${uniqueNames}", required = false)})
	@RequestMapping(value="/check",method=RequestMethod.POST,consumes="application/json;charset=UTF-8")
	@ResponseBody
	public String check${entityName}(@ApiParam(value = "${commentInfo}信息", required = true)@RequestBody ${entityName}Vo ${entityName?uncap_first}Vo,
			HttpServletRequest request)throws Exception {
		${entityName?uncap_first}Service.checkUnique(${entityName?uncap_first}Vo);
		/*保存日志*/
		this.saveLog(${entityName?uncap_first}Vo,request,LOG_TYPE.QUERY,"检验${commentInfo}");
		return response(null);
	}
	</#if>
}