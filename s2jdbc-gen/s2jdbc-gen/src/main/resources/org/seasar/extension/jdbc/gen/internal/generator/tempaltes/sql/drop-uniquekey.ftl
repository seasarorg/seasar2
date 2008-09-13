<#list uniqueKeyModelList as uniqueKey>
${keyword("alter table")} ${name} ${uniqueKey.dropUniqueKeySyntax} ${uniqueKey.name}${delimiter}
</#list>
