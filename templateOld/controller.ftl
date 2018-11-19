package ${packagePath}.web.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cm.common.utils.LogicUtil;
import com.cm.common.basedao.PageFinder;
import com.cm.common.basedao.Query;
import com.cm.common.BaseController;
import ${packagePath}.model.pojo.${entityName};
import ${packagePath}.model.vo.${entityName}Vo;
import ${packagePath}.model.vo.${entityName}QueryVo;
import ${packagePath}.service.I${entityName}Service;
import java.util.List;
/**
 * ${commentInfo}Controller类
 * @author ${author}
 * @date ${.now?date}
 */
@Controller
@RequestMapping("/${ftlPath}")
public class ${className} extends BaseController {

	private static final Logger log = LoggerFactory.getLogger(${className}.class);

	@Resource
	private I${entityName}Service ${entityName?uncap_first}ServiceImpl;
	
	/**
	 * 增加${commentInfo}
	 * 
	 * @param ${entityName}Vo
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("add")
	@ResponseBody
	public String add${entityName}(${entityName}Vo ${entityName?uncap_first}Vo, HttpServletRequest request) throws Exception {
		if(!LogicUtil.checkBeanNullAndEmptyEx(${entityName?uncap_first}Vo, "id")){
			paramsError();
		}
		${entityName?uncap_first}ServiceImpl.save${entityName}(${entityName?uncap_first}Vo);
		return response(null);
	}
	
	/**
	 * 修改${commentInfo}
	 * 
	 * @param ${entityName}Vo
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/update")
	@ResponseBody
	public String update${entityName}(${entityName}Vo ${entityName?uncap_first}Vo, HttpServletRequest request) throws Exception {
		if(LogicUtil.isNullOrEmpty(${entityName?uncap_first}Vo.getId())){
			paramsError();
		}
		${entityName?uncap_first}ServiceImpl.update${entityName}(${entityName?uncap_first}Vo);
		return response(null);
	}
	
	/**
	 * 删除${commentInfo}
	 * @param queryVo
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/remove")
	public String remove${entityName}(${entityName}QueryVo queryVo,HttpServletRequest request) throws Exception {
		if(LogicUtil.isNullOrEmpty(queryVo.getId())){
			paramsError();
		}
    	${entityName?uncap_first}ServiceImpl.remove${entityName}(queryVo.getId());
       	return response(null);
	}
	
	/**
	 * 批量删除${commentInfo}
	 * @param queryVo
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/removeBatch")
	public String remove${entityName}Batch(${entityName}QueryVo queryVo,HttpServletRequest request) throws Exception {
		if(LogicUtil.isNullOrEmpty(queryVo.getIds())){
			paramsError();
		}
		String[] ids = queryVo.getIds().split(",");
    	${entityName?uncap_first}ServiceImpl.remove${entityName}Batch(ids);
       	return response(null);
	}
	
	
	/**
	 * 获取某个详细信息
	 * @param id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/info")
	@ResponseBody
	public String get${entityName}Detail(String id, HttpServletRequest request, ModelMap modelMap)throws Exception {
		if(LogicUtil.isNullOrEmpty(id)){
			paramsError();
		}
		${entityName} ${entityName?uncap_first} = ${entityName?uncap_first}ServiceImpl.getId(id);
		return response(${entityName?uncap_first});
	}
	
	/**
	 * 分页查询列表
	 * @param queryVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/pageQuery")
	@ResponseBody
	public String pageQuery${entityName}(${entityName}QueryVo queryVo, HttpServletRequest request)throws Exception {
		PageFinder<${entityName}> pageFinder = ${entityName?uncap_first}ServiceImpl.queryPage${entityName}(queryVo);
		return response(pageFinder);
	}
	
	/**
	 * 不分页查询
	 * @param queryVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/query")
	@ResponseBody
	public String query${entityName}(${entityName}QueryVo queryVo, HttpServletRequest request)throws Exception {
		List<${entityName}> ${entityName?uncap_first}s = ${entityName?uncap_first}ServiceImpl.query${entityName}(queryVo);
		return response(${entityName?uncap_first}s);
	}
}