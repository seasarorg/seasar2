package ${packageName};

<#list importPackageNameSet as name>
import ${name};
</#list>

@MappedSuperclass
public abstract class ${shortClassName} {
<#list entityDesc.attributeDescList as attr>

  <#if attr.id>
    @Id
    <#if !entityDesc.hasCompositeId()>
    @GeneratedValue
    </#if>
  </#if>
  <#if attr.lob>
    @Lob
  </#if>
  <#if attr.temporalType??>
    @Temporal(TemporalType.${attr.temporalType})
  </#if>
  <#if attr.transient>
    @Transient
  </#if>
  <#if attr.version>
    @Version
  </#if>
    public ${attr.attributeClass.simpleName} ${attr.name};
</#list>
}