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
 * サービスの抽象クラスです。
 * 
 * @author S2JDBC-Gen
 * @param <ENTITY>
 *            エンティティの型 
 */
public abstract class ${shortClassName}<ENTITY> extends S2AbstractService<ENTITY> {
}