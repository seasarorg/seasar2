<#list tableDescList as table>
  <#list table.uniqueKeyDescList as uniqueKey>
    <#assign constraintName>${table.name}_UK${uniqueKey_index + 1}</#assign>
alter table ${quote(table.fullName)} add constraint ${constraintName} unique (<#list uniqueKey.columnNameList as columnName>${quote(columnName)}<#if columnName_has_next>, </#if></#list>)${delimiter}
  </#list>
</#list>