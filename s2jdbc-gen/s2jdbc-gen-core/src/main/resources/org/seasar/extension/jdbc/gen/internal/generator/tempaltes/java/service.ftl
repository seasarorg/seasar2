<#include "/copyright.ftl">
<#if packageName??>
package ${packageName};
</#if>

<#list importNameSet as importName>
import ${importName};
</#list>

/**
 * {@link ${shortEntityClassName}}のサービスクラスです。
 * 
 * @author S2JDBC-Gen
 */
public class ${shortClassName} extends AbstractService<${shortEntityClassName}> {
<#if idPropertyMetaList?size gt 0>

    /**
     * 識別子でエンティティを検索します。
     * 
  <#list idPropertyMetaList as prop>
     * @param ${prop.name}
     *            識別子
  </#list>
     * @return エンティティ
     */
    public ${shortEntityClassName} findById(<#list idPropertyMetaList as prop>${prop.propertyClass.simpleName} ${prop.name}<#if prop_has_next>, </#if></#list>) {
        return select().id(<#list idPropertyMetaList as prop>${prop.name}<#if prop_has_next>, </#if></#list>).getSingleResult();
    }
</#if>
}