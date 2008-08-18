<#list tableDescList as table>
  <#if table.primaryKeyDesc??>
    <#assign constraintName>${unquote(table.name)}_PK</#assign>
  </#if>
${convertKeyword("create table")} ${convertIdentifier(table.fullName)} (<#list table.columnDescList as column>${convertIdentifier(column.name)} ${convertKeyword(column.definition)}<#if column.identity> ${getIdentityColumnDefinition()}<#else><#if !column.nullable> ${convertKeyword("not null")}</#if></#if><#if column_has_next>, </#if></#list><#if table.primaryKeyDesc??>, ${convertKeyword("constraint")} ${convertIdentifier(constraintName)} ${convertKeyword("primary key")}(<#list table.primaryKeyDesc.columnNameList as columnName>${convertIdentifier(columnName)}<#if columnName_has_next>, </#if></#list>)</#if>)<#if tableOption??> ${convertKeyword(tableOption)}</#if>${delimiter}
</#list>

${convertKeyword("create table")} ${convertIdentifier(schemaInfoFullTableName)} (${convertIdentifier(schemaInfoColumnName)} ${convertKeyword(schemaInfoColumnDefinition)})${delimiter}
${convertKeyword("insert into")} ${convertIdentifier(schemaInfoFullTableName)} (${convertIdentifier(schemaInfoColumnName)}) ${convertKeyword("values")} (${versionNo})${delimiter}
