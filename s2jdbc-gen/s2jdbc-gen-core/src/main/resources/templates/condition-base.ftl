package ${packageName};

<#list importPackageNameSet as name>
import ${name};
</#list>

public abstract class ${shortClassName} extends
        ${shortBaseClassName}<${shortClassName}> {

    public ${shortClassName}() {
    }

    public ${shortClassName}(String prefix, ComplexWhere where) {
        super(prefix, where);
    }
<#list entityDesc.attributeDescList as attr>

  <#if attr.getAttributeClass().getName() == "java.lang.String">
    <#if attr.isNullable()>
    public NullableStringCondition<${shortClassName}> ${attr.name} =
        new NullableStringCondition<${shortClassName}>("${attr.name}", this);
    <#else>
    public NotNullableStringCondition<${shortClassName}> ${attr.name} =
        new NotNullableStringCondition<${shortClassName}>("${attr.name}", this);
    </#if>
  <#else>
    <#if attr.isNullable()>
    public NullableCondition<${shortClassName}, ${getSimpleClassName(attr)}> ${attr.name} =
        new NullableCondition<${shortClassName}, ${getSimpleClassName(attr)}>("${attr.name}", this);
    <#else>
    public NotNullableCondition<${shortClassName}, ${getSimpleClassName(attr)}> ${attr.name} =
        new NotNullableCondition<${shortClassName}, ${getSimpleClassName(attr)}>("${attr.name}", this);
    </#if>
  </#if>
</#list>
}