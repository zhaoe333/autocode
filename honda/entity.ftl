package ${packagePath}.${prex}.entity;

import com.cm.common.query.Query;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * ${commentInfo}Entity类
 * @author ${author}
 * @createTime ${.now?date}
 */
@Data
public class ${entityName} implements java.io.Serializable {
	
	<#list columns as column>
	@ApiModelProperty("${column.comment?default('无')}")
	private ${column.type} ${column.name};
	</#list>

}