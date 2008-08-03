<#list columnNameList as columnName>${columnName}<#if columnName_has_next>${delimiter}</#if></#list>
<#list rowList as row>
<#list row as columnValue><#if columnValue??>${columnValue}</#if><#if columnValue_has_next>${delimiter}</#if></#list>
</#list>