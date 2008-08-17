<#list tableDescList as table>
  <#list table.foreignKeyDescList as foreignKey>
    <#assign constraintName>${table.name}_FK${foreignKey_index + 1}</#assign>
alter table ${quote(table.fullName)} add constraint ${constraintName} foreign key (<#list foreignKey.columnNameList as columnName>${quote(columnName)}<#if columnName_has_next>, </#if></#list>) references ${quote(foreignKey.referencedFullTableName)} (<#list foreignKey.referencedColumnNameList as columnName>${quote(columnName)}<#if columnName_has_next>, </#if></#list>)${delimiter}
  </#list>
</#list>