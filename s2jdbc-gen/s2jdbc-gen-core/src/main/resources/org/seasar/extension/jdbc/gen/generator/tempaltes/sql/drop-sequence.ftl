<#list sequenceDescList as sequence>
drop sequence ${sequence.sequenceName};
</#list>
