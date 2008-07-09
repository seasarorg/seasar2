<#list sequenceDescList as sequence>
create sequence ${quote(sequence.sequenceName)} ${getSequenceDefinitionFragment(sequence)};
</#list>
