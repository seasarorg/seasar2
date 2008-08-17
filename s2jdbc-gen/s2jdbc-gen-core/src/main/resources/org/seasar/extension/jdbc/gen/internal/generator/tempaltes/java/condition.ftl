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
public class ${shortClassName} extends
        AbstractEntityCondition<${shortClassName}> {

    /**
     *
     */
    public ${shortClassName}() {
    }

    /**
     * 
     * @param prefix
     * @param where
     */
    public ${shortClassName}(String prefix, ComplexWhere where) {
        super(prefix, where);
    }
<#list conditionAttributeModelList as attr>

    /** */
    public ${attr.conditionClass.simpleName}<${shortClassName}<#if attr.parameterized>, ${attr.attributeClass.simpleName}</#if>> ${attr.name} =
        new ${attr.conditionClass.simpleName}<${shortClassName}<#if attr.parameterized>, ${attr.attributeClass.simpleName}</#if>>("${attr.name}", this);
</#list>
<#list conditionMethodModelList as method>

    /**
     *
     */
    public ${method.shortReturnClassName} ${method.name}() {
        return new ${method.shortReturnClassName}(prefix + "${method.name}.", where);
    } 
</#list>
}