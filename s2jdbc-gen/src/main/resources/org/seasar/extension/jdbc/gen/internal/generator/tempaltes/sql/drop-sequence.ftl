<#list sequenceModelList as sequence>
drop sequence ${sequence.name}${delimiter}
</#list>
