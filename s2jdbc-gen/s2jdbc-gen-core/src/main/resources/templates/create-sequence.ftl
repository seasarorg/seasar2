<#list tableDescList as table>
  <#list table.sequenceDescList as sequence>
create sequence ${quote(sequence.sequenceName)} ${getSequenceDefinitionFragment(sequence)};
  </#list>
</#list>