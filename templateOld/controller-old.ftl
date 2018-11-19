package ${packagePath}.web;

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

import com.huawei.vifi.common.constant.Constant;
import com.cm.common.basedao.PageFinder;
import com.cm.common.basedao.Query;
import com.cm.common.BaseController;
import ${packagePath}.model.pojo.${entityName};
import ${packagePath}.model.vo.${entityName}Vo;
import ${packagePath}.model.vo.${entityName}QueryVo;
import ${packagePath}.service.I${entityName}Service;

/**
 * ${commentInfo}Controller类
 * @author ${author}
 * @date ${.now?date}
 */
@Controller
@RequestMapping("/${ftlPath}")
public class ${className} extends BaseController {

	private static final Logger log = LoggerFactory.getLogger(${className}.class);

	@Autowired  
	private HttpSession session;
	
	@Resource
	private I${entityName}Service ${entityName?uncap_first}ServiceImpl;
	
	/**
	 * 跳转到增加${commentInfo}页面
	 * @param modelMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/toAdd${entityName}")
	public ModelAndView toAdd${entityName}(ModelMap modelMap) {
		SystemmgtUser loginUser = (SystemmgtUser) session.getAttribute(Constant.LOGIN_SYSTEM_USER);
		try{
	        /*保存日志*/
			this.saveLog(loginUser.getId(), loginUser.getUsername()==null?loginUser.getLoginName():loginUser.getUsername(), "跳转到新增${entityName}页面", "To新增${entityName}");
	    }catch(Exception e){
	    	e.printStackTrace();
	    	log.error(e.toString());
	    }
		return new ModelAndView("${ftlPath}/add_${entityName?uncap_first}",modelMap);
	}
	
	/**
	 * 增加${commentInfo}
	 * 
	 * @param ${entityName}Vo
	 * @param modelMap
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("add${entityName}")
	public ModelAndView add${entityName}(${entityName}Vo ${entityName?uncap_first}Vo, ModelMap modelMap, HttpServletRequest request) throws Exception {

    	HttpSession session = request.getSession();
		SystemmgtUser loginUser = (SystemmgtUser) session.getAttribute(Constant.LOGIN_SYSTEM_USER);
		${entityName?uncap_first}ServiceImpl.save${entityName}(${entityName?uncap_first}Vo);
        try{	
        	/*保存日志*/
        	this.saveLog(loginUser.getId(), loginUser.getUsername()==null?loginUser.getLoginName():loginUser.getUsername(), "新增${entityName}", "新增${entityName}");
        }catch(Exception e){
        	log.error(e.toString());         	
        }
       return new ModelAndView("redirect:pageQuery${entityName}.sc",modelMap);
	}
	
	/**
	 * 跳转到修改${commentInfo}页面
	 * @param id
	 * @param modelMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/toUpdate${entityName}")
	public ModelAndView toUpdate${entityName}(String id, ModelMap modelMap) throws Exception{
		SystemmgtUser loginUser = (SystemmgtUser) session.getAttribute(Constant.LOGIN_SYSTEM_USER);
		${entityName} ${entityName?uncap_first} = ${entityName?uncap_first}ServiceImpl.getId(id);
		modelMap.put("${entityName?uncap_first}", ${entityName?uncap_first});
		
		try{
	        /*保存日志*/
			this.saveLog(loginUser.getId(), loginUser.getUsername()==null?loginUser.getLoginName():loginUser.getUsername(), "跳转到编辑${entityName}页面", "To编辑${entityName}");
	    }catch(Exception e){
	    	e.printStackTrace();
	       	log.error(e.toString());
	    }
	    return new ModelAndView("${ftlPath}/update_${entityName?uncap_first}",modelMap);
	}
	
	/**
	 * 修改${commentInfo}
	 * 
	 * @param ${entityName}Vo
	 * @param modelMap
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/update${entityName}")
	public ModelAndView update${entityName}(${entityName}Vo ${entityName?uncap_first}Vo, ModelMap modelMap, HttpServletRequest request) throws Exception {
	
    	HttpSession session = request.getSession();
		SystemmgtUser loginUser = (SystemmgtUser) session.getAttribute(Constant.LOGIN_SYSTEM_USER);
		${entityName?uncap_first}ServiceImpl.update${entityName}(${entityName?uncap_first}Vo);
        try{
			/*保存日志*/
			this.saveLog(loginUser.getId(), loginUser.getUsername()==null?loginUser.getLoginName():loginUser.getUsername(), "修改${entityName} ", "修改${entityName}");
        }catch(Exception e){
        	e.printStackTrace();
        	log.error(e.toString());          	
        }
      return new ModelAndView("redirect:pageQuery${entityName}.sc",modelMap);
	}
	
	/**
	 * 删除${commentInfo}
	 * 
	 * @param id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/remove${entityName}")
	public String remove${entityName}(String id,HttpServletRequest request) throws Exception {
	    try {
        	HttpSession session = request.getSession();
    		SystemmgtUser loginUser = (SystemmgtUser) session.getAttribute(Constant.LOGIN_SYSTEM_USER);
    		${entityName?uncap_first}ServiceImpl.remove${entityName}(id);
       		/*保存日志*/
        	this.saveLog(loginUser.getId(), loginUser.getUsername()==null?loginUser.getLoginName():loginUser.getUsername(), "删除${entityName}，id="+id, "删除${entityName}");
	        return Constant.SUCCESS;
        } catch (Exception e) {
        	e.printStackTrace();
        	log.error(e.toString());
            return Constant.FAIL;
        }
	}
	
	/**
	 * 到查看${commentInfo}详情页面
	 * @param id
	 * @param modelMap
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/to${entityName}Detail")
	public ModelAndView to${entityName}Detail(String id, HttpServletRequest request, ModelMap modelMap)throws Exception {
	
		${entityName} ${entityName?uncap_first} = ${entityName?uncap_first}ServiceImpl.getId(id);
		
		SystemmgtUser loginUser = (SystemmgtUser) session.getAttribute(Constant.LOGIN_SYSTEM_USER);
		try{
			/*保存日志*/
        	this.saveLog(loginUser.getId(), loginUser.getUsername()==null?loginUser.getLoginName():loginUser.getUsername(), "分页查询${entityName}", "分页查询${entityName}");
    	}catch(Exception e){
    		e.printStackTrace();
         	log.error(e.toString());
        }
		modelMap.put("${entityName?uncap_first}", ${entityName?uncap_first});
		return new ModelAndView("${ftlPath}/${entityName?uncap_first}_Detail",modelMap);
	}
	
	/**
	 * 分页查询${commentInfo}列表
	 * @param queryVo
	 * @param modelMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/pageQuery${entityName}")
	public ModelAndView pageQuery${entityName}(${entityName}QueryVo queryVo, ModelMap modelMap)throws Exception {
	
		SystemmgtUser loginUser = (SystemmgtUser) session.getAttribute(Constant.LOGIN_SYSTEM_USER);
		PageFinder<${entityName}> pageFinder = ${entityName?uncap_first}ServiceImpl.queryPage${entityName}(queryVo);
		try{
			/*保存日志*/
        	this.saveLog(loginUser.getId(), loginUser.getUsername()==null?loginUser.getLoginName():loginUser.getUsername(), "分页查询${entityName}", "分页查询${entityName}");
    	}catch(Exception e){
    		e.printStackTrace();
         	log.error(e.toString());
        }
		modelMap.put("pageFinder", pageFinder);
		return new ModelAndView("${ftlPath}/${entityName?uncap_first}_list",modelMap);
	}
}