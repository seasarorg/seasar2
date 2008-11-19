<#include "/copyright.ftl">
<#if packageName??>
package ${packageName};
</#if>

<#list importNameSet as importName>
import ${importName};
</#list>
<#if staticImportNameSet?size gt 0>

  <#list staticImportNameSet as importName>
import static ${importName};
  </#list>
</#if>

/**
 * {@link ${shortEntityClassName}}の条件クラスです。
 * 
 * @author S2JDBC-Gen
 */
public class ${shortClassName} extends
        AbstractEntityCondition<${shortClassName}> {

    /**
     * インスタンスを構築します。
     */
    public ${shortClassName}() {
    }

    /**
     * インスタンスを構築します。
     * 
     * @param prefix プレフィックス
     * @param where 検索条件
     */
    public ${shortClassName}(String prefix, ComplexWhere where) {
        super(prefix, where);
    }
<#list conditionAttributeModelList as attr>

    /** ${attr.name}の条件 */
    public ${attr.conditionClass.simpleName}<${shortClassName}<#if attr.parameterized>, ${attr.attributeClass.simpleName}</#if>> ${attr.name} =
        new ${attr.conditionClass.simpleName}<${shortClassName}<#if attr.parameterized>, ${attr.attributeClass.simpleName}</#if>>("${attr.name}", this);
</#list>
<#list conditionAssociationModelList as asso>

    /**
     * ${asso.name}の条件
     * 
     * @return ${asso.name}の条件
     */
    public ${asso.shortConditionClassName} ${asso.name}() {
        return new ${asso.shortConditionClassName}(prefix + "${asso.name}.", where);
    } 
</#list>
}