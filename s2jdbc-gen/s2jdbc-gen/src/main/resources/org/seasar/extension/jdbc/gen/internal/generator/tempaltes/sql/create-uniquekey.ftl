<#list tableDescList as table>
  <#list table.uniqueKeyDescList as uniqueKey>
    <#assign constraintName>${unquote(table.name)}_UK${uniqueKey_index + 1}</#assign>
${convertKeyword("alter table")} ${convertIdentifier(table.fullName)} ${convertKeyword("add constraint")} ${convertIdentifier(constraintName)} ${convertKeyword("unique")} (<#list uniqueKey.columnNameList as columnName>${convertIdentifier(columnName)}<#if columnName_has_next>, </#if></#list>)${delimiter}
  </#list>
</#list>