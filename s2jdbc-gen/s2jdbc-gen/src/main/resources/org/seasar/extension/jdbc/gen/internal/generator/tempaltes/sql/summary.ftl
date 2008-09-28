/*-----------------------------------------------*/
/* drop                                          */
/*-----------------------------------------------*/
-- drop foreign key
<#list tableModelList as tableModel>
<@include name=dropForeignKeyTemplateFileName rootModel=tableModel/>
</#list>

-- drop sequence
<#list tableModelList as tableModel>
<@include name=dropSequenceTemplateFileName rootModel=tableModel/>
</#list>

-- drop unique key
<#list tableModelList as tableModel>
<@include name=dropUniqueKeyTemplateFileName rootModel=tableModel/>
</#list>

-- drop table
<#list tableModelList as tableModel>
<@include name=dropTableTemplateFileName rootModel=tableModel/>
</#list>

-- drop schema_info table
<@include name=dropSchemaInfoTableTemplateFileName rootModel=schemaInfoTableModel/>

/*-----------------------------------------------*/
/* create                                        */
/*-----------------------------------------------*/
-- create table
<#list tableModelList as tableModel>
<@include name=createTableTemplateFileName rootModel=tableModel/>
</#list>

-- create schema_info table
<@include name=createSchemaInfoTableTemplateFileName rootModel=schemaInfoTableModel/>

-- create unique key
<#list tableModelList as tableModel>
<@include name=createUniqueKeyTemplateFileName rootModel=tableModel/>
</#list>

-- create sequence
<#list tableModelList as tableModel>
<@include name=createSequenceTemplateFileName rootModel=tableModel/>
</#list>

-- create foreign key
<#list tableModelList as tableModel>
<@include name=createForeignKeyTemplateFileName rootModel=tableModel/>
</#list>

