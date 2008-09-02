<#list sequenceDescList as sequence>
${convertKeyword("create sequence")} ${convertIdentifier(sequence.sequenceName)} ${getSequenceDefinitionFragment(sequence)}${delimiter}
</#list>
