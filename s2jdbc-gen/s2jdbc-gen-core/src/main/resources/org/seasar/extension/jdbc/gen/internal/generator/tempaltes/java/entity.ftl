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
<#list associationModelList as asso>
    
    @${asso.associationType.annotation.simpleName}
  <#if asso.joinColumnModel??>
    @JoinColumn(name = "${asso.joinColumnModel.name}", referencedColumnName = "${asso.joinColumnModel.referencedColumnName}")
  <#else>
    <#list asso.joinColumnModelList as joinColumnModel>
    @JoinColumns( {
        @JoinColumn(name = "${joinColumnModel.name}", referencedColumnName = "${joinColumnModel.referencedColumnName}")<#if joinColumnModel_has_next>,<#else> })</#if>
    </#list>
  </#if>
    public ${asso.shortClassName} ${asso.name};
</#list>
<#list inverseAssociationModelList as asso>
    
    @${asso.associationType.annotation.simpleName}(<#if asso.mappedBy??>mappedBy = "${asso.mappedBy}"</#if>)
    public ${asso.shortClassName} ${asso.name};
</#list>
}