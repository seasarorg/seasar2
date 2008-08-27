<#include "/copyright.ftl">
<#if packageName??>
package ${packageName};
</#if>

<#list importNameSet as importName>
import ${importName};
</#list>

/**
 * {@link ${shortEntityClassName}}のプロパティ名の集合です。
 * 
 * @author S2JDBC-Gen
 */
public interface ${shortClassName} {
<#list nameList as name>

    /** {@link ${shortEntityClassName}#${name}}の名前 */
    String ${name} = "${name}";
</#list>
}