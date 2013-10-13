<#list sequenceModelList as sequence>
${keyword("create sequence")} ${sequence.name} ${sequence.definition}${delimiter}
</#list>
