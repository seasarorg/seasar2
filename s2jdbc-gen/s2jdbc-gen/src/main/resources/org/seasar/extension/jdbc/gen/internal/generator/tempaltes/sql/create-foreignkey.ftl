<#list tableDescList as table>
  <#list table.foreignKeyDescList as foreignKey>
    <#assign constraintName>${unquote(table.name)}_FK${foreignKey_index + 1}</#assign>
${convertKeyword("alter table")} ${convertIdentifier(table.fullName)} ${convertKeyword("add constraint")} ${convertIdentifier(constraintName)} ${convertKeyword("foreign key")} (<#list foreignKey.columnNameList as columnName>${convertIdentifier(columnName)}<#if columnName_has_next>, </#if></#list>) ${convertKeyword("references")} ${convertIdentifier(foreignKey.referencedFullTableName)} (<#list foreignKey.referencedColumnNameList as columnName>${convertIdentifier(columnName)}<#if columnName_has_next>, </#if></#list>)${delimiter}
  </#list>
</#list>