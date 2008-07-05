--Foreign Key
<#list tableDescList as table>
  <#list table.foreignKeyDescList as foreignKey>
  <#assign constraintName>${table.name}_FK${foreignKey_index + 1}</#assign>
alter table ${getQuotedTableName(table)} drop constraint ${quote(constraintName)};
  </#list>
</#list>

--Unique Key
<#list tableDescList as table>
  <#list table.uniqueKeyDescList as uniqueKey>
  <#assign constraintName>${table.name}_UK${uniqueKey_index + 1}</#assign>
alter table ${getQuotedTableName(table)} drop constraint ${quote(constraintName)};
  </#list>
</#list>

--Primary Key
<#list tableDescList as table>
<#if table.primaryKeyDesc??>
<#assign constraintName>${table.name}_PK</#assign>
alter table ${getQuotedTableName(table)} drop constraint ${quote(constraintName)};
</#if>
</#list>