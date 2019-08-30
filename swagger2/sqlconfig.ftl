<#list entitys as entity>
<typeAlias type="${entity.packagePath}.${entity.prex}.entity.${entity.entityName}" alias="${entity.entityName?lower_case}" />
</#list>

<#list entitys as entity>
<typeAlias type="${entity.packagePath}.${entity.prex}.query.${entity.entityName}Query" alias="${entity.entityName?lower_case}query" />
</#list>