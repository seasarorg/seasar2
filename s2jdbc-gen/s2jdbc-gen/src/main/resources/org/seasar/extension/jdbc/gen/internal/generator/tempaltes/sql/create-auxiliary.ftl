<#if dialect.name == "postgre">
  <#list columnModelList as column>
    <#assign columnDef>${column.definition?trim?lower_case}</#assign>
    <#if columnDef?starts_with("serial") || columnDef?starts_with("bigserial")>
      <#assign seqName>${name}_${column.name}_seq</#assign>
${keyword("select setval")}('${identifier(seqName)}', ${keyword("max")}(${column.name})) ${keyword("from")} ${name};
    </#if>
  </#list>
</#if>