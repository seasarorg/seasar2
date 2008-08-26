<#include "/copyright.ftl">
<#if packageName??>
package ${packageName};
</#if>

<#list importNameSet as importName>
import ${importName};
</#list>

/**
 * サービスの抽象クラスです。
 * 
 * @author S2JDBC-Gen
 * @param <ENTITY>
 *            エンティティの型 
 */
public abstract class ${shortClassName}<ENTITY> extends S2AbstractService<ENTITY> {
}