<#list sequenceDescList as sequence>
create sequence ${quote(sequence.sequenceName)} ${getSequenceDefinitionFragment(sequence)}${delimiter}
</#list>
