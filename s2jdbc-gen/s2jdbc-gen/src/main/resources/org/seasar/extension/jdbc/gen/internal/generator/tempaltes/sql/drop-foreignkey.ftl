<#list tableDescList as table>
  <#list table.foreignKeyDescList as foreignKey>
    <#assign constraintName>${unquote(table.name)}_FK${foreignKey_index + 1}</#assign>
${convertKeyword("alter table")} ${convertIdentifier(table.fullName)} ${getDropForeignKeySyntax()} ${convertIdentifier(constraintName)}${delimiter}
  </#list>
</#list>