<#list entitys as entity>
<typeAlias type="com.cm.vehicle.entity.${entity}" alias="${entity?lower_case}" />
</#list>

<#list entitys as entity>
<typeAlias type="com.cm.vehicle.query.${entity}Query" alias="${entity?lower_case}query" />
</#list>