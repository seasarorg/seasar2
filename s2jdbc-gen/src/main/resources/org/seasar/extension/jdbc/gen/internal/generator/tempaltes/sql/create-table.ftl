${keyword("create table")} ${name} (
  <#list columnModelList as column>
    ${column.name} ${column.definition}<#if commentInCreateTableSupported> ${keyword("comment")} '${column.comment}'</#if><#if column_has_next || primaryKeyModel??>,</#if>
  </#list>
  <#if primaryKeyModel??>
    ${keyword("constraint")} ${primaryKeyModel.name} ${keyword("primary key")}(<#list primaryKeyModel.columnNameList as columnName>${columnName}<#if columnName_has_next>, </#if></#list>)
  </#if>
)<#if commentInCreateTableSupported> ${keyword("comment")} = '${comment}'</#if><#if tableOption??> ${tableOption}</#if>${delimiter}
<#if commentOnSupported>

${keyword("comment on table")} ${name} ${keyword("is")} '${comment}'${delimiter}
  <#list columnModelList as column>
${keyword("comment on column")} ${name}.${column.name} ${keyword("is")} '${column.comment}'${delimiter}
  </#list>
</#if>