<#list tableDescList as table>
  <#list table.sequenceDescList as sequence>
drop sequence ${quote(sequence.sequenceName)};
  </#list>
</#list>