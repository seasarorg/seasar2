<#list tableDescList as table>
create table ${table.fullName} (<#list table.columnDescList as column>${column.name} ${column.definition}<#if !column.nullable> not null</#if><#if column_has_next>, </#if></#list>);
</#list>