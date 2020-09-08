package ${packagePath}.${prex}.query;

import com.cm.common.query.Query;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * ${commentInfo}QueryVo类
 * @author ${author}
 * @createTime ${.now?date}
 */
@Data
public class ${entityName}Query extends Query {

	<#list columns as column>
	@ApiModelProperty("${column.comment?default('无')}")
	private ${column.type} ${column.name};
	</#list>

}