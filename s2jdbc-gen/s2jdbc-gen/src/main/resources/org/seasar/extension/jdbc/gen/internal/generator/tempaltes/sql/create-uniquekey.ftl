<#list uniqueKeyModelList as uniqueKey>
${keyword("alter table")} ${name} ${keyword("add constraint")} ${uniqueKey.name} ${keyword("unique")} (<#list uniqueKey.columnNameList as columnName>${columnName}<#if columnName_has_next>, </#if></#list>)${delimiter}
</#list>
