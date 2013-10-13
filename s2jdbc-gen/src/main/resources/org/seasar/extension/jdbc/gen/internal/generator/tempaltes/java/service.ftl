<#import "/lib.ftl" as lib>
<#if lib.copyright??>
${lib.copyright}
</#if>
<#if !lib.copyright??>
<#include "/copyright.ftl">
</#if>
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
 * {@link ${shortEntityClassName}}のサービスクラスです。
 * 
<#if lib.author??>
 * @author ${lib.author}
</#if>
 */
@Generated(value = {<#list generatedInfoList as info>"${info}"<#if info_has_next>, </#if></#list>}, date = "${currentDate?datetime}")
public class ${shortClassName} extends ${shortSuperclassName}<${shortEntityClassName}> {
<#if jdbcManagerSetterNecessary>

    /**
     * JDBCマネージャを設定します。
     * 
     * @param jdbcManager
     *            JDBCマネージャ
     */
    @Resource(name = "${jdbcManagerName}")
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public void setJdbcManager(JdbcManager jdbcManager) {
        this.jdbcManager = jdbcManager;
    }
</#if>
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
<#if idPropertyMetaList?size gt 0 && versionPropertyMeta??>

    /**
     * 識別子とバージョン番号でエンティティを検索します。
     * 
  <#list idPropertyMetaList as prop>
     * @param ${prop.name}
     *            識別子
  </#list>
     * @param ${versionPropertyMeta.name}
     *            バージョン番号
     * @return エンティティ
     */
    public ${shortEntityClassName} findByIdVersion(<#list idPropertyMetaList as prop>${prop.propertyClass.simpleName} ${prop.name}, </#list>${versionPropertyMeta.propertyClass.simpleName} ${versionPropertyMeta.name}) {
        return select().id(<#list idPropertyMetaList as prop>${prop.name}<#if prop_has_next>, </#if></#list>).version(${versionPropertyMeta.name}).getSingleResult();
    }
</#if>
<#if namesModel?? && idPropertyMetaList?size gt 0>

    /**
     * 識別子の昇順ですべてのエンティティを検索します。
     * 
     * @return エンティティのリスト
     */
    public List<${shortEntityClassName}> findAllOrderById() {
        return select().orderBy(<#list idPropertyMetaList as prop>asc(${prop.name}())<#if prop_has_next>, </#if></#list>).getResultList();
    }
</#if>
}