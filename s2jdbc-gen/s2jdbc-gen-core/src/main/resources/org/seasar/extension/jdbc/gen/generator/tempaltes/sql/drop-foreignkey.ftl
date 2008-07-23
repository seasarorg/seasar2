<#list tableDescList as table>
  <#list table.foreignKeyDescList as foreignKey>
    <#assign constraintName>${table.name}_FK${foreignKey_index + 1}</#assign>
alter table ${table.fullName} ${getDropForeignKeySyntax()} ${constraintName}${delimiter}
  </#list>
</#list>