<#list sequenceDescList as sequence>
drop sequence ${sequence.sequenceName}${delimiter}
</#list>
