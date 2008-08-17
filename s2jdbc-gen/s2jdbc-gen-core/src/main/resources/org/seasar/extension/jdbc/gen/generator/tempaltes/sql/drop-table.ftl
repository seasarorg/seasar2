<#list tableDescList as table>
drop table ${quote(table.fullName)}${delimiter}
</#list>

drop table ${quote(schemaInfoFullTableName)}${delimiter}
