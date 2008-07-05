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
    public NullableCondition<${shortClassName}, ${attr.getAttributeClassAsRefType().getSimpleName()}> ${attr.name} =
        new NullableCondition<${shortClassName}, ${attr.getAttributeClassAsRefType().getSimpleName()}>("${attr.name}", this);
    <#else>
    public NotNullableCondition<${shortClassName}, ${attr.getAttributeClassAsRefType().getSimpleName()}> ${attr.name} =
        new NotNullableCondition<${shortClassName}, ${attr.getAttributeClassAsRefType().getSimpleName()}>("${attr.name}", this);
    </#if>
  </#if>
</#list>
}