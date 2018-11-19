package ${packagePath}.${prex}.query;

import com.cm.common.query.Query;
import io.swagger.annotations.ApiModelProperty;

/**
 * ${commentInfo}QueryVo类
 * @author ${author}
 * @date ${.now?date}
 */
public class ${entityName}Query extends Query {

	<#list columns as column>
	@ApiModelProperty("${column.comment?default('无')}")
	private ${column.type} ${column.name};
	</#list>

	<#list columns as column>
	public ${column.type} get${column.name?cap_first}() {
        return this.${column.name};
    }
    public void set${column.name?cap_first}(${column.type} ${column.name}) {
        this.${column.name} = ${column.name};
    }
	</#list>
}