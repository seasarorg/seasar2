<#list tableDescList as table>
drop table ${getQuotedTableName(table)};
</#list>