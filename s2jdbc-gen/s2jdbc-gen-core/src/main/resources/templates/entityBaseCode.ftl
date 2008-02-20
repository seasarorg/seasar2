package ${packageName};

<#list importPackageNames as name>
import ${name};
</#list>

@MappedSuperclass
public abstract class ${shortClassName} {
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