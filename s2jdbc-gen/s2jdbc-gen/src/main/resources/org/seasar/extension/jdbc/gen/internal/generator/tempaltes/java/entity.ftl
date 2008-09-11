<#include "/copyright.ftl">
<#if packageName??>
package ${packageName};
</#if>

<#list importNameSet as importName>
import ${importName};
</#list>

/**
 * ${shortClassName}エンティティクラスです。
 * 
 * @author S2JDBC-Gen
 */
@Entity
<#if catalogName?? || schemaName?? || tableName?? || compositeUniqueConstraintModelList?size gt 0>
@Table(<#if catalogName??>catalog = "${catalogName}"</#if><#if schemaName??><#if catalogName??>, </#if>schema = "${schemaName}"</#if><#if tableName??><#if catalogName?? || schemaName??>, </#if>name = "${tableName}"</#if><#if compositeUniqueConstraintModelList?size gt 0><#if catalogName?? || schemaName?? || tableName??>, </#if>uniqueConstraints = { <#list compositeUniqueConstraintModelList as uniqueConstraint>@UniqueConstraint(columnNames = { <#list uniqueConstraint.columnNameList as columnName>"${columnName}"<#if columnName_has_next>, </#if></#list> })<#if uniqueConstraint_has_next>, </#if></#list> }</#if>)
</#if>
public class ${shortClassName} {
<#list attributeModelList as attr>

  <#if attr.unsupportedColumnType>
    /**
     * FIXME このプロパティに対応するカラムの型(${attr.columnTypeName})はサポート対象外です。
     */
  <#else>
    /** ${attr.name}プロパティ */
  </#if>
  <#if attr.id>
    @Id
    <#if attr.generationType??>
    @GeneratedValue(strategy = GenerationType.${attr.generationType}<#if attr.generationType?matches("SEQUENCE|TABLE")>, generator = "generator"</#if>)
      <#if attr.generationType == "SEQUENCE">
    @SequenceGenerator(name = "generator", initialValue = ${attr.initialValue}, allocationSize = ${attr.allocationSize})
      <#elseif attr.generationType == "TABLE">
    @TableGenerator(name = "generator", initialValue = ${attr.initialValue}, allocationSize = ${attr.allocationSize})
      </#if>
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
    @Column(<#if attr.columnName??>name = "${attr.columnName}", </#if><#if attr.columnDefinition??>columnDefinition = "${attr.columnDefinition}", <#else><#if attr.length??>length = ${attr.length}, </#if><#if attr.precision??>precision = ${attr.precision}, </#if><#if attr.scale??>scale = ${attr.scale}, </#if></#if>nullable = ${attr.nullable?string}, unique = ${attr.unique?string})
  </#if>
    public ${attr.attributeClass.simpleName} ${attr.name};
</#list>
<#list associationModelList as asso>

    /** ${asso.name}関連プロパティ */
    @${asso.associationType.annotation.simpleName}<#if asso.mappedBy??>(mappedBy = "${asso.mappedBy}")</#if>
  <#if asso.joinColumnModel??>
    @JoinColumn(name = "${asso.joinColumnModel.name}", referencedColumnName = "${asso.joinColumnModel.referencedColumnName}")
  <#elseif asso.joinColumnsModel??>
    @JoinColumns( {
    <#list asso.joinColumnsModel.joinColumnModelList as joinColumnModel>
        @JoinColumn(name = "${joinColumnModel.name}", referencedColumnName = "${joinColumnModel.referencedColumnName}")<#if joinColumnModel_has_next>,<#else> })</#if>
    </#list>
  </#if>
    public ${asso.shortClassName} ${asso.name};
</#list>
}