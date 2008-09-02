<#list sequenceDescList as sequence>
drop sequence ${convertIdentifier(sequence.sequenceName)}${delimiter}
</#list>
