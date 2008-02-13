package ${package};

<#list imports as import>
import ${import};
</#list>

@MappedSuperclass
public abstract class ${gapClassName} {
<#list entityModel.propertyModelList as p>

  <#if p.id>
    @Id
    @GeneratedValue
  </#if>
  <#if p.lob>
    @Lob
  </#if>
  <#if p.temporalType??>
    @Temporal(TemporalType.${p.temporalType})
  </#if>
  <#if p.transient>
    @Transient
  </#if>
  <#if p.version>
    @Version
  </#if>
    public ${p.propertyClass.simpleName} ${p.name};
</#list>
}