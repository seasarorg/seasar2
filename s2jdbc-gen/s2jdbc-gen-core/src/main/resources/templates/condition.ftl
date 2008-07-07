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
<#list conditionAttributeModelList as attr>

  <#if attr.attributeClass.name == "java.lang.String">
    public ${attr.conditionClass.simpleName}<${getShortClassName(className)}> ${attr.name} =
        new ${attr.conditionClass.simpleName}<${getShortClassName(className)}>("${attr.name}", this);
  <#else>
    public ${attr.conditionClass.simpleName}<${getShortClassName(className)}, ${attr.attributeClass.simpleName}> ${attr.name} =
        new ${attr.conditionClass.simpleName}<${getShortClassName(className)}, ${attr.attributeClass.simpleName}>("${attr.name}", this);
  </#if>
</#list>
<#list conditionMethodModelList as method>

    public ${method.returnShortClassName} ${method.name}() {
        return new ${method.returnShortClassName}(prefix + "${method.name}.", where);
    } 
</#list>
}