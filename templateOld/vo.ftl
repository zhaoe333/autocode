package ${packagePath}.model.vo;

/**
 * ${commentInfo}Vo类
 * @author ${author}
 * @date ${.now?date}
 */
public class ${entityName}Vo implements java.io.Serializable {
	
	<#list columns as column>
	// ${column.comment?default('null')}
	private ${column.type} ${column.name};
	</#list>
    
    public ${entityName}Vo() {

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