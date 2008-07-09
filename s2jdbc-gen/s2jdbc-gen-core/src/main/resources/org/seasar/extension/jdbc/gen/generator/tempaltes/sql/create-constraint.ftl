--Primary Key
<#list tableDescList as table>
<#if table.primaryKeyDesc??>
<#assign constraintName>${table.name}_PK</#assign>
alter table ${getQuotedTableName(table)} add constraint ${quote(constraintName)} primary key (<#list table.primaryKeyDesc.columnNameList as columnName>${quote(columnName)}<#if columnName_has_next>, </#if></#list>);
</#if>
</#list>

--Unique Key
<#list tableDescList as table>
  <#list table.uniqueKeyDescList as uniqueKey>
  <#assign constraintName>${table.name}_UK${uniqueKey_index + 1}</#assign>
alter table ${getQuotedTableName(table)} add constraint ${quote(constraintName)} unique (<#list uniqueKey.columnNameList as columnName>${quote(columnName)}<#if columnName_has_next>, </#if></#list>);
  </#list>
</#list>

--Foreign Key
<#list tableDescList as table>
  <#list table.foreignKeyDescList as foreignKey>
  <#assign constraintName>${table.name}_FK${foreignKey_index + 1}</#assign>
alter table ${getQuotedTableName(table)} add constraint ${quote(constraintName)} foreign key (<#list foreignKey.columnNameList as columnName>${quote(columnName)}<#if columnName_has_next>, </#if></#list>) references ${getQuotedReferencedTableName(foreignKey)} (<#list foreignKey.referencedColumnNameList as columnName>${quote(columnName)}<#if columnName_has_next>, </#if></#list>);
  </#list>
</#list>