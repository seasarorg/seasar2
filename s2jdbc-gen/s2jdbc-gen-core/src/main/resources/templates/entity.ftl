package ${packageName};

<#list importPackageNameSet as name>
import ${name};
</#list>

@Entity
<#if tableQualified>
@Table(<#if entityDesc.catalogName??>catalog = "${entityDesc.catalogName}"</#if><#if entityDesc.schemaName??><#if entityDesc.catalogName??>, </#if>schema = "${entityDesc.schemaName}"</#if>)
</#if>
public class ${shortClassName} extends ${shortBaseClassName} {
}