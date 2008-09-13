<#list foreignKeyModelList as foreignKey>
${keyword("alter table")} ${name} ${foreignKey.dropForeignKeySyntax} ${foreignKey.name}${delimiter}
</#list>
