package ${packagePath}.${prex}.query.vo;

import com.cm.common.query.Query;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * ${commentInfo}Vo类
 * @author ${author}
 * @createTime ${.now?date}
 */
@Data
public class ${entityName}Vo extends Query implements java.io.Serializable {
	
	<#list columns as column>
	<#if column.name != 'deleteFlag' && column.name != 'updateTime' && column.name != 'createTime'>
	@ApiModelProperty("${column.comment?default('无')}")
	private ${column.type} ${column.name};
	</#if>
	</#list>

}