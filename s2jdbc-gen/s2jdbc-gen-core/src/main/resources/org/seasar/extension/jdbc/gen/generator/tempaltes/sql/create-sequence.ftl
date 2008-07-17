<#list sequenceDescList as sequence>
create sequence ${sequence.sequenceName} ${getSequenceDefinitionFragment(sequence)}${delimiter}
</#list>
