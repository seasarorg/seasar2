<#list tableDescList as table>
${convertKeyword("drop table")} ${convertIdentifier(table.fullName)}${delimiter}
</#list>

${convertKeyword("drop table")} ${convertIdentifier(schemaInfoFullTableName)}${delimiter}
