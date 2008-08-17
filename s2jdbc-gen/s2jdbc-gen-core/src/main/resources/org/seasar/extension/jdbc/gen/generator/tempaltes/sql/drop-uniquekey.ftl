<#list tableDescList as table>
  <#list table.uniqueKeyDescList as uniqueKey>
    <#assign constraintName>${table.name}_UK${uniqueKey_index + 1}</#assign>
alter table ${quote(table.fullName)} ${getDropUniqueKeySyntax()} ${constraintName}${delimiter}
  </#list>
</#list>