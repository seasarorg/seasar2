<#list tableDescList as table>
  <#list table.uniqueKeyDescList as uniqueKey>
    <#assign constraintName>${unquote(table.name)}_UK${uniqueKey_index + 1}</#assign>
${convertKeyword("alter table")} ${convertIdentifier(table.fullName)} ${getDropUniqueKeySyntax()} ${convertIdentifier(constraintName)}${delimiter}
  </#list>
</#list>