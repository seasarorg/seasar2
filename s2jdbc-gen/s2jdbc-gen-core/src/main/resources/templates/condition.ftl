package ${getPackageName(className)};

<#list importPackageNameSet as packageName>
import ${packageName};
</#list>

public class ${getShortClassName(className)} extends
        ${getShortClassName(baseClassName)}<${getShortClassName(className)}> {

    public ${getShortClassName(className)}() {
    }

    public ${getShortClassName(className)}(String prefix, ComplexWhere where) {
        super(prefix, where);
    }
<#list entityDesc.attributeDescList as attr>

  <#if attr.getAttributeClass().getName() == "java.lang.String">
    <#if attr.isNullable()>
    public NullableStringCondition<${getShortClassName(className)}> ${attr.name} =
        new NullableStringCondition<${getShortClassName(className)}>("${attr.name}", this);
    <#else>
    public NotNullableStringCondition<${getShortClassName(className)}> ${attr.name} =
        new NotNullableStringCondition<${getShortClassName(className)}>("${attr.name}", this);
    </#if>
  <#else>
    <#if attr.isNullable()>
    public NullableCondition<${getShortClassName(className)}, ${getWrapperShortClassName(attr)}> ${attr.name} =
        new NullableCondition<${getShortClassName(className)}, ${getWrapperShortClassName(attr)}>("${attr.name}", this);
    <#else>
    public NotNullableCondition<${getShortClassName(className)}, ${getWrapperShortClassName(attr)}> ${attr.name} =
        new NotNullableCondition<${getShortClassName(className)}, ${getWrapperShortClassName(attr)}>("${attr.name}", this);
    </#if>
  </#if>
</#list>
}