<#list tableDescList as table>
create table ${getQuotedTableName(table)} (<#list table.columnDescList as column>${quote(column.name)} ${column.definition}<#if !column.nullable> not null</#if><#if column_has_next>, </#if></#list>);
</#list>