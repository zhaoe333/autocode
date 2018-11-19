package ${packagePath}.${prex}.entity;

import com.cm.common.query.Query;
import io.swagger.annotations.ApiModelProperty;
/**
 * ${commentInfo}Entity类
 * @author ${author}
 * @date ${.now?date}
 */
public class ${entityName} implements java.io.Serializable {
	
	<#list columns as column>
	@ApiModelProperty("${column.comment?default('无')}")
	private ${column.type} ${column.name};
	</#list>
    
    public ${entityName}() {

    }

    <#list columns as column>
	public ${column.type} get${column.name?cap_first}() {
        return this.${column.name};
    }
    public void set${column.name?cap_first}(${column.type} ${column.name}) {
        this.${column.name} = ${column.name};
    }
	</#list>
}