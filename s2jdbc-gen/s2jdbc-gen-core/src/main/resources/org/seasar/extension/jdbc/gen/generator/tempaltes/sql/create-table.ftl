<#list tableDescList as table>
  <#if table.primaryKeyDesc??>
    <#assign constraintName>${table.name}_PK</#assign>
  </#if>
create table ${table.fullName} (<#list table.columnDescList as column>${column.name} ${column.definition}<#if column.identity> ${getIdentityColumnDefinition()}<#else><#if !column.nullable> not null</#if></#if><#if column_has_next>, </#if></#list><#if table.primaryKeyDesc??>, constraint ${constraintName} primary key(<#list table.primaryKeyDesc.columnNameList as columnName>${columnName}<#if columnName_has_next>, </#if></#list>)</#if>)<#if tableOption??> ${tableOption}</#if>${delimiter}
</#list>

create table ${schemaInfoFullTableName} (${schemaInfoColumnName} ${schemaInfoColumnDefinition})${delimiter}
insert into ${schemaInfoFullTableName} (${schemaInfoColumnName}) values (${versionNo})${delimiter}
