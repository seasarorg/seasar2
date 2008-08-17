<#include "/copyright.ftl">
<#if packageName??>
package ${packageName};
</#if>

<#list importNameSet as importName>
import ${importName};
</#list>

/**
 * 
 * @author S2JDBC-Gen
 */
@Entity
<#if catalogName?? || schemaName??>
@Table(<#if catalogName??>catalog = "${catalogName}"</#if><#if schemaName??><#if catalogName??>, </#if>schema = "${schemaName}"</#if>)
</#if>
public class ${shortClassName} {
<#list attributeModelList as attr>

  <#if attr.transient || attr.columnDefinition??>
    /** */
  <#else>
    /**
     * FIXME このプロパティに対応するカラムの型(${attr.columnTypeName})はサポート対象外です。
     */
  </#if>
  <#if attr.id>
    @Id
    <#if !hasCompositeId()>
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
  <#if !attr.transient>
    @Column(columnDefinition = <#if attr.columnDefinition??>"${attr.columnDefinition}"<#else>null</#if>, nullable = ${attr.nullable?string})
  </#if>
    public ${attr.attributeClass.simpleName} ${attr.name};
</#list>
}