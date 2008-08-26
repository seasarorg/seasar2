<#include "/copyright.ftl">
<#if packageName??>
package ${packageName};
</#if>

<#list importNameSet as importName>
import ${importName};
</#list>

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
<#list conditionMethodModelList as method>

    /**
     * ${method.name}の条件
     * 
     * @return ${method.name}の条件
     */
    public ${method.shortReturnClassName} ${method.name}() {
        return new ${method.shortReturnClassName}(prefix + "${method.name}.", where);
    } 
</#list>
}