package ${packagePath}.${prex}.query.vo;

import com.cm.common.query.Query;
/**
 * ${commentInfo}Vo类
 * @author ${author}
 * @date ${.now?date}
 */
public class ${entityName}Vo  extends Query implements java.io.Serializable {
	
	<#list columns as column>
	// ${column.comment?default('null')}
	<#if column.name != 'deleteFlag'>
		private ${column.type} ${column.name};
	</#if>
	</#list>
    
    public ${entityName}Vo() {

    }
    <#list columns as column>
    <#if column.name != 'deleteFlag'>
	public ${column.type} get${column.name?cap_first}() {
        return this.${column.name};
    }
    public void set${column.name?cap_first}(${column.type} ${column.name}) {
        this.${column.name} = ${column.name};
    }
    </#if>
	</#list>
}