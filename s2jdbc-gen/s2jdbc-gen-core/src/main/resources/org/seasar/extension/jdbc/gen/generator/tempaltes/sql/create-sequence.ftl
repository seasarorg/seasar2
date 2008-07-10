<#list sequenceDescList as sequence>
create sequence ${sequence.sequenceName} ${getSequenceDefinitionFragment(sequence)};
</#list>
