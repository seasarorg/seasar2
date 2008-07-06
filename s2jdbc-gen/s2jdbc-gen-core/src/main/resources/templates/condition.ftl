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
<#assign lastIndex = entityMeta.propertyMetaSize - 1>
<#list 0..lastIndex as i>
  <#assign prop = entityMeta.getPropertyMeta(i)>

  <#if prop.getPropertyClass().getName() == "java.lang.String">
    <#if isNullable(prop)>
    public NullableStringCondition<${getShortClassName(className)}> ${prop.name} =
        new NullableStringCondition<${getShortClassName(className)}>("${prop.name}", this);
    <#else>
    public NotNullableStringCondition<${getShortClassName(className)}> ${prop.name} =
        new NotNullableStringCondition<${getShortClassName(className)}>("${prop.name}", this);
    </#if>
  <#else>
    <#if isNullable(prop)>
    public NullableCondition<${getShortClassName(className)}, ${getWrapperShortClassName(prop)}> ${prop.name} =
        new NullableCondition<${getShortClassName(className)}, ${getWrapperShortClassName(prop)}>("${prop.name}", this);
    <#else>
    public NotNullableCondition<${getShortClassName(className)}, ${getWrapperShortClassName(prop)}> ${prop.name} =
        new NotNullableCondition<${getShortClassName(className)}, ${getWrapperShortClassName(prop)}>("${prop.name}", this);
    </#if>
  </#if>
</#list>
}