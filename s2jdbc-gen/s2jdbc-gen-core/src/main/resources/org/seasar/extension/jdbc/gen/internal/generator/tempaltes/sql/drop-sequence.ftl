<#list sequenceDescList as sequence>
drop sequence ${quote(sequence.sequenceName)}${delimiter}
</#list>
