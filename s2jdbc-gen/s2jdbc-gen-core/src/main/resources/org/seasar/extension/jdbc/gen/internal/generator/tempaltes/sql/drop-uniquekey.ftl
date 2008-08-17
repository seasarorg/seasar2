<#list tableDescList as table>
  <#list table.uniqueKeyDescList as uniqueKey>
    <#assign constraintName>${unquote(table.name)}_UK${uniqueKey_index + 1}</#assign>
alter table ${table.fullName} ${getDropUniqueKeySyntax()} ${constraintName}${delimiter}
  </#list>
</#list>