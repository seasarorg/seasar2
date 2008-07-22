<#list tableDescList as table>
  <#list table.uniqueKeyDescList as uniqueKey>
    <#assign constraintName>${table.name}_UK${uniqueKey_index + 1}</#assign>
alter table ${table.fullName} drop constraint ${constraintName}${delimiter}
  </#list>
</#list>