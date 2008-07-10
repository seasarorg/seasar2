<#list tableDescList as table>
drop table ${table.fullName};
</#list>