--Primary Key
<#list tableDescList as table>
  <#if table.primaryKeyDesc??>
    <#assign constraintName>${table.name}_PK</#assign>
alter table ${table.fullName} add constraint ${constraintName} primary key (<#list table.primaryKeyDesc.columnNameList as columnName>${columnName}<#if columnName_has_next>, </#if></#list>)${delimiter}
  </#if>
</#list>

--Unique Key
<#list tableDescList as table>
  <#list table.uniqueKeyDescList as uniqueKey>
    <#assign constraintName>${table.name}_UK${uniqueKey_index + 1}</#assign>
alter table ${table.fullName} add constraint ${constraintName} unique (<#list uniqueKey.columnNameList as columnName>${columnName}<#if columnName_has_next>, </#if></#list>)${delimiter}
  </#list>
</#list>

--Foreign Key
<#list tableDescList as table>
  <#list table.foreignKeyDescList as foreignKey>
    <#assign constraintName>${table.name}_FK${foreignKey_index + 1}</#assign>
alter table ${table.fullName} add constraint ${constraintName} foreign key (<#list foreignKey.columnNameList as columnName>${columnName}<#if columnName_has_next>, </#if></#list>) references ${foreignKey.referencedFullTableName} (<#list foreignKey.referencedColumnNameList as columnName>${columnName}<#if columnName_has_next>, </#if></#list>)${delimiter}
  </#list>
</#list>