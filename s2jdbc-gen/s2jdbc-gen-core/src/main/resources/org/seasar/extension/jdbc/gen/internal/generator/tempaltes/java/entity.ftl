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
<#if catalogName?? || schemaName?? || compositeUniqueConstraintModelList?size gt 0>
@Table(<#if catalogName??>catalog = "${catalogName}"</#if><#if schemaName??><#if catalogName??>, </#if>schema = "${schemaName}"</#if><#if compositeUniqueConstraintModelList?size gt 0><#if catalogName?? || schemaName??>, </#if>uniqueConstraints = { <#list compositeUniqueConstraintModelList as uniqueConstraint>@UniqueConstraint(columnNames = { <#list uniqueConstraint.columnNameList as columnName>"${columnName}"<#if columnName_has_next>, </#if></#list> })<#if uniqueConstraint_has_next>, </#if></#list> }</#if>)
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
    @Column(columnDefinition = <#if attr.columnDefinition??>"${attr.columnDefinition}"<#else>null</#if>, nullable = ${attr.nullable?string}, unique = ${attr.unique?string})
  </#if>
    public ${attr.attributeClass.simpleName} ${attr.name};
</#list>
<#list associationModelList as asso>
    
    /** */
    @${asso.associationType.annotation.simpleName}<#if asso.mappedBy??>(mappedBy = "${asso.mappedBy}")</#if>
  <#if asso.joinColumnModel??>
    @JoinColumn(name = "${asso.joinColumnModel.name}", referencedColumnName = "${asso.joinColumnModel.referencedColumnName}")
  <#else>
    <#if asso.joinColumnModelList?size gt 0>
    @JoinColumns( {
      <#list asso.joinColumnModelList as joinColumnModel>
        @JoinColumn(name = "${joinColumnModel.name}", referencedColumnName = "${joinColumnModel.referencedColumnName}")<#if joinColumnModel_has_next>,<#else> })</#if>
      </#list>
    </#if>
  </#if>
    public ${asso.shortClassName} ${asso.name};
</#list>
}