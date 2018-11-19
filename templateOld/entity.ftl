package ${packagePath}.model.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * ${commentInfo}Entity类
 * @author ${author}
 * @date ${.now?date}
 */
@Entity
@Table(name = "${tableName}")
public class ${entityName} implements java.io.Serializable {
	
	<#list columns as column>
	/**${column.comment?default('null')}*/
	private ${column.type} ${column.name};
	</#list>
    
    public ${entityName}() {

    }

    <#list columns as column>
		<#if column.isKey == 0>
	@GenericGenerator(name = "generator", strategy = "uuid.hex")
    @GeneratedValue(generator = "generator")
    @Id		
		</#if>
	@Column(name = "${column.columnName}",  <#if column.isKey == 0>unique = true,</#if> nullable = <#if column.isNullAble == 0>true<#else>false</#if>)	
	public ${column.type} get${column.name?cap_first}() {
        return this.${column.name};
    }
    public void set${column.name?cap_first}(${column.type} ${column.name}) {
        this.${column.name} = ${column.name};
    }
	</#list>
}