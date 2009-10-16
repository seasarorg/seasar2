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
 * 名前クラスの集約です。
 * 
<#if lib.author??>
 * @author ${lib.author}
</#if>
 */
@Generated(value = {<#list generatedInfoList as info>"${info}"<#if info_has_next>, </#if></#list>}, date = "${currentDate?datetime}")
public class ${shortClassName} {
<#list namesModelList as namesModel>

    /**
     * {@link ${namesModel.shortEntityClassName}}の名前クラスを返します。
     * 
     * @return ${namesModel.shortEntityClassName}の名前クラス
     */
    public static ${namesModel.shortInnerClassName} ${namesModel.shortEntityClassName?uncap_first}() {
        return new ${namesModel.shortInnerClassName}();
    }
</#list>
}