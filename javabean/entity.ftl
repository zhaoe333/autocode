package ${bean.path};

import java.util.List;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * 
 * @author yunlu
 * @date ${.now?date}
 */
public class ${bean.name} implements java.io.Serializable {
	
	<#list pros as pro>
	/**${pro.comment?default('  ')}*/
	<#if pro.ann??>
	${pro.ann}
	</#if>
	private ${pro.type} ${pro.name};
	</#list>
    
    <#list pros as pro>
	public ${pro.type} get${pro.name?cap_first}() {
        return this.${pro.name};
    }
    public void set${pro.name?cap_first}(${pro.type} ${pro.name}) {
        this.${pro.name} = ${pro.name};
    }
	</#list>
}