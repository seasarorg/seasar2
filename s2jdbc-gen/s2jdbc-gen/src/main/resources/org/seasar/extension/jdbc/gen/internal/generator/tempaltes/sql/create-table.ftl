${keyword("create table")} ${name} (
  <#list columnModelList as column>
    ${column.name} ${column.definition}<#if column_has_next || primaryKeyModel??>,</#if>
  </#list>
  <#if primaryKeyModel??>
    ${keyword("constraint")} ${primaryKeyModel.name} ${keyword("primary key")}(<#list primaryKeyModel.columnNameList as columnName>${columnName}<#if columnName_has_next>, </#if></#list>)
  </#if>
)<#if tableOption??> ${tableOption}</#if>${delimiter}
